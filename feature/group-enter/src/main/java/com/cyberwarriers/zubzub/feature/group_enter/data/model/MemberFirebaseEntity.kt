package com.cyberwarriers.zubzub.feature.group_enter.data.model

import com.google.firebase.Timestamp

/**
 * Firebase의 members 서브컬렉션 데이터 모델
 */
data class MemberFirebaseEntity(
    val userId: String = "",
    val nickname: String = "",
    val profileImage: String = "",
    val role: String = "MEMBER", // OWNER, ADMIN, MEMBER
    val joinedAt: Timestamp? = null,
    val isActive: Boolean = true
) 