package com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 진행률 바 컴포넌트
 */
@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = Color(0xFFE0E0E0)
    val progressColor = Color(0xFF4CAF50)

    Box(
        modifier = modifier
            .height(12.dp)
            .fillMaxWidth()
            .background(
                color = backgroundColor,
            )
    ) {
        // 진행률 표시
        Box(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .background(
                    color = progressColor,
                )
        )
    }
}
