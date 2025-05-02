package com.cyberwarriers.zubzub.feature.auth.presentation.state

sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    data class Error(val message: String) : LoginState()
    object Success : LoginState()
}