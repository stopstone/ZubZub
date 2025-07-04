package com.cyberwarriers.zubzub.feature.auth.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signWithGoogle(account: GoogleSignInAccount): Result<Unit>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): FirebaseUser?
    fun isUserLoggedIn(): Boolean
}