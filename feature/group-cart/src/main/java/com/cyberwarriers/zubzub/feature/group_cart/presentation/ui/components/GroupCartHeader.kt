package com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme

/**
 * 그룹 카트 헤더 컴포넌트
 */
@Composable
fun GroupCartHeader(
    groupName: String,
    memberCount: Int,
    progress: Float = 0f,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 그룹명 큰 텍스트
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            text = groupName,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        // 멤버 수
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "${memberCount}명 참여",
            fontSize = 12.sp,
        )

        // 진행률 바
        ProgressBar(
            progress = progress,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Preview
@Composable
fun GroupCartHeaderPreview() {
    ZubZubTheme {
        GroupCartHeader(
            groupName = "우리 가족",
            memberCount = 4,
            progress = 0.6f
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