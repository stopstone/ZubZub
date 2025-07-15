package com.cyberwarriers.zubzub.feature.group_cart.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_cart.domain.usecase.GetGroupCartDetailUseCase
import com.cyberwarriers.zubzub.feature.group_cart.domain.usecase.UpdateCartItemStatusUseCase
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.CartItem
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.Member
import com.cyberwarriers.zubzub.feature.group_cart.presentation.effect.GroupCartEffect
import com.cyberwarriers.zubzub.feature.group_cart.presentation.mapper.toPresentation
import com.cyberwarriers.zubzub.feature.group_cart.presentation.state.GroupCartUiState
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 그룹 카트 화면 ViewModel (Clean Architecture 적용)
 */
@HiltViewModel
class GroupCartViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGroupCartDetailUseCase: GetGroupCartDetailUseCase,
    private val updateCartItemStatusUseCase: UpdateCartItemStatusUseCase
) : ViewModel() {

    // UI 상태 관리
    private val _uiState = MutableStateFlow(GroupCartUiState())
    val uiState: StateFlow<GroupCartUiState> = _uiState.asStateFlow()

    // 일회성 이벤트 관리
    private val _effect = MutableSharedFlow<GroupCartEffect>()
    val effect: SharedFlow<GroupCartEffect> = _effect.asSharedFlow()

    // Navigation argument에서 groupId 가져오기
    private val groupId: String = savedStateHandle.get<String>("groupId") ?: ""

    init {
        loadGroupCartDetail()
    }

    /**
     * 그룹 카트 상세 정보 로드
     */
    private fun loadGroupCartDetail() {
        if (groupId.isEmpty()) {
            logd("GroupId가 비어있습니다.")
            _uiState.update { it.copy(isLoading = false) }
            emitEffect(GroupCartEffect.ShowError("잘못된 그룹 ID입니다"))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            getGroupCartDetailUseCase(groupId)
                .catch { exception ->
                    logd("그룹 카트 데이터 로드 실패: ${exception.message}")
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(GroupCartEffect.ShowError(exception.message ?: "데이터를 불러올 수 없습니다"))
                }
                .collect { groupCartDetail ->
                    logd("그룹 카트 데이터 로드 성공: ${groupCartDetail.groupName}")
                    
                    _uiState.update { currentState ->
                        currentState.copy(
                            groupName = groupCartDetail.groupName,
                            memberCount = groupCartDetail.memberCount,
                            cartItems = groupCartDetail.cartItems.map { it.toPresentation() },
                            members = groupCartDetail.members.map { it.toPresentation() },
                            isLoading = false
                        )
                    }
                }
        }
    }

    /**
     * 뒤로 가기
     */
    fun onNavigateBack() {
        emitEffect(GroupCartEffect.NavigateBack)
    }

    /**
     * Effect 발생
     */
    fun emitEffect(effect: GroupCartEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    /**
     * 탭 선택 처리
     */
    fun onTabSelected(tabIndex: Int) {
        val selectedTab = TabItem.fromIndex(tabIndex)
        if (selectedTab != null) {
            _uiState.update { it.copy(selectedTabIndex = tabIndex) }
        }
    }

    /**
     * 카트 아이템 완료 상태 변경
     */
    fun onCartItemCompletedChanged(itemId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            // 낙관적 업데이트 (UI 먼저 변경)
            _uiState.update { currentState ->
                currentState.copy(
                    cartItems = currentState.cartItems.map { item ->
                        if (item.id == itemId) {
                            item.copy(isCompleted = isCompleted)
                        } else {
                            item
                        }
                    }
                )
            }

            // 서버에 업데이트 요청
            updateCartItemStatusUseCase(groupId, itemId, isCompleted)
                .onSuccess {
                    logd("아이템 상태 업데이트 성공: $itemId -> $isCompleted")
                    emitEffect(GroupCartEffect.ShowSuccess("아이템 상태가 변경되었습니다"))
                }
                .onFailure { exception ->
                    logd("아이템 상태 업데이트 실패: ${exception.message}")
                    
                    // 실패 시 원래 상태로 되돌리기
                    _uiState.update { currentState ->
                        currentState.copy(
                            cartItems = currentState.cartItems.map { item ->
                                if (item.id == itemId) {
                                    item.copy(isCompleted = !isCompleted)
                                } else {
                                    item
                                }
                            }
                        )
                    }
                    
                    emitEffect(GroupCartEffect.ShowError("상태 변경에 실패했습니다"))
                }
        }
    }

    /**
     * 새로고침
     */
    fun onRefresh() {
        loadGroupCartDetail()
    }
} 