package com.cyberwarriers.zubzub.feature.group_cart.data.mapper

import com.cyberwarriers.zubzub.feature.group_cart.data.model.CartItemEntity
import com.cyberwarriers.zubzub.feature.group_cart.data.model.GroupCartDetailEntity
import com.cyberwarriers.zubzub.feature.group_cart.data.model.GroupMemberEntity
import com.cyberwarriers.zubzub.feature.group_cart.domain.model.CartItem
import com.cyberwarriers.zubzub.feature.group_cart.domain.model.GroupCartDetail
import com.cyberwarriers.zubzub.feature.group_cart.domain.model.GroupMember

/**
 * Data Entity → Domain Model 변환 매퍼
 */

/**
 * CartItemEntity → CartItem 변환
 */
fun CartItemEntity.toDomain(): CartItem {
    return CartItem(
        id = id,
        name = name,
        price = price,
        quantity = quantity,
        addedBy = addedBy,
        addedAt = addedAt,
        isCompleted = isCompleted
    )
}

/**
 * GroupMemberEntity → GroupMember 변환
 */
fun GroupMemberEntity.toDomain(): GroupMember {
    return GroupMember(
        id = id,
        name = name,
        email = email,
        profileImageUrl = profileImageUrl,
        isGroupLeader = isGroupLeader,
        joinedAt = joinedAt,
        totalContribution = totalContribution,
        isActive = isActive
    )
}

/**
 * GroupCartDetailEntity → GroupCartDetail 변환
 */
fun GroupCartDetailEntity.toDomain(): GroupCartDetail {
    return GroupCartDetail(
        groupId = groupId,
        groupName = groupName,
        memberCount = memberCount,
        cartItems = cartItems.map { it.toDomain() },
        members = members.map { it.toDomain() }
    )
} 