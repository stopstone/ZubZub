package com.cyberwarriers.zubzub.feature.home.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.home.R

@Composable
fun GroupCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardText: String,
    cardImageRes: Int,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(98.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(cardImageRes),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(14.dp))
            Text(
                text = cardText,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Top)
            )
        }
    }
}

@Preview
@Composable
fun GroupCardPreview() {
    ZubZubTheme { 
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
            Spacer(modifier = Modifier.width(16.dp))
            GroupCard(
                onClick = {},
                modifier = Modifier.weight(1f),
                cardText = "그룹 입장",
                cardImageRes = R.drawable.ic_enter,
            )
        }
    }
}