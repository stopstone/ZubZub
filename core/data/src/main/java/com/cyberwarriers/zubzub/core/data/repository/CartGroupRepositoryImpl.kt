package com.cyberwarriers.zubzub.core.data.repository

import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.core.data.mapper.createCartGroupEntity
import com.cyberwarriers.zubzub.core.data.mapper.createOwnerMember
import com.cyberwarriers.zubzub.core.data.mapper.toSummary
import com.cyberwarriers.zubzub.core.data.model.CartGroupFirebaseEntity
import com.cyberwarriers.zubzub.core.domain.model.CartGroupSummary
import com.cyberwarriers.zubzub.core.domain.repository.CartGroupRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class CartGroupRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CartGroupRepository {

    companion object {
        private const val CART_GROUPS_COLLECTION = "cart_groups"
        private const val MEMBERS_SUBCOLLECTION = "members"
    }

    /**
     * 새로운 그룹 생성
     */
    override suspend fun createGroup(
        groupName: String,
        description: String,
        targetAmount: Long
    ): Result<String> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            val userId = currentUser.uid
            val userDisplayName = currentUser.displayName ?: "사용자"
            
            logd("그룹 생성 시작: $groupName, 생성자: $userId")
            
            // 1. 그룹 문서 생성
            val groupEntity = createCartGroupEntity(
                groupName = groupName,
                description = description,
                createdBy = userId,
                targetAmount = targetAmount
            )
            
            // 2. Firestore에 그룹 저장
            val groupRef = firestore.collection(CART_GROUPS_COLLECTION).document()
            val groupWithId = groupEntity.copy(groupId = groupRef.id)
            
            groupRef.set(groupWithId).await()
            
            // 3. 생성자를 첫 번째 멤버로 추가
            val ownerMember = createOwnerMember(
                userId = userId,
                nickname = userDisplayName,
                profileImage = currentUser.photoUrl?.toString() ?: ""
            )
            
            groupRef.collection(MEMBERS_SUBCOLLECTION)
                .document(userId)
                .set(ownerMember)
                .await()
            
            logd("그룹 생성 완료: ${groupRef.id}")
            Result.success(groupRef.id)
            
        } catch (e: Exception) {
            logd("그룹 생성 실패: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 사용자가 속한 그룹 목록 조회 (실시간 업데이트)
     */
    override fun getUserGroups(): Flow<List<CartGroupSummary>> = callbackFlow {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        
        val userId = currentUser.uid
        logd("🔥 실시간 그룹 목록 리스너 등록: $userId")
        
        // 🔥 Firebase 실시간 리스너 등록
        val listener = firestore.collection(CART_GROUPS_COLLECTION)
            .whereArrayContains("memberIds", userId)
            .orderBy("updatedAt", DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    logd("❌ 실시간 그룹 목록 에러: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val groups = snapshot.documents.mapNotNull { document ->
                        try {
                            document.toObject(CartGroupFirebaseEntity::class.java)
                                ?.copy(groupId = document.id)
                                ?.toSummary(userId)
                        } catch (e: Exception) {
                            logd("그룹 변환 실패: ${e.message}")
                            null
                        }
                    }
                    
                    logd("🚀 실시간 그룹 업데이트: ${groups.size}개")
                    trySend(groups)
                }
            }
        
        // 🧹 리스너 정리 (Flow 종료시 자동 호출)
        awaitClose {
            logd("🧹 실시간 그룹 목록 리스너 해제")
            listener.remove()
        }
    }

    /**
     * 특정 그룹 상세 정보 조회
     */
    suspend fun getGroupDetail(groupId: String): Result<CartGroupFirebaseEntity?> {
        return try {
            logd("그룹 상세 조회: $groupId")
            
            val snapshot = firestore.collection(CART_GROUPS_COLLECTION)
                .document(groupId)
                .get()
                .await()
            
            if (snapshot.exists()) {
                val group = snapshot.toObject(CartGroupFirebaseEntity::class.java)
                    ?.copy(groupId = snapshot.id)
                Result.success(group)
            } else {
                Result.success(null)
            }
            
        } catch (e: Exception) {
            logd("그룹 상세 조회 실패: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 그룹 정보 업데이트
     */
    override suspend fun updateGroup(
        groupId: String,
        updates: Map<String, Any>
    ): Result<Unit> {
        return try {
            logd("그룹 업데이트: $groupId")
            
            firestore.collection(CART_GROUPS_COLLECTION)
                .document(groupId)
                .update(updates)
                .await()
            
            logd("그룹 업데이트 완료: $groupId")
            Result.success(Unit)
            
        } catch (e: Exception) {
            logd("그룹 업데이트 실패: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 그룹 삭제
     */
    override suspend fun deleteGroup(groupId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("로그인이 필요합니다."))
            
            logd("그룹 삭제: $groupId")
            
            // 그룹 소유자인지 확인
            val groupSnapshot = firestore.collection(CART_GROUPS_COLLECTION)
                .document(groupId)
                .get()
                .await()
            
            val group = groupSnapshot.toObject(CartGroupFirebaseEntity::class.java)
            if (group?.createdBy != currentUser.uid) {
                return Result.failure(Exception("그룹 삭제 권한이 없습니다."))
            }
            
            // 그룹 문서 삭제
            firestore.collection(CART_GROUPS_COLLECTION)
                .document(groupId)
                .delete()
                .await()
            
            logd("그룹 삭제 완료: $groupId")
            Result.success(Unit)
            
        } catch (e: Exception) {
            logd("그룹 삭제 실패: ${e.message}")
            Result.failure(e)
        }
    }
} 