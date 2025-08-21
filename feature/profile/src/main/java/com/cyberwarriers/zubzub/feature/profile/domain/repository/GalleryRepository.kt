package com.cyberwarriers.zubzub.feature.profile.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * 갤러리 이미지 관리를 위한 Repository 인터페이스
 * 
 * Clean Architecture 원칙에 따라 Domain 계층에서 정의
 * 프로필 이미지 선택을 위한 갤러리 기능을 추상화
 */
interface GalleryRepository {
    
    /**
     * 프로필용 갤러리 이미지를 페이징으로 제공합니다.
     * 
     * 특징:
     * - 최신순 정렬
     * - 프로필 이미지로 적합한 크기 필터링
     * - 메모리 효율적인 페이징 처리
     * 
     * @return 페이징된 이미지 URI 플로우
     */
    fun getProfileGalleryImages(): Flow<PagingData<String>>
    
    /**
     * 갤러리 권한 상태를 확인합니다.
     * 
     * @return 권한 허용 여부
     */
    suspend fun checkGalleryPermission(): Boolean
}
