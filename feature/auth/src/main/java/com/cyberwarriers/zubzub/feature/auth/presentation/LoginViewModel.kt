package com.cyberwarriers.zubzub.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.domain.repository.AuthRepository
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
    private val authRepository: AuthRepository
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
                logd("로그인 성공 - 프로필 존재 여부 확인")
                // 로그인 성공 후 프로필 존재 여부 확인
                checkUserProfileAndNavigate()
            }
            .onFailure { exception ->
                _state.value = LoginState.Error(exception.message ?: "로그인에 실패하였습니다.")
                _effect.value = LoginEffect.ShowLoginFailed
                logd("구글 로그인 실패: ${exception.message}")
            }
    }
    
    /**
     * 사용자 프로필 존재 여부를 확인하고 적절한 화면으로 네비게이션
     */
    private suspend fun checkUserProfileAndNavigate() {
        authRepository.hasUserProfile()
            .onSuccess { hasProfile ->
                _state.value = LoginState.Success
                if (hasProfile) {
                    _effect.value = LoginEffect.NavigateToHome
                    logd("기존 사용자 - 홈 화면으로 이동")
                } else {
                    _effect.value = LoginEffect.NavigateToProfileCreate
                    logd("새 사용자 - 프로필 생성 화면으로 이동")
                }
            }
            .onFailure { exception ->
                // 프로필 확인 실패 시에도 홈으로 이동 (fallback)
                _state.value = LoginState.Success
                _effect.value = LoginEffect.NavigateToHome
                logd("프로필 확인 실패하지만 홈으로 이동: ${exception.message}")
            }
    }

    // effect를 처리한 후 null로 리셋
    fun consumeEffect() {
        _effect.value = null
    }
}