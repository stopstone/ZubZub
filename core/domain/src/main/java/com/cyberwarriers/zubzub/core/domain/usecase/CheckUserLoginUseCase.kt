package com.cyberwarriers.zubzub.core.domain.usecase

import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * 사용자 로그인 상태를 확인하는 UseCase
 * 
 * 주요 기능:
 * - 현재 사용자의 로그인 상태 확인
 * - Firebase Auth를 통한 인증 상태 검증
 */
class CheckUserLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    
    /**
     * 사용자 로그인 상태를 확인합니다.
     * 
     * @return 로그인 상태 (true: 로그인됨, false: 로그아웃됨)
     */
    operator fun invoke(): Result<Boolean> {
        return try {
            val isLoggedIn = authRepository.isUserLoggedIn()
            Result.success(isLoggedIn)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
