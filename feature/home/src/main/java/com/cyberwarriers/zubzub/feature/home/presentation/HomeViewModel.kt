package com.cyberwarriers.zubzub.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.core.domain.model.CartGroupSummary
import com.cyberwarriers.zubzub.core.domain.usecase.GetUserGroupsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 홈 화면 ViewModel
 * 
 * 역할:
 * - 사용자가 속한 그룹 목록 관리
 * - 그룹 관련 UI 상태 관리
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserGroupsUseCase: GetUserGroupsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserGroups()
    }

    /**
     * 사용자가 속한 그룹 목록 로드
     */
    private fun loadUserGroups() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getUserGroupsUseCase()
                .catch { exception ->
                    logd("그룹 목록 로드 실패: ${exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "그룹 목록을 불러올 수 없습니다."
                    )
                }
                .collect { groups ->
                    logd("그룹 목록 로드 성공: ${groups.size}개")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        groups = groups,
                        errorMessage = null
                    )
                }
        }
    }

    /**
     * 그룹 새로고침 (실시간 업데이트에서는 에러 상태만 클리어)
     */
    fun refreshGroups() {
        logd("🔄 실시간 업데이트 - 에러 상태 클리어")
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
        // 실시간 리스너가 자동으로 최신 데이터를 가져오므로 별도 호출 불필요
    }

    /**
     * 그룹 클릭 처리
     */
    fun onGroupClicked(groupId: String) {
        logd("그룹 클릭: $groupId")
        // TODO: 그룹 상세 화면 이동 로직 구현
    }

    /**
     * 에러 메시지 클리어
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * 홈 화면 UI 상태
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val groups: List<CartGroupSummary> = emptyList(),
    val errorMessage: String? = null
) 