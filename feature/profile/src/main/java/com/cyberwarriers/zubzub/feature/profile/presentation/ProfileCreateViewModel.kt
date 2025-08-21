package com.cyberwarriers.zubzub.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.domain.usecase.CreateProfileUseCase
import com.cyberwarriers.zubzub.feature.profile.presentation.effect.ProfileCreateEffect
import com.cyberwarriers.zubzub.feature.profile.presentation.state.ProfileCreateState
import com.cyberwarriers.zubzub.feature.profile.presentation.state.ProfileCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 프로필 생성 ViewModel
 * 
 * MVI 패턴을 사용하여 상태 관리
 */
@HiltViewModel
class ProfileCreateViewModel @Inject constructor(
    private val createProfileUseCase: CreateProfileUseCase,
) : ViewModel() {
    
    private val _state = MutableStateFlow<ProfileCreateState>(ProfileCreateState.Initial)
    val state: StateFlow<ProfileCreateState> = _state.asStateFlow()
    
    private val _uiState = MutableStateFlow(ProfileCreateUiState())
    val uiState: StateFlow<ProfileCreateUiState> = _uiState.asStateFlow()
    
    private val _effect = MutableStateFlow<ProfileCreateEffect?>(null)
    val effect: StateFlow<ProfileCreateEffect?> = _effect.asStateFlow()
    
    init {
        logd("ProfileCreateViewModel 초기화")
    }
    
    /**
     * 프로필 이름 변경
     */
    fun updateProfileName(name: String) {
        val currentUiState = _uiState.value
        
        // 20글자 제한
        val trimmedName = if (name.length > 20) name.take(20) else name
        
        val isError = trimmedName.isBlank()
        val errorMessage = if (isError) "프로필 이름을 입력해주세요." else ""
        
        _uiState.value = currentUiState.copy(
            profileName = trimmedName,
            isProfileNameError = isError,
            profileNameErrorMessage = errorMessage,
            isSubmitEnabled = currentUiState.copy(profileName = trimmedName).isSubmitButtonEnabled()
        )
        
        logd("프로필 이름 업데이트: $trimmedName")
    }
    
    /**
     * 프로필 이미지 URL 변경
     */
    fun updateProfileImageUrl(imageUrl: String) {
        val currentUiState = _uiState.value
        _uiState.value = currentUiState.copy(
            profileImageUrl = imageUrl
        )
        logd("프로필 이미지 URL 업데이트: $imageUrl")
    }
    
    /**
     * 프로필 생성
     */
    fun createProfile() = viewModelScope.launch {
        if (_state.value is ProfileCreateState.Loading) return@launch
        
        val currentUiState = _uiState.value
        
        // 유효성 검사
        if (currentUiState.profileName.isBlank()) {
            _uiState.value = currentUiState.copy(
                isProfileNameError = true,
                profileNameErrorMessage = "프로필 이름을 입력해주세요."
            )
            return@launch
        }
        
        if (currentUiState.profileName.length > 20) {
            _uiState.value = currentUiState.copy(
                isProfileNameError = true,
                profileNameErrorMessage = "프로필 이름은 20글자 이하로 입력해주세요."
            )
            return@launch
        }
        
        _state.value = ProfileCreateState.Loading
        logd("프로필 생성 시도: ${currentUiState.profileName}")
        
        createProfileUseCase(
            profileName = currentUiState.profileName,
            profileImageUri = currentUiState.profileImageUrl
        )
            .onSuccess { profile ->
                // 상태를 Success로 변경하지 않고 Loading 상태 유지하면서 바로 홈으로 이동
                _effect.value = ProfileCreateEffect.NavigateToHome
                logd("프로필 생성 성공: ${profile.profileName}")
            }
            .onFailure { exception ->
                _state.value = ProfileCreateState.Error(exception.message ?: "프로필 생성에 실패했습니다.")
                _effect.value = ProfileCreateEffect.ShowCreateFailed
                logd("프로필 생성 실패: ${exception.message}")
            }
    }
    
    /**
     * 이펙트 소비 (처리 완료 후 null로 리셋)
     */
    fun consumeEffect() {
        _effect.value = null
    }
    
    /**
     * 에러 상태 초기화
     */
    fun clearError() {
        if (_state.value is ProfileCreateState.Error) {
            _state.value = ProfileCreateState.Initial
        }
    }
}
