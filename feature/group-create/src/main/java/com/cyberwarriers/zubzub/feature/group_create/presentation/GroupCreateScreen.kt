package com.cyberwarriers.zubzub.feature.group_create.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme

@Composable
fun GroupCreateScreen(
    onNavigateBack: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            ZubZubTopAppBar(
                title = "그룹 생성",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = onNavigateBack
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "그룹 생성 화면",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupCreateScreenPreview() {
    ZubZubTheme {
        GroupCreateScreen()
    }
}