package com.cyberwarriers.zubzub.feature.group_enter.data.model

import com.google.firebase.Timestamp

/**
 * Firebase의 cart_groups 컬렉션 데이터 모델
 */
data class GroupFirebaseEntity(
    val groupId: String = "",
    val groupName: String = "",
    val description: String = "",
    val createdBy: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val targetAmount: Long = 0L,
    val currentAmount: Long = 0L,
    val memberIds: List<String> = emptyList(),
    val isActive: Boolean = true
) 