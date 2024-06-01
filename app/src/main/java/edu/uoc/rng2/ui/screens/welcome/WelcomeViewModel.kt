package edu.uoc.rng2.ui.screens.welcome

// Importaciones necesarias
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.uoc.rng2.PlaybackMediaController
import edu.uoc.rng2.models.GameResult
import edu.uoc.rng2.repositories.AuthRepository
import edu.uoc.rng2.repositories.GameResultRepository
import edu.uoc.rng2.repositories.MusicLibraryRepository
import edu.uoc.rng2.repositories.datasources.AuthState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val TAG = "WelcomeViewModel"

// ViewModel para la pantalla de bienvenida
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val gameResultRepository: GameResultRepository,
    private val playbackMediaController: PlaybackMediaController,
    private val musicLibraryRepository: MusicLibraryRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val isAudioMuted = playbackMediaController.isMuted
    val currentSong = musicLibraryRepository.currentSong

    private val _bestGameResultState =
        MutableStateFlow<BestGameResultState>(BestGameResultState.None)
    val bestGameResultState = _bestGameResultState.asStateFlow()

    // Manejador de suscripciones.
    private val compositeDisposable = CompositeDisposable()

    // Limpia las suscripciones cuando se destruye el ViewModel.
    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    init {
        val authState = authRepository.authState.value

        if (authState is AuthState.LoggedIn) {
            val disposable = gameResultRepository.bestGameResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.email == authState.user.email) {
                            _bestGameResultState.value = BestGameResultState.PlayerIsBest(it)
                        } else {
                            _bestGameResultState.value = BestGameResultState.None
                        }
                    },
                    {
                        Log.e(TAG, "Error checking best player: ${it.message}", it)
                        _bestGameResultState.value = BestGameResultState.None
                    }
                )

            compositeDisposable.add(disposable)
        }

    }

    fun alternatePauseMusic() {
        playbackMediaController.alternatePause()
    }

    fun removeCustomSong() {
        musicLibraryRepository.removeCustomSong()
    }

    // LiveData que contiene el saldo actual del jugador
    val currentBalance =
        gameResultRepository.currentBalance() // Accede al saldo actual desde el repositorio
}

interface BestGameResultState {
    data object None : BestGameResultState
    data class PlayerIsBest(val gameResult: GameResult) : BestGameResultState

}