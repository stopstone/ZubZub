package com.cyberwarriers.zubzub.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.auth.domain.usecase.SignInWithGoogleUseCase
import com.cyberwarriers.zubzub.feature.auth.presentation.effect.LoginEffect
import com.cyberwarriers.zubzub.feature.auth.presentation.state.LoginState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Initial)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableStateFlow<LoginEffect?>(null)
    val effect: StateFlow<LoginEffect?> = _effect.asStateFlow()

    init {
        logd("LoginViewModel 초기화")
    }

    fun signInWithGoogle(account: GoogleSignInAccount) = viewModelScope.launch {
        if (_state.value is LoginState.Loading) return@launch

        _state.value = LoginState.Loading
        logd("구글 소셜 로그인 시도")
        signInWithGoogleUseCase(account)
            .onSuccess {
                _state.value = LoginState.Success
                _effect.value = LoginEffect.NavigateToHome
                logd("로그인 성공: 홈 화면 이동")
            }
            .onFailure { exception ->
                _state.value = LoginState.Error(exception.message ?: "로그인에 실패하였습니다.")
                _effect.value = LoginEffect.ShowLoginFailed
                logd("구글 로그인 실패: ${exception.message}")
            }
    }

    // effect를 처리한 후 null로 리셋
    fun consumeEffect() {
        _effect.value = null
    }
}