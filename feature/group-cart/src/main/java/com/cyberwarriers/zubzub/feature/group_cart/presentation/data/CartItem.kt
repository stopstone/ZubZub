package com.cyberwarriers.zubzub.feature.group_cart.presentation.data

/**
 * 카트 아이템 데이터 클래스
 */
data class CartItem(
    val id: String,
    val name: String,
    val price: Int,
    val quantity: Int,
    val addedBy: String,
    val isCompleted: Boolean = false
)
