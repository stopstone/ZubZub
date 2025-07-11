package com.cyberwarriers.zubzub.feature.group_enter.data.repository

import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_enter.data.mapper.toDomain
import com.cyberwarriers.zubzub.feature.group_enter.data.model.GroupFirebaseEntity
import com.cyberwarriers.zubzub.feature.group_enter.data.model.MemberFirebaseEntity
import com.cyberwarriers.zubzub.feature.group_enter.domain.model.GroupInfo
import com.cyberwarriers.zubzub.feature.group_enter.domain.repository.GroupEnterRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * GroupEnterRepository 구현체
 */
class GroupEnterRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : GroupEnterRepository {

    companion object {
        private const val CART_GROUPS_COLLECTION = "cart_groups"
        private const val MEMBERS_SUBCOLLECTION = "members"
    }

    /**
     * 초대 코드로 그룹 정보 조회
     */
    override suspend fun getGroupByInviteCode(inviteCode: String): Result<GroupInfo?> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                return Result.failure(Exception("로그인이 필요합니다."))
            }
            
            logd("그룹 조회 시작: $inviteCode")

            val document = firestore
                .collection(CART_GROUPS_COLLECTION)
                .document(inviteCode)
                .get()
                .await()

            if (document.exists()) {
                val groupEntity = document.toObject(GroupFirebaseEntity::class.java)
                    ?.copy(groupId = document.id)
                
                if (groupEntity != null && groupEntity.isActive) {
                    logd("그룹 찾음: ${groupEntity.groupName}")
                    Result.success(groupEntity.toDomain(currentUser.uid))
                } else {
                    logd("비활성화된 그룹")
                    Result.success(null)
                }
            } else {
                logd("존재하지 않는 그룹: $inviteCode")
                Result.success(null)
            }
        } catch (exception: Exception) {
            logd("그룹 조회 실패: ${exception.message}")
            Result.failure(exception)
        }
    }

    /**
     * 그룹에 멤버로 참여
     */
    override suspend fun joinGroup(groupId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))

            val userId = currentUser.uid
            val userDisplayName = currentUser.displayName ?: "사용자"
            
            logd("그룹 참여 시작: $groupId, 사용자: $userId")

            // 1. 그룹이 존재하는지 확인
            val groupRef = firestore.collection(CART_GROUPS_COLLECTION).document(groupId)
            val groupSnapshot = groupRef.get().await()
            
            if (!groupSnapshot.exists()) {
                return Result.failure(Exception("존재하지 않는 그룹입니다."))
            }

            val group = groupSnapshot.toObject(GroupFirebaseEntity::class.java)
            if (group?.isActive != true) {
                return Result.failure(Exception("비활성화된 그룹입니다."))
            }

            // 2. 이미 멤버인지 확인
            if (group.memberIds.contains(userId)) {
                logd("이미 그룹 멤버: $userId")
                return Result.success(Unit)
            }

            // 3. 멤버 추가
            val newMember = MemberFirebaseEntity(
                userId = userId,
                nickname = userDisplayName,
                profileImage = currentUser.photoUrl?.toString() ?: "",
                role = "MEMBER",
                joinedAt = Timestamp.now(),
                isActive = true
            )

            // 4. Firebase 트랜잭션으로 동시에 업데이트
            firestore.runTransaction { transaction ->
                // 그룹의 memberIds 업데이트
                val updatedMemberIds = group.memberIds + userId
                transaction.update(groupRef, "memberIds", updatedMemberIds)
                transaction.update(groupRef, "updatedAt", Timestamp.now())

                // members 서브컬렉션에 멤버 추가
                val memberRef = groupRef.collection(MEMBERS_SUBCOLLECTION).document(userId)
                transaction.set(memberRef, newMember)
            }.await()

            logd("그룹 참여 완료: $groupId")
            Result.success(Unit)
            
        } catch (exception: Exception) {
            logd("그룹 참여 실패: ${exception.message}")
            Result.failure(exception)
        }
    }
} 