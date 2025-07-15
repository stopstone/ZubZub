package com.cyberwarriers.zubzub.feature.group_cart.data.model

/**
 * 카트 아이템 데이터 모델 (Data Layer)
 */
data class CartItemEntity(
    val id: String,
    val name: String,
    val price: Long,
    val quantity: Int,
    val addedBy: String,
    val addedAt: Long,
    val isCompleted: Boolean = false,
)

/**
 * 그룹 멤버 데이터 모델 (Data Layer)
 */
data class GroupMemberEntity(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String,
    val isGroupLeader: Boolean,
    val joinedAt: Long,
    val totalContribution: Long,
    val isActive: Boolean,
)

/**
 * 그룹 카트 상세 데이터 모델 (Data Layer)
 */
data class GroupCartDetailEntity(
    val groupId: String,
    val groupName: String,
    val memberCount: Int,
    val cartItems: List<CartItemEntity>,
    val members: List<GroupMemberEntity>,
) 