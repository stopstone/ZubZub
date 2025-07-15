package com.cyberwarriers.zubzub.feature.group_cart.data.repository

import com.cyberwarriers.zubzub.feature.group_cart.data.datasource.GroupCartDataSource
import com.cyberwarriers.zubzub.feature.group_cart.data.mapper.toDomain
import com.cyberwarriers.zubzub.feature.group_cart.domain.model.GroupCartDetail
import com.cyberwarriers.zubzub.feature.group_cart.domain.repository.GroupCartRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 그룹 카트 Repository 구현체
 */
@Singleton
class GroupCartRepositoryImpl @Inject constructor(
    private val dataSource: GroupCartDataSource,
    private val auth: FirebaseAuth
) : GroupCartRepository {
    
    override fun getGroupCartDetail(groupId: String): Flow<GroupCartDetail> {
        return dataSource.getGroupCartDetail(groupId).map { entity ->
            entity?.toDomain() ?: throw IllegalArgumentException("그룹을 찾을 수 없습니다: $groupId")
        }
    }
    
    override suspend fun updateCartItemStatus(
        groupId: String,
        itemId: String,
        isCompleted: Boolean
    ): Result<Unit> {
        return try {
            val success = dataSource.updateCartItemStatus(groupId, itemId, isCompleted)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("아이템 상태 업데이트에 실패했습니다"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun addCartItem(
        groupId: String,
        name: String,
        price: Long,
        quantity: Int
    ): Result<String> {
        return try {
            val currentUser = auth.currentUser
            val addedBy = currentUser?.displayName ?: "익명"
            
            val itemId = dataSource.addCartItem(groupId, name, price, quantity, addedBy)
            if (itemId != null) {
                Result.success(itemId)
            } else {
                Result.failure(Exception("아이템 추가에 실패했습니다"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removeCartItem(groupId: String, itemId: String): Result<Unit> {
        return try {
            val success = dataSource.removeCartItem(groupId, itemId)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("아이템 삭제에 실패했습니다"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 