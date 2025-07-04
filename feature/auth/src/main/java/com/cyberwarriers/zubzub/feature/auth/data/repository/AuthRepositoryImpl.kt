package com.cyberwarriers.zubzub.feature.auth.data.repository

import com.cyberwarriers.zubzub.feature.auth.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
): AuthRepository {

    // 구글 소셜 로그인
    override suspend fun signWithGoogle(
        account: GoogleSignInAccount,
    ): Result<Unit> = try {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 구글 로그아웃
    override suspend fun signOut(): Result<Unit> = try {
        firebaseAuth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 로그인 유저 id 반환
    override fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    // 현재 로그인 상태인지 검사
    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null
}