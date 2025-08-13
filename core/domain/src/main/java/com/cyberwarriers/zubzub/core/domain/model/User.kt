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
    val isActive: Boolean
)
