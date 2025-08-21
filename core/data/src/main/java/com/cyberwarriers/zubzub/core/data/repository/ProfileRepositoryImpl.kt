package com.cyberwarriers.zubzub.core.data.repository

import com.cyberwarriers.zubzub.core.data.model.ProfileFirebaseEntity
import com.cyberwarriers.zubzub.core.domain.model.UserProfile
import com.cyberwarriers.zubzub.core.domain.repository.ProfileRepository
import com.cyberwarriers.zubzub.core.util.logd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProfileRepository {

    companion object {
        private const val PROFILES_COLLECTION = "profiles"
        private const val CART_GROUPS_COLLECTION = "cart_groups"
    }

    /**
     * 현재 사용자의 모든 프로필 조회 (실시간 업데이트)
     */
    override fun getAllProfilesFlow(): Flow<List<UserProfile>> = callbackFlow {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            logd("사용자가 로그인되지 않음")
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val userId = currentUser.uid
        logd("실시간 프로필 목록 리스너 등록: $userId")

        // Firebase 실시간 리스너 등록
        val listener = firestore.collection(PROFILES_COLLECTION)
            .whereEqualTo("user_id", userId)
            .whereEqualTo("is_active", true)
            .orderBy("updated_at", DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    logd("실시간 프로필 목록 에러: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    logd("Firebase 문서 개수: ${snapshot.documents.size}")

                    val profiles = snapshot.documents.mapNotNull { document ->
                        try {
                            logd("문서 ID: ${document.id}, 데이터: ${document.data}")
                            val entity = document.toObject(ProfileFirebaseEntity::class.java)
                                ?.copy(profileId = document.id)

                            if (entity != null) {
                                logd("엔티티 변환 성공: ${entity.profileName}")
                                // Flow에서는 suspend 함수를 직접 호출할 수 없으므로 기본값으로 설정
                                UserProfile(
                                    profileId = entity.profileId,
                                    nickname = entity.profileName,
                                    profileImageUrl = if (entity.profileImageUrl.isNotEmpty()) entity.profileImageUrl else null,
                                    currentGroupName = null, // 실시간 업데이트에서는 그룹 정보를 별도로 처리
                                    isActive = entity.isActive,
                                    createdAt = entity.createdAt,
                                    updatedAt = entity.updatedAt
                                )
                            } else {
                                logd("엔티티 변환 실패: null")
                                null
                            }
                        } catch (e: Exception) {
                            logd("프로필 변환 실패: ${e.message}")
                            null
                        }
                    }
                    logd("실시간 프로필 목록 업데이트: ${profiles.size}개")
                    trySend(profiles)
                } else {
                    logd("Firebase 스냅샷이 null")
                    trySend(emptyList())
                }
            }

        awaitClose {
            logd("실시간 프로필 목록 리스너 해제")
            listener.remove()
        }
    }

    /**
     * 현재 사용자의 모든 프로필 조회 (일회성)
     */
    override suspend fun getAllProfiles(): Result<List<UserProfile>> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))

            val userId = currentUser.uid

            val querySnapshot = firestore.collection(PROFILES_COLLECTION)
                .whereEqualTo("user_id", userId)
                .whereEqualTo("is_active", true)
                .orderBy("updated_at", DESCENDING)
                .get()
                .await()

            val profiles = querySnapshot.documents.mapNotNull { document ->
                try {
                    val entity = document.toObject(ProfileFirebaseEntity::class.java)
                        ?.copy(profileId = document.id)

                    entity?.toUserProfile()
                } catch (e: Exception) {
                    logd("프로필 변환 실패: ${e.message}")
                    null
                }
            }

            logd("프로필 목록 조회 완료: ${profiles.size}개")
            Result.success(profiles)

        } catch (e: Exception) {
            logd("프로필 목록 조회 실패: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 특정 프로필 조회
     */
    override suspend fun getProfileById(profileId: String): Result<UserProfile?> {
        return try {
            val document = firestore.collection(PROFILES_COLLECTION)
                .document(profileId)
                .get()
                .await()

            if (!document.exists()) {
                logd("프로필이 존재하지 않습니다: $profileId")
                return Result.success(null)
            }

            val entity = document.toObject(ProfileFirebaseEntity::class.java)
                ?.copy(profileId = document.id)
                ?: return Result.failure(Exception("프로필 데이터를 읽을 수 없습니다."))

            val profile = entity.toUserProfile()
            logd("프로필 조회 성공: ${profile.nickname}")
            Result.success(profile)

        } catch (e: Exception) {
            logd("프로필 조회 실패: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 프로필 생성
     */
    override suspend fun createProfile(
        nickname: String,
        profileImageUrl: String?
    ): Result<UserProfile> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))

            val userId = currentUser.uid
            val profileId = UUID.randomUUID().toString()
            val currentTime = System.currentTimeMillis()

            val entity = ProfileFirebaseEntity(
                profileId = profileId,
                userId = userId,
                profileName = nickname,
                profileImageUrl = profileImageUrl ?: "",
                isDefault = false,
                isActive = true,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            firestore.collection(PROFILES_COLLECTION)
                .document(profileId)
                .set(entity)
                .await()

            val profile = entity.toUserProfile()
            logd("프로필 생성 성공: ${profile.nickname}")
            Result.success(profile)

        } catch (e: Exception) {
            logd("프로필 생성 실패: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 프로필 업데이트
     */
    override suspend fun updateProfile(
        profileId: String,
        nickname: String?,
        profileImageUrl: String?,
        currentGroupName: String?
    ): Result<UserProfile> {
        return try {
            val updateData = mutableMapOf<String, Any>()

            nickname?.let { updateData["profile_name"] = it }
            profileImageUrl?.let { updateData["profile_image_url"] = it }
            currentGroupName?.let { updateData["current_group_name"] = it }

            updateData["updated_at"] = System.currentTimeMillis()

            if (updateData.isEmpty()) {
                return Result.failure(Exception("업데이트할 정보가 없습니다."))
            }

            firestore.collection(PROFILES_COLLECTION)
                .document(profileId)
                .update(updateData)
                .await()

            val updatedProfile = getProfileById(profileId).getOrThrow()
                ?: return Result.failure(Exception("프로필 업데이트에 실패했습니다."))

            logd("프로필 업데이트 성공: ${updatedProfile.nickname}")
            Result.success(updatedProfile)

        } catch (e: Exception) {
            logd("프로필 업데이트 실패: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 프로필 삭제
     */
    override suspend fun deleteProfile(profileId: String): Result<Unit> {
        return try {
            firestore.collection(PROFILES_COLLECTION)
                .document(profileId)
                .delete()
                .await()

            logd("프로필 삭제 성공: $profileId")
            Result.success(Unit)

        } catch (e: Exception) {
            logd("프로필 삭제 실패: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 사용자가 프로필을 가지고 있는지 확인
     */
    override suspend fun hasProfile(): Result<Boolean> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))

            val userId = currentUser.uid

            val querySnapshot = firestore.collection(PROFILES_COLLECTION)
                .whereEqualTo("user_id", userId)
                .whereEqualTo("is_active", true)
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

    /**
     * 사용자의 현재 활성 그룹명을 가져오는 함수
     */
    private suspend fun getCurrentUserGroupName(userId: String): String? {
        return try {
            logd("사용자 그룹 조회 시작: $userId")
            
            // 사용자가 속한 모든 활성 그룹 조회
            val querySnapshot = firestore.collection(CART_GROUPS_COLLECTION)
                .whereArrayContains("memberIds", userId)
                .whereEqualTo("status", "ACTIVE")
                .orderBy("updatedAt", DESCENDING)
                .get()
                .await()
            
            logd("조회된 그룹 개수: ${querySnapshot.documents.size}")
            
            if (!querySnapshot.isEmpty) {
                // 가장 최근에 업데이트된 그룹 선택
                val mostRecentGroup = querySnapshot.documents.firstOrNull()
                val groupName = mostRecentGroup?.getString("groupName")
                val groupId = mostRecentGroup?.id
                
                logd("사용자 현재 그룹: $groupName (ID: $groupId)")
                groupName
            } else {
                logd("사용자 현재 그룹 없음")
                null
            }
        } catch (e: Exception) {
            logd("그룹 조회 실패: ${e.message}")
            null
        }
    }

    /**
     * ProfileFirebaseEntity를 UserProfile로 변환하는 확장 함수
     */
    private suspend fun ProfileFirebaseEntity.toUserProfile(): UserProfile {
        val currentGroupName = getCurrentUserGroupName(this.userId)
        
        return UserProfile(
            profileId = this.profileId,
            nickname = this.profileName,
            profileImageUrl = this.profileImageUrl.ifEmpty { null },
            currentGroupName = currentGroupName,
            isActive = this.isActive,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}