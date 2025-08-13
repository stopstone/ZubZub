package com.cyberwarriers.zubzub.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.Constants
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
import com.cyberwarriers.zubzub.feature.splash.presentation.effect.SplashEffect
import com.cyberwarriers.zubzub.feature.splash.presentation.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // State 관리
    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    // Effect 관리
    private val _effect = MutableStateFlow<SplashEffect?>(null)
    val effect: StateFlow<SplashEffect?> = _effect.asStateFlow()

    init {
        logd("SplashViewModel 초기화")
        checkLoginStatusAndNavigate()
    }

    /**
     * 로그인 상태를 확인하고 적절한 화면으로 이동하는 함수
     */
    private fun checkLoginStatusAndNavigate() {
        viewModelScope.launch {
            try {
                // 스플래시 화면 지속 시간만큼 대기
                delay(Constants.SPLASH_SCREEN_DURATION)
                
                // DataStore에서 로그인 상태 확인
                val isLoggedIn = authRepository.isUserLoggedInFromDataStore().first()
                
                logd("DataStore 로그인 상태 확인: $isLoggedIn")
                
                if (isLoggedIn) {
                    // 로그인된 상태라면 홈 화면으로 이동
                    _state.value = SplashState.Loaded
                    _effect.value = SplashEffect.NavigateToHome
                    logd("자동 로그인 성공 - 홈 화면으로 이동")
                } else {
                    // 로그인되지 않은 상태라면 로그인 화면으로 이동
                    _state.value = SplashState.Loaded
                    _effect.value = SplashEffect.NavigateToLogin
                    logd("로그인 필요 - 로그인 화면으로 이동")
                }
            } catch (e: Exception) {
                logd("로그인 상태 확인 중 오류 발생: ${e.message}")
                // 오류 발생 시 로그인 화면으로 이동
                _state.value = SplashState.Loaded
                _effect.value = SplashEffect.NavigateToLogin
            }
        }
    }
}