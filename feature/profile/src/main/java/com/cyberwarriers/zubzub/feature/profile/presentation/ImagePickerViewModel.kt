package com.cyberwarriers.zubzub.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.domain.usecase.CheckGalleryPermissionUseCase
import com.cyberwarriers.zubzub.feature.profile.domain.usecase.GetGalleryImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 이미지 선택 화면의 ViewModel
 * 
 * 특징:
 * - Context 의존성 제거로 테스트 가능하고 메모리 안전
 * - Repository 패턴으로 데이터 레이어 추상화
 * - 상태 관리 중앙화 및 단방향 데이터 플로우
 * - 페이징 캐시 최적화
 */
@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val getGalleryImagesUseCase: GetGalleryImagesUseCase,
    private val checkGalleryPermissionUseCase: CheckGalleryPermissionUseCase
): ViewModel() {
    
    // 선택된 이미지 URI 상태
    private val _selectedImageUri = MutableStateFlow<String?>(null)
    val selectedImageUri: StateFlow<String?> = _selectedImageUri.asStateFlow()
    
    // 권한 상태
    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission.asStateFlow()
    
    // 로딩 상태
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // 에러 상태
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // 페이징된 갤러리 이미지 플로우 (ViewModel 스코프에서 캐시)
    val galleryImages: Flow<PagingData<String>> = getGalleryImagesUseCase()
        .cachedIn(viewModelScope)
    
    init {
        checkPermission()
    }
    
    /**
     * 갤러리 권한 상태를 확인합니다.
     */
    fun checkPermission() = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null
        
        checkGalleryPermissionUseCase()
            .onSuccess { hasPermission ->
                _hasPermission.value = hasPermission
                logd("권한 확인 완료: $hasPermission")
            }
            .onFailure { exception ->
                _errorMessage.value = "권한 확인 중 오류가 발생했습니다."
                _hasPermission.value = false
                logd("권한 확인 실패: ${exception.message}")
            }
        
        _isLoading.value = false
    }
    
    /**
     * 이미지를 선택하거나 선택 해제합니다 (토글).
     * 
     * @param imageUri 선택할 이미지 URI
     */
    fun selectImage(imageUri: String) {
        if (_selectedImageUri.value == imageUri) {
            // 이미 선택된 이미지를 다시 클릭하면 선택 해제
            _selectedImageUri.value = null
            logd("이미지 선택 해제됨: $imageUri")
        } else {
            // 새로운 이미지 선택
            _selectedImageUri.value = imageUri
            logd("이미지 선택됨: $imageUri")
        }
    }
    
    /**
     * 선택된 이미지를 초기화합니다.
     */
    fun clearSelection() {
        _selectedImageUri.value = null
        logd("이미지 선택 초기화")
    }
    
    /**
     * 에러 메시지를 초기화합니다.
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * 권한 업데이트 (권한 요청 후 호출)
     */
    fun onPermissionResult(granted: Boolean) {
        _hasPermission.value = granted
        if (granted) {
            _errorMessage.value = null
            logd("권한 승인됨")
        } else {
            _errorMessage.value = "갤러리 접근 권한이 필요합니다."
            logd("권한 거부됨")
        }
    }
}