package com.cyberwarriers.zubzub.core.domain.model

/**
 * 사용자 정보를 나타내는 도메인 모델
 * 
 * Clean Architecture 원칙에 따라 도메인 계층에서 정의
 * 인증 및 기본 사용자 정보만 포함 (프로필 정보는 Profile 모델에서 분리)
 */
data class User(
    val userId: String,
    val email: String,
    val displayName: String, // Google에서 제공하는 표시명
    val profileImageUrl: String, // Google에서 제공하는 프로필 이미지 URL
    val provider: String, // 로그인 제공자 (google, facebook 등)
    val createdAt: Long, // 계정 생성 시간
    val updatedAt: Long, // 마지막 업데이트 시간
    val isActive: Boolean, // 계정 활성화 여부
    val hasProfile: Boolean = false, // 기본 프로필 존재 여부
)
