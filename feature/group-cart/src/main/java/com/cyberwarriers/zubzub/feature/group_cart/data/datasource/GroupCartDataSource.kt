package com.cyberwarriers.zubzub.feature.group_cart.data.datasource

import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_cart.data.model.CartItemEntity
import com.cyberwarriers.zubzub.feature.group_cart.data.model.GroupCartDetailEntity
import com.cyberwarriers.zubzub.feature.group_cart.data.model.GroupMemberEntity
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
        
        // 그룹 정보와 카트 아이템을 동시에 관찰하기 위한 변수들
        var currentGroupData: Map<String, Any>? = null
        var currentCartItems: List<CartItemEntity> = emptyList()
        var currentMembers: List<GroupMemberEntity> = emptyList()
        
        // 데이터 조합 및 전송 함수
        fun combineAndSendData() {
            currentGroupData?.let { groupData ->
                val groupName = groupData["groupName"] as? String ?: ""
                val memberIds = groupData["memberIds"] as? List<String> ?: emptyList()
                val memberCount = memberIds.size
                
                val groupCartDetail = GroupCartDetailEntity(
                    groupId = groupId,
                    groupName = groupName,
                    memberCount = memberCount,
                    cartItems = currentCartItems,
                    members = currentMembers
                )
                
                logd("데이터 조합 완료 - 그룹: '$groupName', 아이템: ${currentCartItems.size}개, 멤버: ${currentMembers.size}명")
                trySend(groupCartDetail)
            }
        }
        
        // 1. 그룹 정보 리스너
        val groupListener = groupRef.addSnapshotListener { groupSnapshot, error ->
            if (error != null) {
                logd("Firebase 그룹 조회 에러: ${error.message}")
                close(error)
                return@addSnapshotListener
            }
            
            if (groupSnapshot?.exists() == true) {
                logd("Firebase 그룹 문서 발견")
                currentGroupData = groupSnapshot.data
                
                val groupName = groupSnapshot.getString("groupName") ?: ""
                val memberIds = groupSnapshot.get("memberIds") as? List<String> ?: emptyList()
                
                logd("그룹 정보 업데이트 - 이름: '$groupName', 멤버 ID들: $memberIds")
                
                // 멤버 정보 조회
                if (memberIds.isNotEmpty()) {
                    firestore.collection("users")
                        .whereIn("userId", memberIds)
                        .addSnapshotListener { usersSnapshot, usersError ->
                            if (usersError != null) {
                                logd("사용자 정보 조회 에러: ${usersError.message}")
                                currentMembers = emptyList()
                                combineAndSendData()
                                return@addSnapshotListener
                            }
                            
                            currentMembers = usersSnapshot?.documents?.mapNotNull { userDoc ->
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
                            
                            logd("멤버 정보 업데이트: ${currentMembers.size}명")
                            combineAndSendData()
                        }
                } else {
                    currentMembers = emptyList()
                    combineAndSendData()
                }
            } else {
                logd("Firebase 그룹 문서가 존재하지 않음: $groupId")
                trySend(null)
            }
        }
        
        // 2. 카트 아이템 리스너
        val cartItemsListener = groupRef.collection(CART_ITEMS_SUBCOLLECTION)
            .orderBy("addedAt")
            .addSnapshotListener { cartSnapshot, error ->
                if (error != null) {
                    logd("Firebase 카트 아이템 조회 에러: ${error.message}")
                    currentCartItems = emptyList()
                    combineAndSendData()
                    return@addSnapshotListener
                }
                
                currentCartItems = cartSnapshot?.documents?.mapNotNull { itemDoc ->
                    try {
                        CartItemEntity(
                            id = itemDoc.id,
                            name = itemDoc.getString("name") ?: "",
                            price = itemDoc.getLong("price") ?: 0L,
                            quantity = itemDoc.getLong("quantity")?.toInt() ?: 1,
                            addedBy = itemDoc.getString("addedBy") ?: "익명",
                            addedAt = itemDoc.getLong("addedAt") ?: 0L,
                            isCompleted = itemDoc.getBoolean("isCompleted") ?: false
                        )
                    } catch (e: Exception) {
                        logd("카트 아이템 파싱 에러: ${e.message}")
                        null
                    }
                } ?: emptyList()
                
                logd("카트 아이템 업데이트: ${currentCartItems.size}개")
                currentCartItems.forEach { item ->
                    logd("아이템: ${item.name} - ${item.price}원 x ${item.quantity}개 (완료: ${item.isCompleted})")
                }
                
                combineAndSendData()
            }
        
        awaitClose { 
            groupListener.remove()
            cartItemsListener.remove()
            logd("Firebase 리스너 해제: $groupId")
        }
    }

    /**
     * 카트 아이템 상태 업데이트
     */
    suspend fun updateCartItemStatus(groupId: String, itemId: String, isCompleted: Boolean): Boolean {
        return try {
            // 1. 카트 아이템 상태 업데이트
            firestore.collection(GROUPS_COLLECTION)
                .document(groupId)
                .collection(CART_ITEMS_SUBCOLLECTION)
                .document(itemId)
                .update("isCompleted", isCompleted)
                .await()
            
            // 2. 그룹 진행률 업데이트
            updateGroupProgress(groupId)
            
            true
        } catch (e: Exception) {
            logd("카트 아이템 상태 업데이트 실패: ${e.message}")
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
            
            // 진행률 업데이트 (새 아이템이 추가되어 전체 아이템 수가 변경됨)
            updateGroupProgress(groupId)
            
            docRef.id
        } catch (e: Exception) {
            logd("카트 아이템 추가 실패: ${e.message}")
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
            
            // 진행률 업데이트 (아이템이 삭제되어 전체 아이템 수가 변경됨)
            updateGroupProgress(groupId)
            
            true
        } catch (e: Exception) {
            logd("카트 아이템 삭제 실패: ${e.message}")
            false
        }
    }
    
    /**
     * 그룹 진행률 업데이트 (공통 함수)
     */
    private suspend fun updateGroupProgress(groupId: String) {
        try {
            // 전체 카트 아이템을 조회하여 진행률 계산
            val cartItemsSnapshot = firestore.collection(GROUPS_COLLECTION)
                .document(groupId)
                .collection(CART_ITEMS_SUBCOLLECTION)
                .get()
                .await()
            
            val cartItems = cartItemsSnapshot.documents.mapNotNull { itemDoc ->
                try {
                    CartItemEntity(
                        id = itemDoc.id,
                        name = itemDoc.getString("name") ?: "",
                        price = itemDoc.getLong("price") ?: 0L,
                        quantity = itemDoc.getLong("quantity")?.toInt() ?: 1,
                        addedBy = itemDoc.getString("addedBy") ?: "익명",
                        addedAt = itemDoc.getLong("addedAt") ?: 0L,
                        isCompleted = itemDoc.getBoolean("isCompleted") ?: false
                    )
                } catch (e: Exception) {
                    logd("카트 아이템 파싱 에러: ${e.message}")
                    null
                }
            }
            
            val progressPercentage = if (cartItems.isNotEmpty()) {
                val completedItems = cartItems.count { it.isCompleted }
                val calculatedProgress = ((completedItems.toDouble() / cartItems.size) * 100).toInt()
                calculatedProgress.coerceIn(0, 100) // 0-100 범위로 제한
            } else {
                0
            }
            
            logd("그룹 진행률 업데이트: ${cartItems.count { it.isCompleted }}개 완료 / ${cartItems.size}개 전체 = ${progressPercentage}%")
            
            // 그룹 문서의 진행률과 updatedAt 업데이트 (실시간 리스너 트리거)
            firestore.collection(GROUPS_COLLECTION)
                .document(groupId)
                .update(
                    mapOf(
                        "progressPercentage" to progressPercentage,
                        "updatedAt" to com.google.firebase.Timestamp.now()
                    )
                )
                .await()
            
            logd("Firebase 진행률 업데이트 완료: ${progressPercentage}%")
        } catch (e: Exception) {
            logd("그룹 진행률 업데이트 실패: ${e.message}")
        }
    }
} 