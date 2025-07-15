package com.cyberwarriers.zubzub.feature.group_cart.domain.usecase

import com.cyberwarriers.zubzub.feature.group_cart.domain.repository.GroupCartRepository
import javax.inject.Inject

/**
 * 카트 아이템 완료 상태 변경 UseCase
 */
class UpdateCartItemStatusUseCase @Inject constructor(
    private val repository: GroupCartRepository
) {
    suspend operator fun invoke(
        groupId: String,
        itemId: String,
        isCompleted: Boolean,
    ): Result<Unit> {
        return repository.updateCartItemStatus(groupId, itemId, isCompleted)
    }
} 