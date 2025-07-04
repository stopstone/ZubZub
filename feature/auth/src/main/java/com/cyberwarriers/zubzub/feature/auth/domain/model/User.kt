package com.cyberwarriers.zubzub.feature.auth.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

/**
 * 사용자 정보 데이터 클래스
 * Firestore에 저장되는 사용자 정보를 나타냄
 */
data class User(
    @DocumentId
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val profileImageUrl: String = "",
    val provider: String = "", // "google", "kakao" 등
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val isActive: Boolean = true
) {
    // Firestore에서 요구하는 기본 생성자
    constructor() : this(
        userId = "",
        email = "",
        displayName = "",
        profileImageUrl = "",
        provider = "",
        createdAt = Timestamp.now(),
        updatedAt = Timestamp.now(),
        isActive = true
    )
} 