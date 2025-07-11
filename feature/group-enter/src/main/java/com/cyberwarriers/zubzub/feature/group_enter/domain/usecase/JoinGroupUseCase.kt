package com.cyberwarriers.zubzub.feature.group_enter.domain.usecase

import com.cyberwarriers.zubzub.feature.group_enter.domain.repository.GroupEnterRepository
import javax.inject.Inject

/**
 * 그룹 참여 UseCase
 */
class JoinGroupUseCase @Inject constructor(
    private val repository: GroupEnterRepository
) {
    
    suspend operator fun invoke(groupId: String): Result<Unit> {
        if (groupId.isBlank()) {
            return Result.failure(IllegalArgumentException("그룹 ID가 필요합니다."))
        }
        
        return repository.joinGroup(groupId)
    }
} 