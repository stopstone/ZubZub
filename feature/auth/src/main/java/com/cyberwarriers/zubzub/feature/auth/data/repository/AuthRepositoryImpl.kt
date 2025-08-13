package com.cyberwarriers.zubzub.feature.auth.data.repository

import com.cyberwarriers.zubzub.core.data.datastore.UserPreferences
import com.cyberwarriers.zubzub.core.domain.model.User
import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
import com.cyberwarriers.zubzub.core.util.logd
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userPreferences: UserPreferences,
): AuthRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    // 구글 소셜 로그인
    override suspend fun signWithGoogle(
        account: Any,
    ): Result<Unit> = try {
        val googleAccount = account as GoogleSignInAccount
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        
        logd("Google 로그인 인증 성공!")
        
        // 로그인 성공 후 사용자 정보를 Firestore와 DataStore에 저장
        authResult.user?.let { firebaseUser ->
            try {
                val user = User(
                    userId = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    profileImageUrl = firebaseUser.photoUrl?.toString() ?: "",
                    provider = "google",
                    createdAt = Timestamp.now().seconds,
                    updatedAt = Timestamp.now().seconds,
                    isActive = true
                )
                
                // DataStore에 로그인 정보 저장
                saveUserLoginInfoToDataStore(
                    userId = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    profileImageUrl = firebaseUser.photoUrl?.toString() ?: "",
                    provider = "google"
                )
                
                // 기존 사용자인지 확인하고, 새 사용자라면 Firestore에 저장
                val existingUser = getUserFromFirestore(firebaseUser.uid).getOrNull()
                if (existingUser == null) {
                    logd("새 사용자 - Firestore에 저장 시도: ${user.email}")
                    saveUserToFirestore(user).onFailure { error ->
                        logd("Firestore 저장 실패하지만 로그인은 계속: ${error.message}")
                    }
                } else {
                    logd("기존 사용자 - 마지막 로그인 시간 업데이트: ${existingUser.email}")
                    updateUserInFirestore(existingUser.copy(updatedAt = Timestamp.now().seconds)).onFailure { error ->
                        logd("Firestore 업데이트 실패하지만 로그인은 계속: ${error.message}")
                    }
                }
            } catch (e: Exception) {
                logd("사용자 정보 저장 중 예외 발생하지만 로그인은 계속: ${e.message}")
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
        // DataStore에서도 로그인 정보 삭제
        clearUserLoginInfoFromDataStore()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 로그인 유저 id 반환
    override fun getCurrentUser(): Any? = firebaseAuth.currentUser

    // 현재 로그인 상태인지 검사
    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    // Firestore에 사용자 정보 저장
    override suspend fun saveUserToFirestore(user: User): Result<Unit> = try {
        // whereIn 쿼리를 위해 userId 필드를 명시적으로 추가
        val userDataWithId = mapOf(
            "userId" to user.userId,
            "email" to user.email,
            "displayName" to user.displayName,
            "profileImageUrl" to user.profileImageUrl,
            "provider" to user.provider,
            "createdAt" to Timestamp(user.createdAt, 0),
            "updatedAt" to Timestamp(user.updatedAt, 0),
            "isActive" to user.isActive
        )
        
        firestore.collection(USERS_COLLECTION)
            .document(user.userId)
            .set(userDataWithId)
            .await()
        
        logd("사용자 정보 Firestore 저장 완료 (userId 필드 포함): ${user.email}")
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
            val createdAt = document.getTimestamp("createdAt")?.seconds ?: 0L
            val updatedAt = document.getTimestamp("updatedAt")?.seconds ?: 0L
            
            User(
                userId = document.getString("userId") ?: "",
                email = document.getString("email") ?: "",
                displayName = document.getString("displayName") ?: "",
                profileImageUrl = document.getString("profileImageUrl") ?: "",
                provider = document.getString("provider") ?: "",
                createdAt = createdAt,
                updatedAt = updatedAt,
                isActive = document.getBoolean("isActive") ?: true
            )
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
        // whereIn 쿼리를 위해 userId 필드를 명시적으로 추가
        val userDataWithId = mapOf(
            "userId" to user.userId,
            "email" to user.email,
            "displayName" to user.displayName,
            "profileImageUrl" to user.profileImageUrl,
            "provider" to user.provider,
            "createdAt" to Timestamp(user.createdAt, 0),
            "updatedAt" to Timestamp(user.updatedAt, 0),
            "isActive" to user.isActive
        )
        
        firestore.collection(USERS_COLLECTION)
            .document(user.userId)
            .set(userDataWithId)
            .await()
        
        logd("사용자 정보 Firestore 업데이트 완료 (userId 필드 포함): ${user.email}")
        Result.success(Unit)
    } catch (e: Exception) {
        logd("사용자 정보 Firestore 업데이트 실패: ${e.message}")
        Result.failure(e)
    }
    
    // DataStore 관련 메서드들
    override suspend fun saveUserLoginInfoToDataStore(
        userId: String,
        email: String,
        displayName: String,
        profileImageUrl: String,
        provider: String
    ) {
        try {
            userPreferences.saveUserLoginInfo(
                userId = userId,
                email = email,
                displayName = displayName,
                profileImageUrl = profileImageUrl,
                provider = provider
            )
            logd("사용자 로그인 정보 DataStore 저장 완료: $email")
        } catch (e: Exception) {
            logd("사용자 로그인 정보 DataStore 저장 실패: ${e.message}")
        }
    }
    
    override suspend fun clearUserLoginInfoFromDataStore() {
        try {
            userPreferences.clearUserLoginInfo()
            logd("사용자 로그인 정보 DataStore 삭제 완료")
        } catch (e: Exception) {
            logd("사용자 로그인 정보 DataStore 삭제 실패: ${e.message}")
        }
    }
    
    override fun isUserLoggedInFromDataStore(): Flow<Boolean> {
        return userPreferences.isLoggedIn
    }
}