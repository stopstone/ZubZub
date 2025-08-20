package com.cyberwarriers.zubzub.feature.profile.domain.model

/**
 * 프로필 정보를 나타내는 도메인 모델
 * 
 * Clean Architecture 원칙에 따라 도메인 계층에서 정의
 * 사용자 커스텀 프로필 정보를 관리
 */
data class Profile(
    val profileId: String, // 프로필 고유 식별자
    val userId: String, // 소유자 사용자 ID
    val profileName: String, // 사용자 설정 프로필 이름
    val profileImageUrl: String, // 사용자 업로드 프로필 이미지 URL
    val isDefault: Boolean, // 기본 프로필 여부
    val isActive: Boolean, // 활성 상태
    val createdAt: Long, // 프로필 생성 시간
    val updatedAt: Long, // 마지막 업데이트 시간
)

/**
 * 프로필 생성 요청 모델
 */
data class CreateProfileRequest(
    val profileName: String,
    val profileImageUrl: String = "",
)

/**
 * 프로필 업데이트 요청 모델
 */
data class UpdateProfileRequest(
    val profileId: String,
    val profileName: String? = null,
    val profileImageUrl: String? = null,
)
