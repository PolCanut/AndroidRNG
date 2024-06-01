package edu.uoc.rng2.ui.screens.bestGamesResult

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.uoc.rng2.models.GameResult
import edu.uoc.rng2.repositories.GameResultRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BestGamesViewModel @Inject constructor(
    private val gameResultRepository: GameResultRepository,
) : ViewModel() {
    private val _bestGameState = MutableStateFlow<BestGamesState>(BestGamesState.Loading)
    val bestGameState = _bestGameState.asStateFlow()

    private val compositeDisposable = CompositeDisposable()

    init {
        val disposable = gameResultRepository.getBestGames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _bestGameState.value = BestGamesState.Loaded(it)
                },
                {
                    _bestGameState.value = BestGamesState.Error
                }
            )

        compositeDisposable.add(disposable)
    }


    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}

sealed interface BestGamesState {
    data object Loading : BestGamesState
    data object Error : BestGamesState
    data class Loaded(val games: List<GameResult>) : BestGamesState
}