package com.cyberwarriers.zubzub.feature.profile.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.data.datasource.GalleryPagingSource
import com.cyberwarriers.zubzub.feature.profile.domain.repository.GalleryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 갤러리 Repository 구현체
 * 
 * Data 계층에서 Context 의존성을 안전하게 처리하고
 * 페이징 설정을 최적화하여 성능과 사용자 경험을 향상
 */
@Singleton
class GalleryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GalleryRepository {
    
    companion object {
        // 페이징 설정 상수
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 5
        private const val INITIAL_LOAD_SIZE = 40
        private const val MAX_SIZE = 200 // 메모리 보호를 위한 최대 캐시 크기
    }
    
    override fun getProfileGalleryImages(): Flow<PagingData<String>> {
        logd("갤러리 페이징 플로우 생성 시작")
        
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE,
                maxSize = MAX_SIZE,
                jumpThreshold = Int.MIN_VALUE,
            ),
            pagingSourceFactory = { 
                logd("새로운 GalleryPagingSource 생성")
                GalleryPagingSource(
                    context = context,
                    pageSize = PAGE_SIZE,
                )
            }
        ).flow
    }
    
    override suspend fun checkGalleryPermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        val isGranted = ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
        
        logd("갤러리 권한 상태: $isGranted")
        return isGranted
    }
}
