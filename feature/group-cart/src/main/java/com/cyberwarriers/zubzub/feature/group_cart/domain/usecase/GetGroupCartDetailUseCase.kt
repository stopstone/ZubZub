package com.cyberwarriers.zubzub.feature.group_cart.domain.usecase

import com.cyberwarriers.zubzub.feature.group_cart.domain.model.GroupCartDetail
import com.cyberwarriers.zubzub.feature.group_cart.domain.repository.GroupCartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 그룹 카트 상세 정보 조회 UseCase
 */
class GetGroupCartDetailUseCase @Inject constructor(
    private val repository: GroupCartRepository
) {
    operator fun invoke(groupId: String): Flow<GroupCartDetail> {
        return repository.getGroupCartDetail(groupId)
    }
} 