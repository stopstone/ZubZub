package com.cyberwarriers.zubzub.feature.group_create.presentation.state

/**
 * 그룹 생성 화면의 UI 상태를 관리하는 데이터 클래스
 * 
 * Clean Architecture 원칙에 따라 순수한 데이터만 보관
 * 비즈니스 로직은 ViewModel에서 처리
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
) 