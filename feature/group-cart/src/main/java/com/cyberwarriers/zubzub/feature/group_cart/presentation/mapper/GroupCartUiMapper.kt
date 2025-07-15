package com.cyberwarriers.zubzub.feature.group_cart.presentation.mapper

import com.cyberwarriers.zubzub.feature.group_cart.domain.model.CartItem as DomainCartItem
import com.cyberwarriers.zubzub.feature.group_cart.domain.model.GroupMember as DomainGroupMember
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.CartItem as PresentationCartItem
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.Member as PresentationMember

/**
 * Domain Model → Presentation Model 변환 매퍼
 */

/**
 * Domain CartItem → Presentation CartItem 변환
 */
fun DomainCartItem.toPresentation(): PresentationCartItem {
    return PresentationCartItem(
        id = id,
        name = name,
        price = price.toInt(), // Long → Int 변환
        quantity = quantity,
        addedBy = addedBy,
        isCompleted = isCompleted
    )
}

/**
 * Domain GroupMember → Presentation Member 변환
 */
fun DomainGroupMember.toPresentation(): PresentationMember {
    return PresentationMember(
        id = id,
        name = name,
        email = email,
        isGroupLeader = isGroupLeader,
        joinDate = formatTimestamp(joinedAt),
        totalContribution = totalContribution.toInt(), // Long → Int 변환
        isSelected = false // 기본값
    )
}

/**
 * 타임스탬프를 날짜 문자열로 변환
 */
private fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy.MM.dd", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
} 