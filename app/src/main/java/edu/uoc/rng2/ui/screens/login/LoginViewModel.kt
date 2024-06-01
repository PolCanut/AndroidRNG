package edu.uoc.rng2.ui.screens.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.uoc.rng2.repositories.AuthRepository
import edu.uoc.rng2.repositories.GameResultRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val gameResultRepository: GameResultRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val authState = authRepository.authState
}
