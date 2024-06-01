package edu.uoc.rng2.repositories.datasources

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AuthDataSource"

@Singleton
class AuthDataSource @Inject constructor() {
    private val auth = Firebase.auth

    private val _authState = MutableStateFlow(getNewAuthState())
    val authState = _authState.asStateFlow()

    init {
        auth.addAuthStateListener {
            _authState.value = getNewAuthState()
        }
    }

    private fun getNewAuthState(): AuthState {
        val user = auth.currentUser

        return if (user != null) {
            AuthState.LoggedIn(user)
        } else {
            AuthState.NotLogged
        }
    }
}

sealed interface AuthState {
    data object NotLogged : AuthState
    class LoggedIn(val user: FirebaseUser) : AuthState
}