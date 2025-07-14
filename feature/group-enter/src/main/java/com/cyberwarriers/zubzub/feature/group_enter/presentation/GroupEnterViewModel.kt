package com.cyberwarriers.zubzub.feature.group_enter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_enter.domain.usecase.VerifyInviteCodeUseCase
import com.cyberwarriers.zubzub.feature.group_enter.presentation.effect.GroupEnterEffect
import com.cyberwarriers.zubzub.feature.group_enter.presentation.state.GroupEnterUiState
import com.cyberwarriers.zubzub.feature.group_enter.presentation.utils.EffectUtils.emitEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 그룹 입장 코드 입력 ViewModel
 */
@HiltViewModel
class GroupEnterViewModel @Inject constructor(
    private val verifyInviteCodeUseCase: VerifyInviteCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupEnterUiState())
    val uiState: StateFlow<GroupEnterUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GroupEnterEffect>()
    val effect: SharedFlow<GroupEnterEffect> = _effect

    /**
     * 초대 코드 입력 처리
     */
    fun onInviteCodeChanged(code: String) {
        _uiState.value = _uiState.value.copy(inviteCode = code)
    }

    /**
     * 코드 검증 실행
     */
    fun onVerifyCodeClicked() {
        val inviteCode = _uiState.value.inviteCode.trim()
        
        if (inviteCode.isBlank()) {
            emitEffect(GroupEnterEffect.ShowError("초대 코드를 입력해주세요."), _effect)
            return
        }

        verifyInviteCode(inviteCode)
    }

    /**
     * 초대 코드 검증
     */
    private fun verifyInviteCode(inviteCode: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                logd("초대 코드 검증 시작: $inviteCode")

                val result = verifyInviteCodeUseCase(inviteCode)
                
                result.fold(
                    onSuccess = { groupInfo ->
                        if (groupInfo != null) {
                            logd("그룹 찾음: ${groupInfo.groupName}")
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                inviteCode = ""
                            )
                            emitEffect(GroupEnterEffect.NavigateToConfirm(groupInfo), _effect)
                        } else {
                            logd("존재하지 않는 그룹")
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            emitEffect(GroupEnterEffect.ShowError("존재하지 않는 그룹입니다."), _effect)
                        }
                    },
                    onFailure = { exception ->
                        logd("초대 코드 검증 실패: ${exception.message}")
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        emitEffect(GroupEnterEffect.ShowError(
                            exception.message ?: "네트워크 오류가 발생했습니다."
                        ), _effect)
                    }
                )
            } catch (exception: Exception) {
                logd("예상치 못한 오류: ${exception.message}")
                _uiState.value = _uiState.value.copy(isLoading = false)
                emitEffect(GroupEnterEffect.ShowError(
                    "네트워크 오류가 발생했습니다. 다시 시도해주세요."
                ), _effect)
            }
        }
    }


} 