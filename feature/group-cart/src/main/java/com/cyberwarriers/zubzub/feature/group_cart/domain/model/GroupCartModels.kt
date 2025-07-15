package com.cyberwarriers.zubzub.feature.group_cart.domain.model

/**
 * 그룹 카트 상세 정보 (Domain Model)
 */
data class GroupCartDetail(
    val groupId: String,
    val groupName: String,
    val memberCount: Int,
    val cartItems: List<CartItem>,
    val members: List<GroupMember>
)

/**
 * 카트 아이템 (Domain Model)
 */
data class CartItem(
    val id: String,
    val name: String,
    val price: Long,
    val quantity: Int,
    val addedBy: String,
    val addedAt: Long,
    val isCompleted: Boolean = false
)

/**
 * 그룹 멤버 (Domain Model)
 */
data class GroupMember(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String,
    val isGroupLeader: Boolean,
    val joinedAt: Long,
    val totalContribution: Long,
    val isActive: Boolean
) 