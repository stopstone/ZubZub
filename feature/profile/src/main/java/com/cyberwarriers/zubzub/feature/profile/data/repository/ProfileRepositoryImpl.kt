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
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 * 프로필 레포지토리 구현체
 * 
 * Firebase Firestore를 사용하여 프로필 정보를 관리
 * 트랜잭션을 사용하여 데이터 일관성을 보장하고 다중 프로필을 지원
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
            val profileId = UUID.randomUUID().toString()
            
            // 기존 프로필 존재 여부 확인
            val hasExistingProfile = hasProfile().getOrElse { false }
            
            // Firestore 트랜잭션으로 데이터 일관성 보장
            val createdProfile = firestore.runTransaction { transaction ->
                val profileEntity = ProfileFirebaseEntity(
                    profileId = profileId,
                    userId = userId,
                    profileName = request.profileName,
                    profileImageUrl = request.profileImageUrl,
                    isDefault = !hasExistingProfile, // 첫 번째 프로필은 기본 프로필
                    isActive = true,
                )
                
                // 1. 프로필 저장
                val profileRef = firestore.collection(PROFILES_COLLECTION).document(profileId)
                transaction.set(profileRef, profileEntity)
                
                // 2. users 컬렉션의 hasProfile 필드 업데이트 (첫 프로필인 경우만)
                if (!hasExistingProfile) {
                    val userRef = firestore.collection(USERS_COLLECTION).document(userId)
                    transaction.update(userRef, "hasProfile", true)
                }
                
                profileEntity.toDomain()
            }.await()
            
            logd("프로필 생성 성공: ${createdProfile.profileName}")
            Result.success(createdProfile)
            
        } catch (e: Exception) {
            logd("프로필 생성 실패: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
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
                .document(request.profileId)
                .update(updateData)
                .await()
            
            // 업데이트된 프로필 조회
            val updatedProfile = getProfileById(request.profileId).getOrThrow()
                ?: return Result.failure(Exception("프로필 업데이트에 실패했습니다."))
            
            logd("프로필 업데이트 성공: ${updatedProfile.profileName}")
            Result.success(updatedProfile)
            
        } catch (e: Exception) {
            logd("프로필 업데이트 실패: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun getDefaultProfile(): Result<Profile?> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            
            val querySnapshot = firestore.collection(PROFILES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isDefault", true)
                .whereEqualTo("isActive", true)
                .limit(1)
                .get()
                .await()
            
            val profile = querySnapshot.documents.firstOrNull()
                ?.toObject(ProfileFirebaseEntity::class.java)
                ?.toDomain()
            
            logd("기본 프로필 조회: ${profile?.profileName ?: "없음"}")
            Result.success(profile)
            
        } catch (e: Exception) {
            logd("기본 프로필 조회 실패: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun getAllProfiles(): Result<List<Profile>> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            
            val querySnapshot = firestore.collection(PROFILES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isActive", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val profiles = querySnapshot.documents.mapNotNull { document ->
                document.toObject(ProfileFirebaseEntity::class.java)?.toDomain()
            }
            
            logd("모든 프로필 조회 완료: ${profiles.size}개")
            Result.success(profiles)
            
        } catch (e: Exception) {
            logd("모든 프로필 조회 실패: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun getProfileById(profileId: String): Result<Profile?> {
        return try {
            val profileDoc = firestore.collection(PROFILES_COLLECTION)
                .document(profileId)
                .get()
                .await()
            
            if (!profileDoc.exists()) {
                logd("프로필이 존재하지 않습니다: $profileId")
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
    
    override suspend fun hasProfile(): Result<Boolean> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            
            val querySnapshot = firestore.collection(PROFILES_COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isActive", true)
                .limit(1)
                .get()
                .await()
            
            val hasProfile = !querySnapshot.isEmpty
            logd("프로필 존재 여부: $hasProfile")
            
            Result.success(hasProfile)
            
        } catch (e: Exception) {
            logd("프로필 존재 여부 확인 실패: ${e.message}")
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