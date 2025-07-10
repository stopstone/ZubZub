package com.cyberwarriers.zubzub.feature.group_create.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_create.presentation.state.GroupCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 그룹 생성 화면의 ViewModel
 *
 * 역할:
 * - UI 상태 관리
 * - 입력 값 검증
 * - 그룹 생성 비즈니스 로직 처리
 * - 에러 처리
 */
@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    // 향후 UseCase 주입 예정
    // private val createGroupUseCase: CreateGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupCreateUiState())
    val uiState: StateFlow<GroupCreateUiState> = _uiState.asStateFlow()

    init {
        logd("GroupCreateViewModel 초기화")
    }

    /**
     * 그룹 이름 입력 처리
     */
    fun onGroupNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(
            groupName = name,
            isCreateButtonEnabled = _uiState.value.copy(groupName = name).isFormValid()
        )
        logd("그룹 이름 변경: $name")
    }

    /**
     * 그룹 생성 버튼 클릭 처리
     */
    fun onCreateGroupClicked() {
        if (!_uiState.value.isFormValid()) {
            logd("폼이 유효하지 않음")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // TODO: 실제 그룹 생성 로직 구현
                // createGroupUseCase.invoke(...)

                // 임시로 2초 딜레이 후 성공 처리
                delay(2000)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showSuccessDialog = true
                )
                logd("그룹 생성 성공")

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showErrorDialog = true,
                    errorMessage = e.message ?: "그룹 생성에 실패했습니다."
                )
                logd("그룹 생성 실패: ${e.message}")
            }
        }
    }
}