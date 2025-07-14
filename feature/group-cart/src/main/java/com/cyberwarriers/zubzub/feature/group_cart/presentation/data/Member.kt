package com.cyberwarriers.zubzub.feature.group_cart.presentation.data

/**
 * 멤버 데이터 클래스
 */
data class Member(
    val id: String,
    val name: String,
    val email: String,
    val isGroupLeader: Boolean = false,
    val joinDate: String,
    val totalContribution: Int = 0,
)