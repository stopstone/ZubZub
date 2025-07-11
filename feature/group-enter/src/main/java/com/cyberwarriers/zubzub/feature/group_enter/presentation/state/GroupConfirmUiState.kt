package com.cyberwarriers.zubzub.feature.group_enter.presentation.state

import com.cyberwarriers.zubzub.feature.group_enter.domain.model.GroupInfo

/**
 * 그룹 정보 확인 화면 UI 상태
 */
data class GroupConfirmUiState(
    val groupInfo: GroupInfo? = null,
    val isLoading: Boolean = false,
    val isJoining: Boolean = false,
    val navigateToGroup: Boolean = false,
    val errorMessage: String = ""
) 