package com.cyberwarriers.zubzub.feature.auth.presentation.effect

sealed class LoginEffect {
    object NavigateToHome : LoginEffect()
    object ShowLoginFailed : LoginEffect()
}