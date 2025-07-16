package com.cyberwarriers.zubzub.feature.group_cart.presentation.effect

/**
 * 그룹 카트 화면 Effect
 */
sealed class GroupCartEffect {
    object NavigateBack : GroupCartEffect()
    data class NavigateToMemberDetail(val memberId: String) : GroupCartEffect()
    object NavigateToGroupSettings : GroupCartEffect()
    data class ShowError(val message: String) : GroupCartEffect()
    data class ShowSuccess(val message: String) : GroupCartEffect()
    data class ShowToast(val message: String) : GroupCartEffect()
} 