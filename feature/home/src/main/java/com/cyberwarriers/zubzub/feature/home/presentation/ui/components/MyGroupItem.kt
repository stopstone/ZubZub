package com.cyberwarriers.zubzub.feature.home.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberwarriers.zubzub.core.domain.model.CartGroupSummary
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.home.R

@Composable
fun MyGroupItem(
    group: CartGroupSummary,
    onClick: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = BorderStroke(1.dp, Color.Gray),
        onClick = { onClick(group.groupId) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = group.groupName,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.size(8.dp))

                Row {
                    Text("멤버 수: ${group.memberCount}명")
                    Spacer(modifier = Modifier.size(20.dp))
                    Text("진행률: ${group.progressPercentage}%")
                }

            }

            Icon(
                painter = painterResource(R.drawable.ic_front),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun MyGroupItemPreview() {
    ZubZubTheme {
        MyGroupItem(
            group = CartGroupSummary(
                groupId = "sample-group-id",
                groupName = "가족 장바구니",
                memberCount = 4,
                progressPercentage = 35,
                createdAt = System.currentTimeMillis(),
                isOwner = true,
                currentAmount = 35000,
                targetAmount = 100000
            )
        )
    }
}