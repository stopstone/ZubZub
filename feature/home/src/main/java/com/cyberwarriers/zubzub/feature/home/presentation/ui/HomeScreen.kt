package com.cyberwarriers.zubzub.feature.home.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.home.R
import com.cyberwarriers.zubzub.feature.home.presentation.ui.components.GroupCard

@Composable
fun HomeScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            ZubZubTopAppBar(
                title = "홈"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                GroupCard(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    cardText = "그룹 생성",
                    cardImageRes = R.drawable.ic_add,
                )
                Spacer(modifier = Modifier.width(12.dp))
                GroupCard(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    cardText = "그룹 입장",
                    cardImageRes = R.drawable.ic_enter,
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    ZubZubTheme {
        HomeScreen()
    }
}