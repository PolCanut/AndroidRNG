package edu.uoc.rng2.repositories

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.uoc.rng2.repositories.datasources.AuthDataSource
import javax.inject.Inject

private const val TAG = "AuthRepository"

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
) {
    val authState = authDataSource.authState
}