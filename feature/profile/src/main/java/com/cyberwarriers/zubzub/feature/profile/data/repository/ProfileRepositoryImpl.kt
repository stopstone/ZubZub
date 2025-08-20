package com.cyberwarriers.zubzub.feature.profile.data.repository

import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.data.mapper.ProfileMapper.toDomain
import com.cyberwarriers.zubzub.feature.profile.data.model.ProfileFirebaseEntity
import com.cyberwarriers.zubzub.feature.profile.domain.model.CreateProfileRequest
import com.cyberwarriers.zubzub.feature.profile.domain.model.Profile
import com.cyberwarriers.zubzub.feature.profile.domain.model.UpdateProfileRequest
import com.cyberwarriers.zubzub.feature.profile.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * 프로필 레포지토리 구현체
 * 
 * Firebase Firestore를 사용하여 프로필 정보를 관리
 */
class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val authRepository: AuthRepository
) : ProfileRepository {
    
    companion object {
        private const val PROFILES_COLLECTION = "profiles"
        private const val USERS_COLLECTION = "users"
    }
    
    override suspend fun createProfile(request: CreateProfileRequest): Result<Profile> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            
            // 프로필 데이터 생성
            val profileEntity = ProfileFirebaseEntity(
                userId = userId,
                profileName = request.profileName,
                profileImageUrl = request.profileImageUrl
            )
            
            // Firestore에 프로필 저장
            firestore.collection(PROFILES_COLLECTION)
                .document(userId)
                .set(profileEntity)
                .await()
            
            // users 컬렉션의 hasProfile 필드 업데이트
            val userUpdateData = mapOf(
                "hasProfile" to true,
                "profileName" to request.profileName,
                "customProfileImageUrl" to request.profileImageUrl
            )
            
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .update(userUpdateData)
                .await()
            
            // 생성된 프로필 조회
            val createdProfile = firestore.collection(PROFILES_COLLECTION)
                .document(userId)
                .get()
                .await()
                .toObject(ProfileFirebaseEntity::class.java)
                ?: return Result.failure(Exception("프로필 생성에 실패했습니다."))
            
            logd("프로필 생성 성공: ${createdProfile.profileName}")
            Result.success(createdProfile.toDomain())
            
        } catch (e: Exception) {
            logd("프로필 생성 실패: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            val updateData = mutableMapOf<String, Any>()
            
            request.profileName?.let { 
                updateData["profileName"] = it
            }
            request.profileImageUrl?.let { 
                updateData["profileImageUrl"] = it
            }
            
            if (updateData.isEmpty()) {
                return Result.failure(Exception("업데이트할 정보가 없습니다."))
            }
            
            // Firestore 프로필 업데이트
            firestore.collection(PROFILES_COLLECTION)
                .document(userId)
                .update(updateData)
                .await()
            
            // users 컬렉션도 업데이트
            val userUpdateData = mutableMapOf<String, Any>()
            request.profileName?.let { userUpdateData["profileName"] = it }
            request.profileImageUrl?.let { userUpdateData["customProfileImageUrl"] = it }
            
            if (userUpdateData.isNotEmpty()) {
                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .update(userUpdateData)
                    .await()
            }
            
            // 업데이트된 프로필 조회
            val updatedProfile = firestore.collection(PROFILES_COLLECTION)
                .document(userId)
                .get()
                .await()
                .toObject(ProfileFirebaseEntity::class.java)
                ?: return Result.failure(Exception("프로필 업데이트에 실패했습니다."))
            
            logd("프로필 업데이트 성공: ${updatedProfile.profileName}")
            Result.success(updatedProfile.toDomain())
            
        } catch (e: Exception) {
            logd("프로필 업데이트 실패: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun getProfile(): Result<Profile?> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            
            val profileDoc = firestore.collection(PROFILES_COLLECTION)
                .document(userId)
                .get()
                .await()
            
            if (!profileDoc.exists()) {
                logd("프로필이 존재하지 않습니다.")
                return Result.success(null)
            }
            
            val profileEntity = profileDoc.toObject(ProfileFirebaseEntity::class.java)
                ?: return Result.failure(Exception("프로필 데이터를 읽을 수 없습니다."))
            
            logd("프로필 조회 성공: ${profileEntity.profileName}")
            Result.success(profileEntity.toDomain())
            
        } catch (e: Exception) {
            logd("프로필 조회 실패: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun uploadProfileImage(imageUri: String): Result<String> {
        // TODO: Firebase Storage 구현 시 이미지 업로드 로직 추가
        // 현재는 기본 이미지 URL 반환
        return try {
            logd("프로필 이미지 업로드: $imageUri")
            // 임시로 기본 URL 반환
            Result.success("")
        } catch (e: Exception) {
            logd("프로필 이미지 업로드 실패: ${e.message}")
            Result.failure(e)
        }
    }
}
