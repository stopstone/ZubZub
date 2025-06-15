package com.cyberwarriers.zubzub.feature.home.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.home.R

@Composable
fun GroupActionCards(
    onCreateGroupClick: () -> Unit,
    onEnterGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        GroupCard(
            onClick = onCreateGroupClick,
            modifier = Modifier.weight(1f),
            cardText = "그룹 생성",
            cardImageRes = R.drawable.ic_add,
        )
        Spacer(modifier = Modifier.width(12.dp))
        GroupCard(
            onClick = onEnterGroupClick,
            modifier = Modifier.weight(1f),
            cardText = "그룹 입장",
            cardImageRes = R.drawable.ic_enter,
        )
    }
}

@Preview
@Composable
fun GroupActionCardsPreview() {
    ZubZubTheme {
        GroupActionCards(
            onCreateGroupClick = {},
            onEnterGroupClick = {}
        )
    }
} 