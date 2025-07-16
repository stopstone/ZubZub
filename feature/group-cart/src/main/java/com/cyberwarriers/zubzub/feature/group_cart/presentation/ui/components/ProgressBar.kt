package com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme

/**
 * 애니메이션이 적용된 진행률 바 컴포넌트
 */
@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0xFFE0E0E0) // 연한 회색
    val progressColor = Color(0xFF4CAF50) // 녹색
    
    // 애니메이션 적용된 진행률
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        label = "progress_animation"
    )
    
    Box(
        modifier = modifier
            .height(12.dp)
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        // 진행률 표시
        Box(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth(animatedProgress)
                .background(
                    color = progressColor,
                    shape = RoundedCornerShape(6.dp)
                )
        )
    }
}

@Preview
@Composable
fun ProgressBarPreview() {
    ZubZubTheme {
        ProgressBar(
            progress = 0.75f,
            modifier = Modifier.padding(16.dp)
        )
    }
}
