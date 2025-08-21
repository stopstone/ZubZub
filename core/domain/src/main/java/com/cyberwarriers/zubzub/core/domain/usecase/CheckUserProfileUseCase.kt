package com.cyberwarriers.zubzub.core.domain.usecase

import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 사용자 프로필 존재 여부를 확인하는 UseCase
 * 
 * 주요 기능:
 * - 현재 로그인된 사용자의 프로필 존재 여부 확인
 * - 프로필이 있는 경우 기본 프로필 설정 확인 및 처리
 */
class CheckUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    
    /**
     * 사용자 프로필 존재 여부를 확인하고 기본 프로필을 설정합니다.
     * 
     * @return 프로필 존재 여부
     */
    suspend operator fun invoke(): Result<Boolean> {
        return authRepository.hasUserProfile()
            .onSuccess { hasProfile ->
                if (hasProfile) {
                    // 프로필이 있으면 기본 프로필 설정 확인
                    authRepository.ensurePrimaryProfileExists()
                }
            }
    }
}
