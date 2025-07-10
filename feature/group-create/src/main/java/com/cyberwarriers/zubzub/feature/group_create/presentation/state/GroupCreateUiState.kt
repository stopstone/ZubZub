package com.cyberwarriers.zubzub.feature.group_create.presentation.state

/**
 * 그룹 생성 화면의 UI 상태를 관리하는 데이터 클래스
 */
data class GroupCreateUiState(
    // 입력 필드 값들
    val groupName: String = "",

    // 에러 상태
    val groupNameError: String = "",

    // UI 상태
    val isLoading: Boolean = false,
    val isCreateButtonEnabled: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val showErrorDialog: Boolean = false,
    val errorMessage: String = ""
) {
    /**
     * 입력 필드에 에러가 있는지 확인
     */
    fun hasErrors(): Boolean {
        return groupNameError.isNotEmpty()
    }

    /**
     * 모든 필수 필드가 입력되었는지 확인
     */
    fun isFormValid(): Boolean {
        return groupName.isNotBlank() &&
                !hasErrors()
    }
} 