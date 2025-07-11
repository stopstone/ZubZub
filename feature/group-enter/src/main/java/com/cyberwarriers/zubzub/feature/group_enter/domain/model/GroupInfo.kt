package com.cyberwarriers.zubzub.feature.group_enter.domain.model

/**
 * 그룹 정보 도메인 모델
 */
data class GroupInfo(
    val groupId: String,
    val groupName: String,
    val description: String,
    val memberCount: Int,
    val createdBy: String,
    val createdAt: Long,
    val targetAmount: Long,
    val isAlreadyMember: Boolean
) 