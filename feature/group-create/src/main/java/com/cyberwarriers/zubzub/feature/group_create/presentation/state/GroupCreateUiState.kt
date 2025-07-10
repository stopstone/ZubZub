package com.cyberwarriers.zubzub.feature.group_create.presentation.state

/**
 * 그룹 생성 화면의 UI 상태를 관리하는 데이터 클래스
 * 
 * Clean Architecture 원칙에 따라 UI 상태를 중앙 집중화하여 관리
 */
data class GroupCreateUiState(
    // 입력 필드 값들
    val groupName: String = "",
    val groupDescription: String = "",
    val groupPassword: String = "",
    val maxMembers: String = "",
    
    // 에러 상태
    val groupNameError: String = "",
    val groupDescriptionError: String = "",
    val groupPasswordError: String = "",
    val maxMembersError: String = "",
    
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
        return groupNameError.isNotEmpty() ||
                groupDescriptionError.isNotEmpty() ||
                groupPasswordError.isNotEmpty() ||
                maxMembersError.isNotEmpty()
    }
    
    /**
     * 모든 필수 필드가 입력되었는지 확인
     */
    fun isFormValid(): Boolean {
        return groupName.isNotBlank() &&
                groupDescription.isNotBlank() &&
                !hasErrors()
    }
} 