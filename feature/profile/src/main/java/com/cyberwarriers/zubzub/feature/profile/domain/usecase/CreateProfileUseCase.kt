package com.cyberwarriers.zubzub.feature.profile.domain.usecase

import com.cyberwarriers.zubzub.core.util.logd
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
     * @param profileImageUri 프로필 이미지 URI (content:// 형태, 선택사항)
     * @return 생성된 프로필 정보
     */
    suspend operator fun invoke(
        profileName: String,
        profileImageUri: String = ""
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
                    logd("프로필 생성 시작: $profileName, 이미지 URI: $profileImageUri")
                    
                    // 이미지가 있으면 Firebase Storage에 업로드
                    val uploadedImageUrl = if (profileImageUri.isNotEmpty() && profileImageUri.startsWith("content://")) {
                        logd("이미지 업로드 시작")
                        
                        profileRepository.uploadProfileImage(profileImageUri)
                            .onSuccess { url ->
                                logd("이미지 업로드 성공: $url")
                            }
                            .onFailure { error ->
                                logd("이미지 업로드 실패: ${error.message}")
                                // 이미지 업로드 실패 시에도 프로필 생성은 진행 (이미지 없이)
                            }
                            .getOrElse { "" }
                    } else {
                        profileImageUri
                    }
                    
                    logd("최종 이미지 URL: $uploadedImageUrl")
                    
                    // 프로필 생성 요청
                    val request = CreateProfileRequest(
                        profileName = profileName.trim(),
                        profileImageUrl = uploadedImageUrl,
                    )
                    profileRepository.createProfile(request)
                }
            }
        } catch (e: Exception) {
            logd("프로필 생성 중 오류: ${e.message}")
            Result.failure(e)
        }
    }
}
