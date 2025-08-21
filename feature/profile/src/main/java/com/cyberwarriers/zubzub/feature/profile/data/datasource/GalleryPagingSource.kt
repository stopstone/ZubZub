package com.cyberwarriers.zubzub.feature.profile.data.datasource

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cyberwarriers.zubzub.core.util.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 프로필 이미지 선택을 위한 갤러리 페이징 데이터소스
 * 
 * 성능 최적화와 메모리 효율성을 고려한 구현:
 * - IO 디스패처에서 데이터베이스 쿼리 실행
 * - 프로필 이미지로 적합한 필터링 조건 적용
 * - 에러 처리 및 로깅 강화
 * 
 * @param context Android ApplicationContext (메모리 누수 방지)
 * @param pageSize 한 페이지당 로드할 이미지 수
 */
class GalleryPagingSource(
    private val context: Context,
    private val pageSize: Int = 20
) : PagingSource<Int, String>() {
    
    companion object {
        // 프로필 이미지 품질 기준
        private const val MIN_FILE_SIZE = 51200L // 50KB
        private const val MIN_WIDTH = 200 // 200px
        private const val MIN_HEIGHT = 200 // 200px
        
        // MediaStore 프로젝션
        private val PROJECTION = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.DISPLAY_NAME
        )
    }
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return withContext(Dispatchers.IO) {
            try {
                val currentPage = params.key ?: 0
                val offset = currentPage * pageSize
                
                logd("갤러리 이미지 로드 시작 - 페이지: $currentPage, 오프셋: $offset, 크기: ${params.loadSize}")
                
                val images = loadGalleryImagesWithOptimization(
                    offset = offset,
                    limit = params.loadSize
                )
                
                logd("갤러리 이미지 로드 완료 - ${images.size}개 로드됨")
                
                LoadResult.Page(
                    data = images,
                    prevKey = if (currentPage == 0) null else currentPage - 1,
                    nextKey = if (images.size < params.loadSize) null else currentPage + 1
                )
            } catch (exception: Exception) {
                logd("갤러리 이미지 로드 실패: ${exception.message}")
                LoadResult.Error(exception)
            }
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        // 가장 최근에 접근한 페이지 근처에서 새로고침
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    
    /**
     * 최적화된 갤러리 이미지 로드 함수
     * 
     * 프로필 이미지로 적합한 조건:
     * - 파일 크기: 50KB 이상
     * - 해상도: 200x200px 이상
     * - 최신순 정렬
     * 
     * Android API 30+ 에서는 Bundle을 사용한 페이징,
     * 그 이하에서는 전통적인 cursor 기반 페이징 사용
     */
    private fun loadGalleryImagesWithOptimization(
        offset: Int,
        limit: Int
    ): List<String> {
        val images = mutableListOf<String>()
        var cursor: Cursor? = null
        
        try {
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            
            // 프로필 이미지로 적합한 필터링 조건
            val selection = buildString {
                append("${MediaStore.Images.Media.SIZE} >= ?")
                append(" AND ${MediaStore.Images.Media.WIDTH} >= ?")
                append(" AND ${MediaStore.Images.Media.HEIGHT} >= ?")
                // 손상된 이미지 제외
                append(" AND ${MediaStore.Images.Media.SIZE} IS NOT NULL")
                append(" AND ${MediaStore.Images.Media.WIDTH} IS NOT NULL")
                append(" AND ${MediaStore.Images.Media.HEIGHT} IS NOT NULL")
            }
            
            val selectionArgs = arrayOf(
                MIN_FILE_SIZE.toString(),
                MIN_WIDTH.toString(),
                MIN_HEIGHT.toString()
            )
            
            // Android API 30 이상에서는 Bundle을 사용한 페이징
            cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val queryArgs = Bundle().apply {
                    putString(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                    putStringArray(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
                    putString(android.content.ContentResolver.QUERY_ARG_SQL_SORT_ORDER, "${MediaStore.Images.Media.DATE_MODIFIED} DESC")
                    putInt(android.content.ContentResolver.QUERY_ARG_LIMIT, limit)
                    putInt(android.content.ContentResolver.QUERY_ARG_OFFSET, offset)
                }
                
                context.contentResolver.query(
                    uri,
                    PROJECTION,
                    queryArgs,
                    null
                )
            } else {
                // Android API 29 이하에서는 전통적인 방식 사용
                // sortOrder에서 LIMIT과 OFFSET 제거
                val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
                
                context.contentResolver.query(
                    uri,
                    PROJECTION,
                    selection,
                    selectionArgs,
                    sortOrder
                )
            }
            
            cursor?.let { c ->
                val idColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                
                // API 29 이하에서는 수동으로 페이징 처리
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    // offset만큼 건너뛰기
                    var skipCount = 0
                    while (skipCount < offset && c.moveToNext()) {
                        skipCount++
                    }
                    
                    // limit만큼 읽기
                    var loadedCount = 0
                    while (loadedCount < limit && c.moveToNext()) {
                        try {
                            val id = c.getLong(idColumn)
                            val name = c.getString(nameColumn) ?: "unknown"
                            val size = c.getLong(sizeColumn)
                            
                            val contentUri = ContentUris.withAppendedId(uri, id)
                            images.add(contentUri.toString())
                            loadedCount++
                            
                            logd("이미지 추가: $name (${size}bytes)")
                        } catch (e: Exception) {
                            logd("개별 이미지 처리 중 오류: ${e.message}")
                            // 개별 이미지 오류는 전체 로딩을 중단하지 않음
                            continue
                        }
                    }
                } else {
                    // API 30+ 에서는 Bundle로 이미 페이징 처리됨
                    while (c.moveToNext()) {
                        try {
                            val id = c.getLong(idColumn)
                            val name = c.getString(nameColumn) ?: "unknown"
                            val size = c.getLong(sizeColumn)
                            
                            val contentUri = ContentUris.withAppendedId(uri, id)
                            images.add(contentUri.toString())
                            
                            logd("이미지 추가: $name (${size}bytes)")
                        } catch (e: Exception) {
                            logd("개별 이미지 처리 중 오류: ${e.message}")
                            // 개별 이미지 오류는 전체 로딩을 중단하지 않음
                            continue
                        }
                    }
                }
            } ?: run {
                logd("ContentResolver.query 결과가 null")
            }
            
        } catch (e: SecurityException) {
            logd("갤러리 접근 권한 없음: ${e.message}")
            throw GalleryPermissionException("갤러리 접근 권한이 필요합니다.")
        } catch (e: Exception) {
            logd("갤러리 이미지 로드 중 오류: ${e.message}")
            throw GalleryLoadException("갤러리 이미지를 불러올 수 없습니다.", e)
        } finally {
            cursor?.close()
        }
        
        return images
    }
}

/**
 * 갤러리 관련 커스텀 예외 클래스들
 */
class GalleryPermissionException(message: String) : Exception(message)
class GalleryLoadException(message: String, cause: Throwable?) : Exception(message, cause)