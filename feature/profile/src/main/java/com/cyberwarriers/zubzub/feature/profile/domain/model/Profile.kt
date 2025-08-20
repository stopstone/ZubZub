package com.cyberwarriers.zubzub.feature.profile.domain.model

/**
 * 프로필 정보를 나타내는 도메인 모델
 * 
 * Clean Architecture 원칙에 따라 도메인 계층에서 정의
 */
data class Profile(
    val userId: String,
    val profileName: String,
    val profileImageUrl: String,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * 프로필 생성 요청 모델
 */
data class CreateProfileRequest(
    val profileName: String,
    val profileImageUrl: String = ""
)

/**
 * 프로필 업데이트 요청 모델
 */
data class UpdateProfileRequest(
    val profileName: String? = null,
    val profileImageUrl: String? = null
)
