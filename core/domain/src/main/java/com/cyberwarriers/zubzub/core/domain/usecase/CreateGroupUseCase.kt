package com.cyberwarriers.zubzub.core.domain.usecase

import com.cyberwarriers.zubzub.core.domain.repository.CartGroupRepository
import javax.inject.Inject

/**
 * Domain Layer - UseCase
 * 
 * 그룹 생성 비즈니스 로직을 담당
 * Repository 인터페이스에만 의존
 */
class CreateGroupUseCase @Inject constructor(
    private val cartGroupRepository: CartGroupRepository
) {

    /**
     * 그룹 생성 실행
     * 
     * @param groupName 그룹 이름 (필수)
     * @param description 그룹 설명 (선택)
     * @param targetAmount 목표 금액 (선택, 기본값 0)
     * @return 생성된 그룹 ID 또는 에러
     */
    suspend operator fun invoke(
        groupName: String,
        description: String = "",
        targetAmount: Long = 0L
    ): Result<String> {
        // 비즈니스 로직 검증
        if (groupName.isBlank()) {
            return Result.failure(IllegalArgumentException("그룹 이름은 필수입니다."))
        }
        
        if (groupName.length < 2) {
            return Result.failure(IllegalArgumentException("그룹 이름은 2글자 이상이어야 합니다."))
        }
        
        if (groupName.length > 20) {
            return Result.failure(IllegalArgumentException("그룹 이름은 20글자 이하여야 합니다."))
        }
        
        if (targetAmount < 0) {
            return Result.failure(IllegalArgumentException("목표 금액은 0 이상이어야 합니다."))
        }
        
        // Repository를 통해 실제 생성 수행
        return cartGroupRepository.createGroup(
            groupName = groupName.trim(),
            description = description.trim(),
            targetAmount = targetAmount
        )
    }
} 