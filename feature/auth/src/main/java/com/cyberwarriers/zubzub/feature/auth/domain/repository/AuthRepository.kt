package com.cyberwarriers.zubzub.feature.auth.domain.repository

import com.cyberwarriers.zubzub.feature.auth.domain.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signWithGoogle(account: GoogleSignInAccount): Result<Unit>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): FirebaseUser?
    fun isUserLoggedIn(): Boolean
    
    // Firestore 관련 기능
    suspend fun saveUserToFirestore(user: User): Result<Unit>
    suspend fun getUserFromFirestore(userId: String): Result<User?>
    suspend fun updateUserInFirestore(user: User): Result<Unit>
}