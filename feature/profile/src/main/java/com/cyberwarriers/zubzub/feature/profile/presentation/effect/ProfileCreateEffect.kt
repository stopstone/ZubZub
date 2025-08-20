package com.cyberwarriers.zubzub.feature.profile.presentation.effect

/**
 * 프로필 생성 화면 이펙트
 */
sealed class ProfileCreateEffect {
    object NavigateToHome : ProfileCreateEffect()
    object ShowCreateFailed : ProfileCreateEffect()
    object ShowCreateSuccess : ProfileCreateEffect()
}
