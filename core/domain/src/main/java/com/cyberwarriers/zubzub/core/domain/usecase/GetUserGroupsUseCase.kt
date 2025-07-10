package com.cyberwarriers.zubzub.core.domain.usecase

import com.cyberwarriers.zubzub.core.domain.model.CartGroupSummary
import com.cyberwarriers.zubzub.core.domain.repository.CartGroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Domain Layer - UseCase
 * 
 * 사용자가 속한 그룹 목록 조회 비즈니스 로직
 */
class GetUserGroupsUseCase @Inject constructor(
    private val cartGroupRepository: CartGroupRepository
) {

    /**
     * 사용자가 속한 그룹 목록 조회
     * 
     * @return 그룹 요약 정보 Flow (실시간 업데이트)
     */
    operator fun invoke(): Flow<List<CartGroupSummary>> {
        return cartGroupRepository.getUserGroups()
    }
} 