package com.cyberwarriers.zubzub.feature.group_cart.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.CartItem
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.Member
import com.cyberwarriers.zubzub.feature.group_cart.presentation.effect.GroupCartEffect
import com.cyberwarriers.zubzub.feature.group_cart.presentation.state.GroupCartUiState
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 그룹 카트 화면 ViewModel
 */
@HiltViewModel
class GroupCartViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
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
        loadGroupData(groupId)
    }

    /**
     * 특정 그룹 데이터 로드
     */
    private fun loadGroupData(groupId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                groupName = if (groupId.isNotEmpty()) "그룹 ID: $groupId" else "알 수 없는 그룹",
                memberCount = 4,
                cartItems = getDummyCartItems(),
                members = getDummyMembers()
            )
        }
    }

    /**
     * 초기 데이터 로드
     */
    private fun loadInitialData() {
        _uiState.update { currentState ->
            currentState.copy(
                groupName = "우리 가족",
                memberCount = 4,
                cartItems = getDummyCartItems(),
                members = getDummyMembers()
            )
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
    }

    /**
     * 더미 카트 아이템 생성
     */
    private fun getDummyCartItems(): List<CartItem> {
        return listOf(
            CartItem(
                id = "1",
                name = "사과",
                price = 2000,
                quantity = 3,
                addedBy = "엄마",
                isCompleted = false
            ),
            CartItem(
                id = "2", 
                name = "바나나",
                price = 1500,
                quantity = 2,
                addedBy = "아빠",
                isCompleted = true
            ),
            CartItem(
                id = "3",
                name = "우유",
                price = 3500,
                quantity = 1,
                addedBy = "첫째",
                isCompleted = false
            )
        )
    }

    /**
     * 더미 멤버 데이터 생성
     */
    private fun getDummyMembers(): List<Member> {
        return listOf(
            Member(
                id = "1",
                name = "엄마",
                email = "mom@example.com",
                isGroupLeader = true,
                joinDate = "2024-01-01",
                totalContribution = 50000,
                isSelected = true
            ),
            Member(
                id = "2",
                name = "아빠", 
                email = "dad@example.com",
                isGroupLeader = false,
                joinDate = "2024-01-01",
                totalContribution = 30000,
                isSelected = false
            ),
            Member(
                id = "3",
                name = "첫째",
                email = "first@example.com",
                isGroupLeader = false,
                joinDate = "2024-01-01",
                totalContribution = 20000,
                isSelected = true
            ),
            Member(
                id = "4",
                name = "둘째",
                email = "second@example.com",
                isGroupLeader = false,
                joinDate = "2024-01-01",
                totalContribution = 10000,
                isSelected = false
            )
        )
    }
} 