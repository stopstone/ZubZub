package com.cyberwarriers.zubzub.feature.group_enter.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_enter.domain.usecase.JoinGroupUseCase
import com.cyberwarriers.zubzub.feature.group_enter.domain.usecase.VerifyInviteCodeUseCase
import com.cyberwarriers.zubzub.feature.group_enter.presentation.effect.GroupConfirmEffect
import com.cyberwarriers.zubzub.feature.group_enter.presentation.state.GroupConfirmUiState
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
 * 그룹 정보 확인 ViewModel
 */
@HiltViewModel
class GroupConfirmViewModel @Inject constructor(
    private val verifyInviteCodeUseCase: VerifyInviteCodeUseCase,
    private val joinGroupUseCase: JoinGroupUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupConfirmUiState())
    val uiState: StateFlow<GroupConfirmUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GroupConfirmEffect>()
    val effect: SharedFlow<GroupConfirmEffect> = _effect

    private val groupId: String = savedStateHandle.get<String>("groupId") ?: ""

    init {
        if (groupId.isNotEmpty()) {
            loadGroupInfo(groupId)
        } else {
            logd("그룹 ID가 없음")
        }
    }

    /**
     * 그룹 정보 로드
     */
    private fun loadGroupInfo(groupId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                logd("그룹 정보 로드: $groupId")

                val result = verifyInviteCodeUseCase(groupId)

                result.fold(
                    onSuccess = { groupInfo ->
                        if (groupInfo != null) {
                            logd("그룹 정보 로드 성공: ${groupInfo.groupName}")
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                groupInfo = groupInfo
                            )
                        } else {
                            logd("그룹 정보 없음")
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            emitEffect(GroupConfirmEffect.ShowError("그룹 정보를 찾을 수 없습니다."), _effect)
                        }
                    },
                    onFailure = { exception ->
                        logd("그룹 정보 로드 실패: ${exception.message}")
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        emitEffect(
                            GroupConfirmEffect.ShowError(
                                exception.message ?: "그룹 정보를 불러올 수 없습니다."
                            ), _effect
                        )
                    }
                )
            } catch (exception: Exception) {
                logd("그룹 정보 로드 예외: ${exception.message}")
                _uiState.value = _uiState.value.copy(isLoading = false)
                emitEffect(GroupConfirmEffect.ShowError("네트워크 오류가 발생했습니다."), _effect)
            }
        }
    }

    /**
     * 그룹 참여하기
     */
    fun onJoinGroupClicked() {
        val currentGroupInfo = _uiState.value.groupInfo ?: return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isJoining = true)
                logd("그룹 참여 시작: ${currentGroupInfo.groupId}")

                val result = joinGroupUseCase(currentGroupInfo.groupId)

                result.fold(
                    onSuccess = {
                        logd("그룹 참여 성공")
                        _uiState.value = _uiState.value.copy(isJoining = false)
                        emitEffect(GroupConfirmEffect.NavigateToHome, _effect)
                    },
                    onFailure = { exception ->
                        logd("그룹 참여 실패: ${exception.message}")
                        _uiState.value = _uiState.value.copy(isJoining = false)
                        emitEffect(
                            GroupConfirmEffect.ShowError(
                                exception.message ?: "그룹 참여에 실패했습니다."
                            ), _effect
                        )
                    }
                )
            } catch (exception: Exception) {
                logd("그룹 참여 예외: ${exception.message}")
                _uiState.value = _uiState.value.copy(isJoining = false)
                emitEffect(GroupConfirmEffect.ShowError("네트워크 오류가 발생했습니다."), _effect)
            }
        }
    }


} 