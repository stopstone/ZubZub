package com.cyberwarriers.zubzub.feature.group_enter.data.mapper

import com.cyberwarriers.zubzub.feature.group_enter.data.model.GroupFirebaseEntity
import com.cyberwarriers.zubzub.feature.group_enter.domain.model.GroupInfo

/**
 * Firebase 데이터 모델을 도메인 모델로 변환
 */
fun GroupFirebaseEntity.toDomain(currentUserId: String): GroupInfo {
    return GroupInfo(
        groupId = groupId,
        groupName = groupName,
        description = description,
        memberCount = memberIds.size,
        createdBy = createdBy,
        createdAt = createdAt?.seconds ?: 0L,
        targetAmount = targetAmount,
        isAlreadyMember = memberIds.contains(currentUserId)
    )
} 