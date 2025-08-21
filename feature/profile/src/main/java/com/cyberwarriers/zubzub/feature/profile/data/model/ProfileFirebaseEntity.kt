package com.cyberwarriers.zubzub.feature.profile.data.model

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

/**
 * Firebase Firestore 프로필 엔티티
 * 
 * 사용자 커스텀 프로필 정보를 Firestore에 저장하기 위한 엔티티
 */
@Keep
data class ProfileFirebaseEntity(
    @DocumentId
    val profileId: String = "", // 프로필 고유 식별자 (문서 ID)
    
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String = "", // 소유자 사용자 ID
    
    @get:PropertyName("profile_name")
    @set:PropertyName("profile_name")
    var profileName: String = "", // 사용자 설정 프로필 이름
    
    @get:PropertyName("profile_image_url")
    @set:PropertyName("profile_image_url")
    var profileImageUrl: String = "", // 사용자 업로드 프로필 이미지 URL
    
    @get:PropertyName("is_default")
    @set:PropertyName("is_default")
    var isDefault: Boolean = false, // 기본 프로필 여부
    
    @get:PropertyName("is_active")
    @set:PropertyName("is_active")
    var isActive: Boolean = true, // 활성 상태
    
    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt: Long = System.currentTimeMillis(), // 프로필 생성 시간
    
    @get:PropertyName("updated_at")
    @set:PropertyName("updated_at")
    var updatedAt: Long = System.currentTimeMillis(), // 마지막 업데이트 시간
)
