package com.cyberwarriers.zubzub.core.domain.usecase

import com.cyberwarriers.zubzub.core.domain.model.UserProfile
import com.cyberwarriers.zubzub.core.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Domain Layer - UseCase
 * 
 * 현재 사용자의 모든 프로필 조회 비즈니스 로직
 */
class GetUserProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    /**
     * 현재 사용자의 모든 프로필 조회
     * 
     * @return 프로필 목록 Flow (실시간 업데이트)
     */
    operator fun invoke(): Flow<List<UserProfile>> {
        return profileRepository.getAllProfilesFlow()
            .map { profiles ->
                profiles.map { profile ->
                    UserProfile(
                        profileId = profile.profileId,
                        nickname = profile.nickname,
                        profileImageUrl = profile.profileImageUrl,
                        currentGroupName = profile.currentGroupName,
                        isActive = profile.isActive,
                        createdAt = profile.createdAt,
                        updatedAt = profile.updatedAt
                    )
                }
            }
    }
}
