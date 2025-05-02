package com.cyberwarriers.zubzub.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.auth.presentation.effect.LoginEffect
import com.cyberwarriers.zubzub.feature.auth.presentation.state.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Initial)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableStateFlow<LoginEffect?>(null)
    val effect: StateFlow<LoginEffect?> = _effect.asStateFlow()

    init {
        logd("LoginViewModel 초기화")
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            // 이미 로딩 중이면 중복 요청 방지
            if (state.value is LoginState.Loading) return@launch

            _state.value = LoginState.Loading
            logd("로그인 시도: $username")

            // 실제 로그인 로직은 여기에 구현 (지금은 mock)
            delay(1500) // 로딩 시뮬레이션

            // 간단한 검증 - 실제로는 API 호출 등을 통해 검증
            if (username.isNotEmpty() && password.isNotEmpty()) {
                _state.value = LoginState.Success
                _effect.value = LoginEffect.NavigateToHome
                logd("로그인 성공")
            } else {
                _state.value = LoginState.Error("아이디와 비밀번호를 입력해주세요")
                _effect.value = LoginEffect.ShowLoginFailed
                logd("로그인 실패: 입력 누락")
            }
        }
    }

    // effect를 처리한 후 null로 리셋
    fun consumeEffect() {
        _effect.value = null
    }
}