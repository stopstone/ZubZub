package com.cyberwarriers.zubzub.feature.profile.domain.usecase

import androidx.paging.PagingData
import com.cyberwarriers.zubzub.feature.profile.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 갤러리 이미지를 페이징으로 가져오는 UseCase
 * 
 * 주요 기능:
 * - 프로필용 갤러리 이미지 페이징 데이터 제공
 * - 메모리 효율적인 이미지 로딩
 */
class GetGalleryImagesUseCase @Inject constructor(
    private val galleryRepository: GalleryRepository
) {
    
    /**
     * 프로필용 갤러리 이미지를 페이징으로 가져옵니다.
     * 
     * @return 페이징된 이미지 URI Flow
     */
    operator fun invoke(): Flow<PagingData<String>> {
        return galleryRepository.getProfileGalleryImages()
    }
}
