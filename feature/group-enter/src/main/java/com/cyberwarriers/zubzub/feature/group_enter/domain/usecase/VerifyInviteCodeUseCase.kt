package com.cyberwarriers.zubzub.feature.group_enter.domain.usecase

import com.cyberwarriers.zubzub.feature.group_enter.domain.model.GroupInfo
import com.cyberwarriers.zubzub.feature.group_enter.domain.repository.GroupEnterRepository
import javax.inject.Inject

/**
 * 초대 코드 검증 UseCase
 */
class VerifyInviteCodeUseCase @Inject constructor(
    private val repository: GroupEnterRepository
) {
    
    suspend operator fun invoke(inviteCode: String): Result<GroupInfo?> {
        // 비즈니스 로직 검증
        if (inviteCode.isBlank()) {
            return Result.failure(IllegalArgumentException("초대 코드를 입력해주세요."))
        }
        
        if (inviteCode.length < 6) {
            return Result.failure(IllegalArgumentException("올바른 초대 코드를 입력해주세요."))
        }
        
        // Repository를 통해 그룹 조회
        return repository.getGroupByInviteCode(inviteCode.trim())
    }
} 