package com.cyberwarriers.zubzub.feature.profile.domain.usecase

import com.cyberwarriers.zubzub.feature.profile.domain.model.CreateProfileRequest
import com.cyberwarriers.zubzub.feature.profile.domain.model.Profile
import com.cyberwarriers.zubzub.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * 프로필 생성 UseCase
 * 
 * 프로필 이름 유효성 검사 및 프로필 생성 로직을 담당
 */
class CreateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    
    /**
     * 프로필을 생성합니다.
     * 
     * @param profileName 프로필 이름 (1-20글자)
     * @param profileImageUrl 프로필 이미지 URL (선택사항)
     * @return 생성된 프로필 정보
     */
    suspend operator fun invoke(
        profileName: String,
        profileImageUrl: String = ""
    ): Result<Profile> {
        return try {
            // 프로필 이름 유효성 검사
            when {
                profileName.isBlank() -> {
                    Result.failure(IllegalArgumentException("프로필 이름을 입력해주세요."))
                }
                profileName.length > 20 -> {
                    Result.failure(IllegalArgumentException("프로필 이름은 20글자 이하로 입력해주세요."))
                }
                else -> {
                    val request = CreateProfileRequest(
                        profileName = profileName.trim(),
                        profileImageUrl = profileImageUrl
                    )
                    profileRepository.createProfile(request)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
