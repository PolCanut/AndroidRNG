package edu.uoc.rng2.usecases

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import edu.uoc.rng2.Constants
import edu.uoc.rng2.Constants.GAME_PROFIT
import edu.uoc.rng2.models.GameResult
import edu.uoc.rng2.repositories.AuthRepository
import edu.uoc.rng2.repositories.GameResultRepository
import edu.uoc.rng2.repositories.datasources.AuthState
import edu.uoc.rng2.workers.UploadGameResultWorker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SaveGameResultUseCase @Inject constructor(
    private val gameResultRepository: GameResultRepository,
    private val authRepository: AuthRepository,
    private val workManager: WorkManager,
) {
    operator fun invoke(
        personTurns: Int,
        date: Long,
        userWon: Boolean,
        latitude: Double?,
        longitude: Double?,
    ): Maybe<Long> {
        val authState = authRepository.authState.value

        check(authState is AuthState.LoggedIn) { "User is not logged in!" }
        val user = authState.user

        val profit = if (userWon) {
            GAME_PROFIT
        } else {
            -GAME_PROFIT
        }

        val gameResult = GameResult(
            turns = personTurns,
            date = date,
            userWon = userWon,
            profit = profit,
            latitude = latitude,
            longitude = longitude,
            name = user.displayName ?: "",
            email = user.email ?: "",
        )

        return gameResultRepository.insertGameResult(gameResult)
            .map {
                scheduleUploadWork()
                it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun scheduleUploadWork() {
        workManager.beginUniqueWork(
            Constants.UPLOAD_GAME_RESULT,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.Companion.from(UploadGameResultWorker::class.java)
        ).enqueue()
    }
}