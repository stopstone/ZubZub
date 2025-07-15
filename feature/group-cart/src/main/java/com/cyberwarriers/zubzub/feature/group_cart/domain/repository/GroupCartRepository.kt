package com.cyberwarriers.zubzub.feature.group_cart.domain.repository

import com.cyberwarriers.zubzub.feature.group_cart.domain.model.GroupCartDetail
import kotlinx.coroutines.flow.Flow

/**
 * 그룹 카트 Repository Interface
 */
interface GroupCartRepository {
    fun getGroupCartDetail(groupId: String): Flow<GroupCartDetail>
    suspend fun updateCartItemStatus(
        groupId: String,
        itemId: String,
        isCompleted: Boolean
    ): Result<Unit>
    suspend fun addCartItem(
        groupId: String,
        name: String,
        price: Long,
        quantity: Int
    ): Result<String>
    suspend fun removeCartItem(
        groupId: String,
        itemId: String
    ): Result<Unit>
} 