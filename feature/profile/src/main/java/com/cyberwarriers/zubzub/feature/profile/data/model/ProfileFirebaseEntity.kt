package com.cyberwarriers.zubzub.feature.profile.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

/**
 * Firebase Firestore 프로필 엔티티
 * 
 * 사용자 커스텀 프로필 정보를 Firestore에 저장하기 위한 엔티티
 */
@Keep
data class ProfileFirebaseEntity(
    @DocumentId
    val profileId: String = "", // 프로필 고유 식별자 (문서 ID)
    val userId: String = "", // 소유자 사용자 ID
    val profileName: String = "", // 사용자 설정 프로필 이름
    val profileImageUrl: String = "", // 사용자 업로드 프로필 이미지 URL
    val isDefault: Boolean = false, // 기본 프로필 여부
    val isActive: Boolean = true, // 활성 상태
    
    @ServerTimestamp
    val createdAt: Timestamp? = null, // 프로필 생성 시간
    
    @ServerTimestamp
    val updatedAt: Timestamp? = null, // 마지막 업데이트 시간
)
