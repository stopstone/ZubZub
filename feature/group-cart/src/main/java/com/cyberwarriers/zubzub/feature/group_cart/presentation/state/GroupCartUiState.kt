package com.cyberwarriers.zubzub.feature.group_cart.presentation.state

import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.CartItem
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.Member

/**
 * 그룹 카트 화면 UI 상태
 */
data class GroupCartUiState(
    val selectedTabIndex: Int = 0,
    val cartItems: List<CartItem> = emptyList(),
    val members: List<Member> = emptyList(),
    val groupName: String = "",
    val memberCount: Int = 0,
    val isLoading: Boolean = false
) 