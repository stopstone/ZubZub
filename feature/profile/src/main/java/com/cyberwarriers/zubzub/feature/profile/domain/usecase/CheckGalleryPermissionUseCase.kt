package com.cyberwarriers.zubzub.feature.profile.domain.usecase

import com.cyberwarriers.zubzub.feature.profile.domain.repository.GalleryRepository
import javax.inject.Inject

/**
 * 갤러리 권한을 확인하는 UseCase
 * 
 * 주요 기능:
 * - 갤러리 접근 권한 상태 확인
 * - 권한 관련 비즈니스 로직 처리
 */
class CheckGalleryPermissionUseCase @Inject constructor(
    private val galleryRepository: GalleryRepository
) {
    
    /**
     * 갤러리 접근 권한을 확인합니다.
     * 
     * @return 권한 상태 (true: 권한 있음, false: 권한 없음)
     */
    suspend operator fun invoke(): Result<Boolean> {
        return try {
            val hasPermission = galleryRepository.checkGalleryPermission()
            Result.success(hasPermission)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
