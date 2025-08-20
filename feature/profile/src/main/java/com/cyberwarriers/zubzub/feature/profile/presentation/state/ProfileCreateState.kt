package com.cyberwarriers.zubzub.feature.profile.presentation.state

/**
 * 프로필 생성 화면 상태
 */
sealed class ProfileCreateState {
    object Initial : ProfileCreateState()
    object Loading : ProfileCreateState()
    data class Error(val message: String) : ProfileCreateState()
    object Success : ProfileCreateState()
}

/**
 * 프로필 생성 화면 UI 상태
 */
data class ProfileCreateUiState(
    val profileName: String = "",
    val profileImageUrl: String = "",
    val isProfileNameError: Boolean = false,
    val profileNameErrorMessage: String = "",
    val isSubmitEnabled: Boolean = false
) {
    /**
     * 제출 버튼 활성화 여부 계산
     */
    fun isSubmitButtonEnabled(): Boolean {
        return profileName.isNotBlank() && 
               profileName.length <= 20 && 
               !isProfileNameError
    }
}
