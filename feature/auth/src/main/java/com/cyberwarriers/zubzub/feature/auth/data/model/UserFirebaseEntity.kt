package com.cyberwarriers.zubzub.feature.auth.data.model

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

/**
 * Firebase Firestore 사용자 엔티티
 * 
 * 사용자 정보를 Firestore에 저장하기 위한 엔티티
 * snake_case 필드명으로 저장하고 camelCase로 매핑
 */
@Keep
data class UserFirebaseEntity(
    @DocumentId
    val userId: String = "", // 사용자 고유 식별자 (문서 ID)
    
    @get:PropertyName("email")
    @set:PropertyName("email") 
    var email: String = "", // 이메일
    
    @get:PropertyName("display_name")
    @set:PropertyName("display_name")
    var displayName: String = "", // Google에서 제공하는 표시명
    
    @get:PropertyName("profile_image_url")
    @set:PropertyName("profile_image_url")
    var profileImageUrl: String = "", // Google에서 제공하는 프로필 이미지 URL
    
    @get:PropertyName("provider")
    @set:PropertyName("provider")
    var provider: String = "", // 로그인 제공자 (google, facebook 등)
    
    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt: Long = System.currentTimeMillis(), // 계정 생성 시간
    
    @get:PropertyName("updated_at")
    @set:PropertyName("updated_at")
    var updatedAt: Long = System.currentTimeMillis(), // 마지막 업데이트 시간
    
    @get:PropertyName("is_active")
    @set:PropertyName("is_active")
    var isActive: Boolean = true, // 계정 활성화 여부
    
    @get:PropertyName("has_profile")
    @set:PropertyName("has_profile")
    var hasProfile: Boolean = false, // 기본 프로필 존재 여부
)
