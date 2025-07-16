package com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * 수량 카운터 컴포넌트
 * 클릭 가능한 수량 조절 컴포넌트
 */
@Composable
fun QuantityCounter(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val minQuantity = 1
    val maxQuantity = 99
    val minusInteractionSource = remember { MutableInteractionSource() }
    val plusInteractionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(56.dp)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 마이너스 버튼
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "수량 감소",
                tint = if (quantity > minQuantity) {
                    Color.Black
                } else {
                    Color.LightGray
                },
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        interactionSource = minusInteractionSource,
                        indication = null
                    ) {
                        if (quantity > minQuantity) {
                            onQuantityChange(quantity - 1)
                        }
                    }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 숫자 영역 고정 너비
            Box(
                modifier = Modifier.width(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = quantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 플러스 버튼
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "수량 증가",
                tint = if (quantity < maxQuantity) {
                    Color.Black
                } else {
                    Color.LightGray
                },
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        interactionSource = plusInteractionSource,
                        indication = null
                    ) {
                        if (quantity < maxQuantity) {
                            onQuantityChange(quantity + 1)
                        }
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuantityCounterPreview() {
    MaterialTheme {
        QuantityCounter(
            quantity = 3,
            onQuantityChange = {}
        )
    }
}