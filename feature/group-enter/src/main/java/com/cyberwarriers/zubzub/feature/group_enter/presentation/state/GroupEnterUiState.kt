package com.cyberwarriers.zubzub.feature.group_enter.presentation.state

/**
 * 그룹 입장 코드 입력 화면 UI 상태
 */
data class GroupEnterUiState(
    val inviteCode: String = "",
    val inviteCodeError: String = "",
    val isLoading: Boolean = false,
    val navigateToConfirm: String? = null // groupId가 있으면 확인 화면으로 이동
) 