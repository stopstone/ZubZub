package com.cyberwarriers.zubzub.feature.group_cart.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.feature.group_cart.presentation.GroupCartViewModel
import com.cyberwarriers.zubzub.feature.group_cart.presentation.effect.GroupCartEffect
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components.CartListContent
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components.GroupCartHeader
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components.GroupCartTabRow
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components.MembersContent
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components.TabItem

/**
 * 그룹 카트 화면
 */
@Composable
fun GroupCartScreen(
    viewModel: GroupCartViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GroupCartEffect.NavigateBack -> {
                    onNavigateBack()
                }

                is GroupCartEffect.NavigateToAddItem -> {
                    // TODO: 아이템 추가 화면으로 네비게이션
                }

                is GroupCartEffect.NavigateToMemberDetail -> {
                    // TODO: 멤버 상세 정보 화면으로 네비게이션
                }

                is GroupCartEffect.NavigateToGroupSettings -> {
                    // TODO: 그룹 설정 화면으로 네비게이션
                }

                is GroupCartEffect.ShowError -> {
                    // TODO: 에러 메시지 표시
                }

                is GroupCartEffect.ShowSuccess -> {
                    // TODO: 성공 메시지 표시
                }

                is GroupCartEffect.ShowToast -> {
                    // TODO: 토스트 메시지 표시
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
        ) {
            // TopAppBar
            ZubZubTopAppBar(
                title = "",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = { viewModel.onNavigateBack() }
            )

            // 그룹 카트 헤더
            GroupCartHeader(
                groupName = uiState.groupName,
                memberCount = uiState.memberCount
            )

            // 탭 레이아웃
            GroupCartTabRow(
                selectedTabIndex = uiState.selectedTabIndex,
                onTabSelected = viewModel::onTabSelected
            )

            // 탭 콘텐츠
            when (uiState.selectedTabIndex) {
                TabItem.CART.index -> {
                    CartListContent(
                        cartItems = uiState.cartItems,
                        onItemToggle = { itemId ->
                            val item = uiState.cartItems.find { it.id == itemId }
                            item?.let {
                                viewModel.onCartItemCompletedChanged(
                                    itemId,
                                    !it.isCompleted
                                )
                            }
                        },
                        onCartItemAdd = { name, price, quantity ->
                            viewModel.onCartItemAdd(name, price, quantity)
                        }
                    )
                }

                TabItem.MEMBERS.index -> {
                    MembersContent(
                        members = uiState.members,
                        onMemberClick = { memberId ->
                            // TODO: 멤버 상세 정보 화면으로 네비게이션
                        }
                    )
                }
            }
        }
    }
}