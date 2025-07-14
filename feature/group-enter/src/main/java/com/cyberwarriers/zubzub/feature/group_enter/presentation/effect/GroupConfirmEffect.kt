package com.cyberwarriers.zubzub.feature.group_enter.presentation.effect

/**
 * 그룹 확인 화면 Effect
 */
sealed class GroupConfirmEffect {
    object NavigateToHome : GroupConfirmEffect()
    data class ShowError(val message: String) : GroupConfirmEffect()
} 