package com.cyberwarriers.zubzub.feature.auth.data.mapper

import com.cyberwarriers.zubzub.core.domain.model.User
import com.cyberwarriers.zubzub.feature.auth.data.model.UserFirebaseEntity

/**
 * 사용자 데이터 매퍼
 * 
 * Firebase 엔티티와 도메인 모델 간의 변환을 담당
 */
object UserMapper {
    
    /**
     * Firebase 엔티티를 도메인 모델로 변환
     */
    fun UserFirebaseEntity.toDomain(): User {
        return User(
            userId = userId,
            email = email,
            displayName = displayName,
            profileImageUrl = profileImageUrl,
            provider = provider,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isActive = isActive,
            hasProfile = hasProfile,
        )
    }
    
    /**
     * 도메인 모델을 Firebase 엔티티로 변환
     */
    fun User.toFirebaseEntity(): UserFirebaseEntity {
        return UserFirebaseEntity(
            userId = userId,
            email = email,
            displayName = displayName,
            profileImageUrl = profileImageUrl,
            provider = provider,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isActive = isActive,
            hasProfile = hasProfile,
        )
    }
}
