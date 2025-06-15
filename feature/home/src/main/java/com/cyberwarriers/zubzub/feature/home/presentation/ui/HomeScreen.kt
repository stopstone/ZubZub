package com.cyberwarriers.zubzub.feature.home.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.home.presentation.ui.components.GroupActionCards
import com.cyberwarriers.zubzub.feature.home.presentation.ui.components.MyGroupItem

@Composable
fun HomeScreen() {
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
                title = "홈"
            )

            GroupActionCards(
                onCreateGroupClick = { },
                onEnterGroupClick = { },
            )

            MyGroupList()
        }
    }
}

@Composable
private fun MyGroupList(
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "나의 장바구니 그룹",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
        )

        Text(
            text = "모두 보기",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Bottom)
        )
    }

    LazyColumn {
        item {
            MyGroupItem()
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

@Preview
@Composable
fun MyGroupItemPreview() {
    ZubZubTheme {
        MyGroupItem()
    }
}