package com.cyberwarriers.zubzub.feature.splash.presentation.state

sealed class SplashState {
    object Loading : SplashState()
    object Loaded : SplashState()
}