package com.cyberwarriers.zubzub.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme

/**
 * 프로필 이미지 선택 컴포넌트
 */
@Composable
fun ProfileImageSelector(
    profileImageUrl: String,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        // 프로필 이미지 영역
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
                .background(Color(0xFFF5F5F5))
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUrl.isNotEmpty()) {
                // 선택된 이미지 표시
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "프로필 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // 기본 아이콘 표시
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "기본 프로필 이미지",
                    modifier = Modifier.size(80.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileImageSelectorPreview() {
    ZubZubTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileImageSelector(
                profileImageUrl = "",
                onImageClick = {}
            )
            
            ProfileImageSelector(
                profileImageUrl = "https://via.placeholder.com/120",
                onImageClick = {}
            )
        }
    }
}
