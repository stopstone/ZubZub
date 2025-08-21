package com.cyberwarriers.zubzub.feature.auth.data.repository

import com.cyberwarriers.zubzub.core.data.datastore.UserPreferences
import com.cyberwarriers.zubzub.core.domain.model.User
import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.auth.data.mapper.UserMapper.toDomain
import com.cyberwarriers.zubzub.feature.auth.data.mapper.UserMapper.toFirebaseEntity
import com.cyberwarriers.zubzub.feature.auth.data.model.UserFirebaseEntity
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
        private const val PROFILES_COLLECTION = "profiles"
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
                    isActive = true,
                )
                
                // DataStore에 로그인 정보 저장
                saveUserLoginInfoToDataStore(
                    userId = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "",
                    profileImageUrl = firebaseUser.photoUrl?.toString() ?: "",
                    provider = "google",
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
                    updateUserInFirestore(existingUser.copy(updatedAt = Timestamp.now().seconds,)).onFailure { error ->
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
        val userEntity = user.toFirebaseEntity()
        
        firestore.collection(USERS_COLLECTION)
            .document(user.userId)
            .set(userEntity)
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
            document.toObject(UserFirebaseEntity::class.java)?.toDomain()
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
        val userEntity = user.toFirebaseEntity()
        
        firestore.collection(USERS_COLLECTION)
            .document(user.userId)
            .set(userEntity)
            .await()
        
        logd("사용자 정보 Firestore 업데이트 완료: ${user.email}")
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
                provider = provider,
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
    
    override suspend fun hasUserProfile(): Result<Boolean> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            logd("프로필 존재 여부 확인 시작 - userId: $userId")
            
            // profiles 컬렉션에서 해당 사용자의 모든 활성 프로필 존재 확인
            val querySnapshot = firestore.collection(PROFILES_COLLECTION)
                .whereEqualTo("user_id", userId)
                .whereEqualTo("is_active", true)
                .limit(1)
                .get()
                .await()
            
            val hasProfile = !querySnapshot.isEmpty
            val documentCount = querySnapshot.size()
            logd("사용자 프로필 쿼리 결과 - 문서 개수: $documentCount, 프로필 존재: $hasProfile")
            
            // 실제 데이터가 있는지 전체 프로필을 확인해보기
            val allProfilesSnapshot = firestore.collection(PROFILES_COLLECTION)
                .whereEqualTo("user_id", userId)
                .get()
                .await()
            
            logd("전체 프로필 문서 개수: ${allProfilesSnapshot.size()}")
            allProfilesSnapshot.documents.forEachIndexed { index, document ->
                val data = document.data
                logd("프로필 $index: user_id=${data?.get("user_id")}, is_active=${data?.get("is_active")}, profile_name=${data?.get("profile_name")}")
            }
            
            Result.success(hasProfile)
            
        } catch (e: Exception) {
            logd("프로필 존재 여부 확인 실패: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun ensurePrimaryProfileExists(): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            
            // 기본 프로필이 있는지 확인
            val defaultProfileQuery = firestore.collection(PROFILES_COLLECTION)
                .whereEqualTo("user_id", userId)
                .whereEqualTo("is_default", true)
                .whereEqualTo("is_active", true)
                .limit(1)
                .get()
                .await()
            
            if (defaultProfileQuery.isEmpty) {
                // 기본 프로필이 없으면 첫 번째 프로필을 기본으로 설정
                val firstProfileQuery = firestore.collection(PROFILES_COLLECTION)
                    .whereEqualTo("user_id", userId)
                    .whereEqualTo("is_active", true)
                    .orderBy("created_at")
                    .limit(1)
                    .get()
                    .await()
                
                if (!firstProfileQuery.isEmpty) {
                    val firstProfile = firstProfileQuery.documents.first()
                    val firstProfileId = firstProfile.id
                    
                    // 첫 번째 프로필을 기본으로 설정
                    firestore.collection(PROFILES_COLLECTION)
                        .document(firstProfileId)
                        .update("is_default", true)
                        .await()
                    
                    logd("첫 번째 프로필을 기본 프로필로 설정: $firstProfileId")
                } else {
                    logd("설정할 프로필이 없습니다.")
                }
            } else {
                logd("기본 프로필이 이미 존재합니다.")
            }
            
            Result.success(Unit)
            
        } catch (e: Exception) {
            logd("기본 프로필 확인/설정 실패: ${e.message}")
            Result.failure(e)
        }
    }
}