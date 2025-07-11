package com.cyberwarriers.zubzub.feature.group_create.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.domain.usecase.CreateGroupUseCase
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_create.presentation.state.GroupCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
 * - UseCase 호출을 통한 비즈니스 로직 실행
 * - UI 에러 처리
 */
@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupCreateUiState())
    val uiState: StateFlow<GroupCreateUiState> = _uiState.asStateFlow()

    /**
     * 그룹 이름 입력 처리 (UI 검증만)
     */
    fun onGroupNameChanged(name: String) {
        val error = validateGroupNameForUI(name)
        val isValid = name.isNotBlank() && error.isEmpty()
        
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
        if (currentState.groupName.isBlank() || currentState.groupNameError.isNotEmpty()) {
            logd("폼이 유효하지 않음")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val result = createGroupUseCase(
                    groupName = currentState.groupName,
                    description = "",
                    targetAmount = 0L,
                )

                result.fold(
                    onSuccess = { groupId ->
                        logd("그룹 생성 성공: $groupId")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            groupId = groupId,
                            navigateToConfirm = true,
                        )
                    },
                    onFailure = { exception ->
                        logd("그룹 생성 실패: ${exception.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            showErrorDialog = true,
                            errorMessage = exception.message ?: "그룹 생성에 실패했습니다."
                        )
                    }
                )

            } catch (e: Exception) {
                logd("그룹 생성 예외 발생: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showErrorDialog = true,
                    errorMessage = e.message ?: "그룹 생성에 실패했습니다."
                )
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

    /**
     * 네비게이션 상태 리셋
     */
    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(
            navigateToConfirm = false
        )
    }

    // ===========================================
    // UI 검증 로직 (ViewModel 책임)
    // ===========================================

    /**
     * UI용 그룹 이름 검증 (실시간 피드백용)
     * 비즈니스 로직 검증은 UseCase에서 처리
     * @param name 검증할 그룹 이름
     * @return 에러 메시지 (유효하면 빈 문자열)
     */
    private fun validateGroupNameForUI(name: String): String {
        return when {
            name.isBlank() -> "그룹 이름을 입력해주세요."
            name.length < 2 -> "그룹 이름은 2글자 이상이어야 합니다."
            name.length > 20 -> "그룹 이름은 20글자 이하여야 합니다."
            else -> ""
        }
    }
}