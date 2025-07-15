package com.cyberwarriers.zubzub.feature.group_cart.data.datasource

import com.cyberwarriers.zubzub.feature.group_cart.data.model.CartItemEntity
import com.cyberwarriers.zubzub.feature.group_cart.data.model.GroupCartDetailEntity
import com.cyberwarriers.zubzub.feature.group_cart.data.model.GroupMemberEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 그룹 카트 데이터 소스 (더미 데이터 제공)
 */
@Singleton
class GroupCartDataSource @Inject constructor() {
    
    // 더미 데이터 저장소
    private val _groupCartData = MutableStateFlow(createDummyData())
    
    /**
     * 그룹 카트 상세 정보 조회
     */
    fun getGroupCartDetail(groupId: String): Flow<GroupCartDetailEntity?> {
        return _groupCartData.asStateFlow().map { dataMap ->
            dataMap[groupId]
        }
    }

    /**
     * 카트 아이템 상태 업데이트
     */
    suspend fun updateCartItemStatus(groupId: String, itemId: String, isCompleted: Boolean): Boolean {
        _groupCartData.update { currentData ->
            val groupDetail = currentData[groupId] ?: return false
            val updatedCartItems = groupDetail.cartItems.map { item ->
                if (item.id == itemId) {
                    item.copy(isCompleted = isCompleted)
                } else {
                    item
                }
            }
            currentData.toMutableMap().apply {
                put(groupId, groupDetail.copy(cartItems = updatedCartItems))
            }
        }
        return true
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
        delay(500) // 네트워크 지연 시뮬레이션

        val newItemId = "item_${System.currentTimeMillis()}"
        val newItem = CartItemEntity(
            id = newItemId,
            name = name,
            price = price,
            quantity = quantity,
            addedBy = addedBy,
            addedAt = System.currentTimeMillis(),
            isCompleted = false
        )
        
        _groupCartData.update { currentData ->
            val groupDetail = currentData[groupId] ?: return null
            val updatedCartItems = groupDetail.cartItems + newItem
            currentData.toMutableMap().apply {
                put(groupId, groupDetail.copy(cartItems = updatedCartItems))
            }
        }
        return newItemId
    }

    /**
     * 카트 아이템 삭제
     */
    suspend fun removeCartItem(groupId: String, itemId: String): Boolean {
        delay(300) // 네트워크 지연 시뮬레이션

        _groupCartData.update { currentData ->
            val groupDetail = currentData[groupId] ?: return false
            val updatedCartItems = groupDetail.cartItems.filter { it.id != itemId }
            currentData.toMutableMap().apply {
                put(groupId, groupDetail.copy(cartItems = updatedCartItems))
            }
        }
        return true
    }
    
    /**
     * 더미 데이터 생성
     */
    private fun createDummyData(): Map<String, GroupCartDetailEntity> {
        val currentTime = System.currentTimeMillis()
        
        return mapOf(
            "group1" to GroupCartDetailEntity(
                groupId = "group1",
                groupName = "우리 가족 장보기",
                memberCount = 4,
                cartItems = listOf(
                    CartItemEntity(
                        id = "item1",
                        name = "사과",
                        price = 5000L,
                        quantity = 6,
                        addedBy = "엄마",
                        addedAt = currentTime - 86400000, // 1일 전
                        isCompleted = false
                    ),
                    CartItemEntity(
                        id = "item2",
                        name = "바나나",
                        price = 3000L,
                        quantity = 1,
                        addedBy = "아빠",
                        addedAt = currentTime - 43200000, // 12시간 전
                        isCompleted = true
                    ),
                    CartItemEntity(
                        id = "item3",
                        name = "우유",
                        price = 4500L,
                        quantity = 2,
                        addedBy = "첫째",
                        addedAt = currentTime - 21600000, // 6시간 전
                        isCompleted = false
                    ),
                    CartItemEntity(
                        id = "item4",
                        name = "빵",
                        price = 2500L,
                        quantity = 3,
                        addedBy = "둘째",
                        addedAt = currentTime - 10800000, // 3시간 전
                        isCompleted = false
                    )
                ),
                members = listOf(
                    GroupMemberEntity(
                        id = "member1",
                        name = "엄마",
                        email = "mom@family.com",
                        profileImageUrl = "",
                        isGroupLeader = true,
                        joinedAt = currentTime - 2592000000, // 30일 전
                        totalContribution = 150000L,
                        isActive = true
                    ),
                    GroupMemberEntity(
                        id = "member2",
                        name = "아빠",
                        email = "dad@family.com",
                        profileImageUrl = "",
                        isGroupLeader = false,
                        joinedAt = currentTime - 2592000000, // 30일 전
                        totalContribution = 120000L,
                        isActive = true
                    ),
                    GroupMemberEntity(
                        id = "member3",
                        name = "첫째",
                        email = "first@family.com",
                        profileImageUrl = "",
                        isGroupLeader = false,
                        joinedAt = currentTime - 1296000000, // 15일 전
                        totalContribution = 50000L,
                        isActive = true
                    ),
                    GroupMemberEntity(
                        id = "member4",
                        name = "둘째",
                        email = "second@family.com",
                        profileImageUrl = "",
                        isGroupLeader = false,
                        joinedAt = currentTime - 604800000, // 7일 전
                        totalContribution = 25000L,
                        isActive = true
                    )
                )
            ),
            "group2" to GroupCartDetailEntity(
                groupId = "group2",
                groupName = "회사 동료들",
                memberCount = 3,
                cartItems = listOf(
                    CartItemEntity(
                        id = "item5",
                        name = "커피원두",
                        price = 25000L,
                        quantity = 1,
                        addedBy = "김철수",
                        addedAt = currentTime - 7200000, // 2시간 전
                        isCompleted = false
                    ),
                    CartItemEntity(
                        id = "item6",
                        name = "간식세트",
                        price = 15000L,
                        quantity = 2,
                        addedBy = "이영희",
                        addedAt = currentTime - 3600000, // 1시간 전
                        isCompleted = true
                    )
                ),
                members = listOf(
                    GroupMemberEntity(
                        id = "member5",
                        name = "김철수",
                        email = "kim@company.com",
                        profileImageUrl = "",
                        isGroupLeader = true,
                        joinedAt = currentTime - 1209600000, // 14일 전
                        totalContribution = 80000L,
                        isActive = true
                    ),
                    GroupMemberEntity(
                        id = "member6",
                        name = "이영희",
                        email = "lee@company.com",
                        profileImageUrl = "",
                        isGroupLeader = false,
                        joinedAt = currentTime - 864000000, // 10일 전
                        totalContribution = 60000L,
                        isActive = true
                    ),
                    GroupMemberEntity(
                        id = "member7",
                        name = "박민수",
                        email = "park@company.com",
                        profileImageUrl = "",
                        isGroupLeader = false,
                        joinedAt = currentTime - 432000000, // 5일 전
                        totalContribution = 30000L,
                        isActive = true
                    )
                )
            )
        )
    }
} 