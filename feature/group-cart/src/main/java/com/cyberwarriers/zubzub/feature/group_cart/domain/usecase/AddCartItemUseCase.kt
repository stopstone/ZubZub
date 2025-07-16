package com.cyberwarriers.zubzub.feature.group_cart.domain.usecase

import com.cyberwarriers.zubzub.feature.group_cart.domain.repository.GroupCartRepository
import javax.inject.Inject

/**
 * 카트 아이템 추가 UseCase
 */
class AddCartItemUseCase @Inject constructor(
    private val repository: GroupCartRepository
) {
    suspend operator fun invoke(
        groupId: String,
        name: String,
        price: Long,
        quantity: Int
    ): Result<String> {
        return repository.addCartItem(groupId, name, price, quantity)
    }
} 