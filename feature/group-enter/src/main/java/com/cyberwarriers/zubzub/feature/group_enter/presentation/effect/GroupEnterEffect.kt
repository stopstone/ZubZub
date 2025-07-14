package com.cyberwarriers.zubzub.feature.group_enter.presentation.effect

import com.cyberwarriers.zubzub.feature.group_enter.domain.model.GroupInfo

/**
 * 그룹 입장 화면 Effect
 */
sealed class GroupEnterEffect {
    data class NavigateToConfirm(val groupInfo: GroupInfo) : GroupEnterEffect()
    data class ShowError(val message: String) : GroupEnterEffect()
} 