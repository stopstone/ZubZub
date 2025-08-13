package com.cyberwarriers.zubzub.feature.splash.presentation.effect

sealed class SplashEffect {
    object NavigateToLogin : SplashEffect()
    object NavigateToHome : SplashEffect()
}