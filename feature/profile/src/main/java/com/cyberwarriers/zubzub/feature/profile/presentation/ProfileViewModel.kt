package com.cyberwarriers.zubzub.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.domain.model.UserProfile
import com.cyberwarriers.zubzub.core.domain.usecase.GetUserProfilesUseCase
import com.cyberwarriers.zubzub.core.util.logd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 프로필 화면 ViewModel
 * 
 * 역할:
 * - 사용자 프로필 목록 관리
 * - 프로필 관련 UI 상태 관리
 * - 실시간 프로필 데이터 업데이트
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfilesUseCase: GetUserProfilesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfiles()
    }

    /**
     * 사용자 프로필 목록 로드
     */
    private fun loadUserProfiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getUserProfilesUseCase()
                .catch { exception ->
                    logd("프로필 목록 로드 실패: ${exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "프로필 목록을 불러올 수 없습니다."
                    )
                }
                .collect { profiles ->
                    logd("프로필 목록 로드 성공: ${profiles.size}개")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profiles = profiles,
                        errorMessage = null
                    )
                }
        }
    }

    /**
     * 프로필 새로고침 (실시간 업데이트에서는 에러 상태만 클리어)
     */
    fun refreshProfiles() {
        logd("🔄 실시간 업데이트 - 에러 상태 클리어")
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
        // 실시간 리스너가 자동으로 최신 데이터를 가져오므로 별도 호출 불필요
    }

    /**
     * 프로필 클릭 처리
     */
    fun onProfileClicked(profileId: String) {
        logd("프로필 클릭: $profileId")
        // TODO: 프로필 상세 화면 이동 로직 구현
    }

    /**
     * 프로필 추가 클릭 처리
     */
    fun onAddProfileClicked() {
        logd("프로필 추가 클릭")
        // TODO: 프로필 생성 화면 이동 로직 구현
    }

    /**
     * 에러 메시지 클리어
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * 프로필 화면 UI 상태
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val profiles: List<UserProfile> = emptyList(),
    val errorMessage: String? = null
)
