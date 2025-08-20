package com.cyberwarriers.zubzub.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        modifier = modifier
    ) {
        // 프로필 이미지 레이블
        Text(
            text = "프로필 이미지",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // 프로필 이미지 영역
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
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
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 카메라 아이콘 오버레이
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "이미지 선택",
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
        }
        
        // 안내 텍스트
        Text(
            text = "탭하여 이미지 선택 (선택사항)",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
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
