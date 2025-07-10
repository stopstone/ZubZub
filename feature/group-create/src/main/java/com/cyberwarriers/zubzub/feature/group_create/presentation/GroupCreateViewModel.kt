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
 * - 입력 값 검증 (비즈니스 로직)
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

    /**
     * 그룹 이름 입력 처리
     */
    fun onGroupNameChanged(name: String) {
        val error = validateGroupName(name)
        val isValid = isFormValid(name, error)
        
        _uiState.value = _uiState.value.copy(
            groupName = name,
            groupNameError = error,
            isCreateButtonEnabled = isValid,
        )
        logd("그룹 이름 변경: $name, 유효성: $isValid")
    }

    /**
     * 그룹 생성 버튼 클릭 처리
     */
    fun onCreateGroupClicked() {
        val currentState = _uiState.value
        if (!isFormValid(currentState.groupName, currentState.groupNameError)) {
            logd("폼이 유효하지 않음")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // TODO: 실제 그룹 생성 로직 구현
                delay(2000)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    navigateBack = true,
                )
                logd("그룹 생성 성공 - 화면 닫기")

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

    /**
     * 다이얼로그 닫기
     */
    fun onDismissDialog() {
        _uiState.value = _uiState.value.copy(
            showSuccessDialog = false,
            showErrorDialog = false,
            errorMessage = ""
        )
    }

    // ===========================================
    // 비즈니스 로직 (ViewModel 책임)
    // ===========================================

    /**
     * 폼 유효성 검사
     * @param name 그룹 이름
     * @param nameError 그룹 이름 에러 메시지
     * @return 폼이 유효한지 여부
     */
    private fun isFormValid(name: String, nameError: String): Boolean {
        return name.isNotBlank() && nameError.isEmpty()
    }

    /**
     * 그룹 이름 검증
     * @param name 검증할 그룹 이름
     * @return 에러 메시지 (유효하면 빈 문자열)
     */
    private fun validateGroupName(name: String): String {
        return when {
            name.isBlank() -> "그룹 이름을 입력해주세요."
            name.length < 2 -> "그룹 이름은 2글자 이상이어야 합니다."
            name.length > 20 -> "그룹 이름은 20글자 이하여야 합니다."
            else -> ""
        }
    }
}