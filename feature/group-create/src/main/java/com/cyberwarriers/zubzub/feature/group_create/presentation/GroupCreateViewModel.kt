package com.cyberwarriers.zubzub.feature.group_create.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            groupNameError = validateGroupName(name),
            isCreateButtonEnabled = _uiState.value.copy(groupName = name).isFormValid()
        )
        logd("그룹 이름 변경: $name")
    }

    /**
     * 그룹 설명 입력 처리
     */
    fun onGroupDescriptionChanged(description: String) {
        _uiState.value = _uiState.value.copy(
            groupDescription = description,
            groupDescriptionError = validateGroupDescription(description),
            isCreateButtonEnabled = _uiState.value.copy(groupDescription = description).isFormValid()
        )
        logd("그룹 설명 변경: $description")
    }

    /**
     * 그룹 비밀번호 입력 처리
     */
    fun onGroupPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(
            groupPassword = password,
            groupPasswordError = validateGroupPassword(password)
        )
        logd("그룹 비밀번호 변경")
    }

    /**
     * 최대 멤버 수 입력 처리
     */
    fun onMaxMembersChanged(maxMembers: String) {
        _uiState.value = _uiState.value.copy(
            maxMembers = maxMembers,
            maxMembersError = validateMaxMembers(maxMembers)
        )
        logd("최대 멤버 수 변경: $maxMembers")
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
                kotlinx.coroutines.delay(2000)

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
    // 입력 값 검증 함수들
    // ===========================================

    private fun validateGroupName(name: String): String {
        return when {
            name.isBlank() -> "그룹 이름을 입력해주세요."
            name.length < 2 -> "그룹 이름은 2글자 이상이어야 합니다."
            name.length > 20 -> "그룹 이름은 20글자 이하여야 합니다."
            else -> ""
        }
    }

    private fun validateGroupDescription(description: String): String {
        return when {
            description.isBlank() -> "그룹 설명을 입력해주세요."
            description.length < 5 -> "그룹 설명은 5글자 이상이어야 합니다."
            description.length > 100 -> "그룹 설명은 100글자 이하여야 합니다."
            else -> ""
        }
    }

    private fun validateGroupPassword(password: String): String {
        return when {
            password.isNotEmpty() && password.length < 4 -> "비밀번호는 4글자 이상이어야 합니다."
            password.length > 10 -> "비밀번호는 10글자 이하여야 합니다."
            else -> ""
        }
    }

    private fun validateMaxMembers(maxMembers: String): String {
        if (maxMembers.isEmpty()) return ""

        return try {
            val number = maxMembers.toInt()
            when {
                number < 2 -> "최소 2명 이상이어야 합니다."
                number > 50 -> "최대 50명까지 가능합니다."
                else -> ""
            }
        } catch (e: NumberFormatException) {
            "숫자만 입력해주세요."
        }
    }
}