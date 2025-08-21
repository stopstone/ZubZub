package com.cyberwarriers.zubzub.core.domain.usecase

import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * DataStore에서 로그인 상태를 확인하는 UseCase
 * 
 * 주요 기능:
 * - DataStore에 저장된 로그인 상태 확인
 * - 앱 재시작 시 빠른 로그인 상태 판단
 */
class CheckLoginFromDataStoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    
    /**
     * DataStore에서 로그인 상태를 확인합니다.
     * 
     * @return 로그인 상태 Flow
     */
    operator fun invoke(): Flow<Boolean> {
        return authRepository.isUserLoggedInFromDataStore()
    }
}
