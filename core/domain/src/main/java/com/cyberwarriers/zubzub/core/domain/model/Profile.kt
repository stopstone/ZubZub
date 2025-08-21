package com.cyberwarriers.zubzub.core.domain.model

/**
 * 사용자 프로필 정보
 */
data class UserProfile(
    val profileId: String,
    val nickname: String,
    val profileImageUrl: String?,
    val currentGroupName: String?,
    val isActive: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * 프로필 생성 요청 데이터
 */
data class CreateProfileRequest(
    val nickname: String,
    val profileImageUrl: String? = null
)

/**
 * 프로필 업데이트 요청 데이터
 */
data class UpdateProfileRequest(
    val profileId: String,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val currentGroupName: String? = null
)
