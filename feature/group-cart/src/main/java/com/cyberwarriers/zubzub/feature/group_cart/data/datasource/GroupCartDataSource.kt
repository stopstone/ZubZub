package com.cyberwarriers.zubzub.feature.group_cart.data.datasource

import com.cyberwarriers.zubzub.feature.group_cart.data.model.CartItemEntity
import com.cyberwarriers.zubzub.feature.group_cart.data.model.GroupCartDetailEntity
import com.cyberwarriers.zubzub.feature.group_cart.data.model.GroupMemberEntity
import com.cyberwarriers.zubzub.core.util.logd
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 그룹 카트 데이터 소스 (Firebase Firestore 연동)
 */
@Singleton
class GroupCartDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    companion object {
        private const val GROUPS_COLLECTION = "cart_groups"
        private const val CART_ITEMS_SUBCOLLECTION = "cartItems"
    }
    
    /**
     * 그룹 카트 상세 정보 조회 (실시간)
     */
    fun getGroupCartDetail(groupId: String): Flow<GroupCartDetailEntity?> = callbackFlow {
        logd("Firebase 그룹 조회 시작 - 그룹 ID: $groupId")
        val groupRef = firestore.collection(GROUPS_COLLECTION).document(groupId)
        
        val groupListener = groupRef.addSnapshotListener { groupSnapshot, error ->
            if (error != null) {
                logd("Firebase 그룹 조회 에러: ${error.message}")
                close(error)
                return@addSnapshotListener
            }
            
            if (groupSnapshot?.exists() == true) {
                logd("Firebase 그룹 문서 발견")
                logd("전체 문서 데이터: ${groupSnapshot.data}")
                
                // 모든 필드를 로그로 출력해서 구조 파악
                groupSnapshot.data?.forEach { (key, value) ->
                    logd("필드: $key = $value (타입: ${value?.javaClass?.simpleName})")
                }
                
                // 그룹 기본 정보 추출
                val groupName = groupSnapshot.getString("groupName") ?: ""
                val memberIds = groupSnapshot.get("memberIds") as? List<String> ?: emptyList()
                val memberCount = memberIds.size
                
                logd("추출된 그룹 정보 - 이름: '$groupName', 멤버 ID들: $memberIds, 멤버수: $memberCount")
                
                // memberIds가 있으면 users 컬렉션에서 실제 사용자 정보 조회
                if (memberIds.isNotEmpty()) {
                    logd("사용자 정보 조회 시작 - ${memberIds.size}명")
                    logd("조회할 memberIds: $memberIds")
                    
                    // whereIn으로 한번에 모든 사용자 조회 (효율적인 방식)
                    firestore.collection("users")
                        .whereIn("userId", memberIds)
                        .addSnapshotListener { usersSnapshot, usersError ->
                            if (usersError != null) {
                                logd("사용자 정보 조회 에러: ${usersError.message}")
                                // 에러가 있어도 그룹 정보는 표시 (빈 멤버 리스트)
                                val groupCartDetail = GroupCartDetailEntity(
                                    groupId = groupId,
                                    groupName = groupName,
                                    memberCount = memberCount,
                                    cartItems = emptyList(),
                                    members = emptyList()
                                )
                                trySend(groupCartDetail)
                                return@addSnapshotListener
                            }
                            
                            logd("사용자 정보 조회 결과: ${usersSnapshot?.documents?.size ?: 0}명")
                            
                            val members = usersSnapshot?.documents?.mapNotNull { userDoc ->
                                logd("사용자 데이터: ${userDoc.data}")
                                try {
                                    GroupMemberEntity(
                                        id = userDoc.getString("userId") ?: userDoc.id,
                                        name = userDoc.getString("displayName") ?: userDoc.getString("name") ?: "익명",
                                        email = userDoc.getString("email") ?: "",
                                        profileImageUrl = userDoc.getString("profileImageUrl") ?: "",
                                        isGroupLeader = (userDoc.getString("userId") ?: userDoc.id) == groupSnapshot.getString("createdBy"),
                                        joinedAt = userDoc.getTimestamp("createdAt")?.toDate()?.time ?: 0L,
                                        totalContribution = 0L,
                                        isActive = userDoc.getBoolean("isActive") ?: true
                                    )
                                } catch (e: Exception) {
                                    logd("사용자 정보 파싱 에러: ${e.message}")
                                    null
                                }
                            }?.sortedWith(compareBy<GroupMemberEntity> { !it.isGroupLeader }.thenBy { it.joinedAt }) ?: emptyList()
                            
                            logd("파싱된 멤버 정보: ${members.size}명")
                            members.forEach { member ->
                                logd("멤버: ${member.name} (${member.email}), 리더: ${member.isGroupLeader}, 입장일: ${java.text.SimpleDateFormat("MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date(member.joinedAt))}")
                            }
                            
                            // 전체 데이터 조합
                            val groupCartDetail = GroupCartDetailEntity(
                                groupId = groupId,
                                groupName = groupName,
                                memberCount = memberCount,
                                cartItems = emptyList(),
                                members = members
                            )
                            
                            logd("최종 데이터 조합 완료 - 그룹: '$groupName', 멤버: ${members.size}명")
                            trySend(groupCartDetail)
                        }
                } else {
                    logd("멤버 ID가 없음 - 빈 그룹")
                    // memberIds가 없으면 빈 그룹으로 처리
                    val groupCartDetail = GroupCartDetailEntity(
                        groupId = groupId,
                        groupName = groupName,
                        memberCount = 0,
                        cartItems = emptyList(),
                        members = emptyList()
                    )
                    trySend(groupCartDetail)
                }
            } else {
                logd("Firebase 그룹 문서가 존재하지 않음: $groupId")
                trySend(null)
            }
        }
        
        awaitClose { 
            groupListener.remove()
            logd("Firebase 그룹 리스너 해제: $groupId")
        }
    }

    /**
     * 카트 아이템 상태 업데이트
     */
    suspend fun updateCartItemStatus(groupId: String, itemId: String, isCompleted: Boolean): Boolean {
        return try {
            firestore.collection(GROUPS_COLLECTION)
                .document(groupId)
                .collection(CART_ITEMS_SUBCOLLECTION)
                .document(itemId)
                .update("isCompleted", isCompleted)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 카트 아이템 추가
     */
    suspend fun addCartItem(
        groupId: String,
        name: String,
        price: Long,
        quantity: Int,
        addedBy: String
    ): String? {
        return try {
            val cartItemData = mapOf(
                "name" to name,
                "price" to price,
                "quantity" to quantity,
                "addedBy" to addedBy,
                "addedAt" to System.currentTimeMillis(),
                "isCompleted" to false
            )
            
            val docRef = firestore.collection(GROUPS_COLLECTION)
                .document(groupId)
                .collection(CART_ITEMS_SUBCOLLECTION)
                .add(cartItemData)
                .await()
            
            docRef.id
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 카트 아이템 삭제
     */
    suspend fun removeCartItem(groupId: String, itemId: String): Boolean {
        return try {
            firestore.collection(GROUPS_COLLECTION)
                .document(groupId)
                .collection(CART_ITEMS_SUBCOLLECTION)
                .document(itemId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
} 