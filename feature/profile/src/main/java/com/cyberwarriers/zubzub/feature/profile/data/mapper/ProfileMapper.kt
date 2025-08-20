package com.cyberwarriers.zubzub.feature.profile.data.mapper

import com.cyberwarriers.zubzub.feature.profile.data.model.ProfileFirebaseEntity
import com.cyberwarriers.zubzub.feature.profile.domain.model.Profile

/**
 * 프로필 데이터 매퍼
 * 
 * Firebase 엔티티와 도메인 모델 간의 변환을 담당
 */
object ProfileMapper {
    
    /**
     * Firebase 엔티티를 도메인 모델로 변환
     */
    fun ProfileFirebaseEntity.toDomain(): Profile {
        return Profile(
            profileId = profileId,
            userId = userId,
            profileName = profileName,
            profileImageUrl = profileImageUrl,
            isDefault = isDefault,
            isActive = isActive,
            createdAt = createdAt?.seconds ?: 0L,
            updatedAt = updatedAt?.seconds ?: 0L,
        )
    }
    
    /**
     * 도메인 모델을 Firebase 엔티티로 변환
     */
    fun Profile.toFirebaseEntity(): ProfileFirebaseEntity {
        return ProfileFirebaseEntity(
            profileId = profileId,
            userId = userId,
            profileName = profileName,
            profileImageUrl = profileImageUrl,
            isDefault = isDefault,
            isActive = isActive,
        )
    }
}
