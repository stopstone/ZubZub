package com.cyberwarriers.zubzub.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.Constants
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.splash.presentation.effect.SplashEffect
import com.cyberwarriers.zubzub.feature.splash.presentation.state.SplashState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    // State 관리
    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    // Effect 관리
    private val _effect = MutableStateFlow<SplashEffect?>(null)
    val effect: StateFlow<SplashEffect?> = _effect.asStateFlow()

    init {
        logd("SplashViewModel 초기화")
        startSplashTimer()
    }

    private fun startSplashTimer() {
        viewModelScope.launch {
            delay(Constants.SPLASH_SCREEN_DURATION)
            _state.value = SplashState.Loaded
            _effect.value = SplashEffect.NavigateToLogin
            logd("스플래시 타이머 완료")
        }
    }
}