package com.cyberwarriers.zubzub.core.domain.model

/**
 * 사용자 정보를 나타내는 도메인 모델
 * 
 * Clean Architecture 원칙에 따라 도메인 계층에서 정의
 */
data class User(
    val userId: String,
    val email: String,
    val displayName: String,
    val profileImageUrl: String,
    val provider: String,
    val createdAt: Long, // Timestamp를 Long으로 변경
    val updatedAt: Long, // Timestamp를 Long으로 변경
    val isActive: Boolean,
    val hasProfile: Boolean = false, // 프로필 생성 여부
    val profileName: String = "", // 사용자가 설정한 프로필 이름 (20글자 제한)
    val customProfileImageUrl: String = "" // 사용자가 업로드한 프로필 이미지
)
