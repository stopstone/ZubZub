package com.cyberwarriers.zubzub.feature.auth.data.repository

import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.auth.domain.model.User
import com.cyberwarriers.zubzub.feature.auth.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
): AuthRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    // 구글 소셜 로그인
    override suspend fun signWithGoogle(
        account: GoogleSignInAccount,
    ): Result<Unit> = try {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        
        logd("Google 로그인 인증 성공!")
        
        // 로그인 성공 후 사용자 정보를 Firestore에 저장 (실패해도 로그인은 성공으로 처리)
        authResult.user?.let { firebaseUser ->
            try {
                val user = User(
                    userId = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    profileImageUrl = firebaseUser.photoUrl?.toString() ?: "",
                    provider = "google",
                    createdAt = Timestamp.now(),
                    updatedAt = Timestamp.now(),
                    isActive = true
                )
                
                // 기존 사용자인지 확인하고, 새 사용자라면 저장
                val existingUser = getUserFromFirestore(firebaseUser.uid).getOrNull()
                if (existingUser == null) {
                    logd("새 사용자 - Firestore에 저장 시도: ${user.email}")
                    saveUserToFirestore(user).onFailure { error ->
                        logd("Firestore 저장 실패하지만 로그인은 계속: ${error.message}")
                    }
                } else {
                    logd("기존 사용자 - 마지막 로그인 시간 업데이트: ${existingUser.email}")
                    updateUserInFirestore(existingUser.copy(updatedAt = Timestamp.now())).onFailure { error ->
                        logd("Firestore 업데이트 실패하지만 로그인은 계속: ${error.message}")
                    }
                }
            } catch (e: Exception) {
                logd("Firestore 작업 중 예외 발생하지만 로그인은 계속: ${e.message}")
            }
        }
        
        Result.success(Unit)
    } catch (e: Exception) {
        logd("Google 로그인 실패: ${e.message}")
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

    // Firestore에 사용자 정보 저장
    override suspend fun saveUserToFirestore(user: User): Result<Unit> = try {
        firestore.collection(USERS_COLLECTION)
            .document(user.userId)
            .set(user)
            .await()
        
        logd("사용자 정보 Firestore 저장 완료: ${user.email}")
        Result.success(Unit)
    } catch (e: Exception) {
        logd("사용자 정보 Firestore 저장 실패: ${e.message}")
        Result.failure(e)
    }

    // Firestore에서 사용자 정보 조회
    override suspend fun getUserFromFirestore(userId: String): Result<User?> = try {
        val document = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .await()
        
        val user = if (document.exists()) {
            document.toObject(User::class.java)
        } else {
            null
        }
        
        logd("사용자 정보 Firestore 조회 완료: ${user?.email}")
        Result.success(user)
    } catch (e: Exception) {
        logd("사용자 정보 Firestore 조회 실패: ${e.message}")
        Result.failure(e)
    }

    // Firestore에서 사용자 정보 업데이트
    override suspend fun updateUserInFirestore(user: User): Result<Unit> = try {
        firestore.collection(USERS_COLLECTION)
            .document(user.userId)
            .set(user)
            .await()
        
        logd("사용자 정보 Firestore 업데이트 완료: ${user.email}")
        Result.success(Unit)
    } catch (e: Exception) {
        logd("사용자 정보 Firestore 업데이트 실패: ${e.message}")
        Result.failure(e)
    }
}