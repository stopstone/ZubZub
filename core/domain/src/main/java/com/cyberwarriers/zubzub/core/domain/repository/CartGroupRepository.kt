package com.cyberwarriers.zubzub.core.domain.repository

import com.cyberwarriers.zubzub.core.domain.model.CartGroupSummary
import kotlinx.coroutines.flow.Flow

/**
 * Domain Layer - Repository Interface
 * 
 * 비즈니스 로직에서 필요한 데이터 연산을 정의
 * 구현체는 Data Layer에서 제공
 */
interface CartGroupRepository {

    /**
     * 새로운 그룹 생성
     * @param groupName 그룹 이름
     * @param description 그룹 설명
     * @param targetAmount 목표 금액
     * @return 생성된 그룹 ID
     */
    suspend fun createGroup(
        groupName: String,
        description: String = "",
        targetAmount: Long = 0L
    ): Result<String>

    /**
     * 사용자가 속한 그룹 목록 조회
     * @return 그룹 요약 정보 Flow
     */
    fun getUserGroups(): Flow<List<CartGroupSummary>>

    /**
     * 그룹 삭제
     * @param groupId 삭제할 그룹 ID
     */
    suspend fun deleteGroup(groupId: String): Result<Unit>

    /**
     * 그룹 정보 업데이트
     * @param groupId 그룹 ID
     * @param updates 업데이트할 정보
     */
    suspend fun updateGroup(
        groupId: String,
        updates: Map<String, Any>
    ): Result<Unit>
} 