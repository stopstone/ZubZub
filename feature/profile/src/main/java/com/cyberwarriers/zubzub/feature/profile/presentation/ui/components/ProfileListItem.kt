package com.cyberwarriers.zubzub.feature.profile.presentation.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.cyberwarriers.zubzub.core.domain.model.UserProfile
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme

/**
 * 프로필 리스트 아이템 컴포넌트
 * 
 * 역할:
 * - 개별 프로필 정보 표시
 * - 클릭 시 리플 효과
 * - 프로필 사진, 닉네임, 사용중인 그룹명 표시
 */
@Composable
fun ProfileListItem(
    profile: UserProfile,
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
        onClick = { onClick(profile.profileId) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 프로필 이미지
                ProfileImage(
                    imageUrl = profile.profileImageUrl,
                    nickname = profile.nickname,
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.size(16.dp))
                
                // 프로필 정보
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = profile.nickname,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.size(4.dp))
                    
                    if (profile.currentGroupName != null) {
                        Text(
                            text = "사용중인 그룹: ${profile.currentGroupName}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "사용중인 그룹 없음",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // 활성 상태 표시
                if (profile.isActive) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}

/**
 * 원형 프로필 이미지 컴포넌트
 * 
 * @param imageUrl 프로필 이미지 URL (null인 경우 기본 아이콘 표시)
 * @param nickname 프로필 닉네임 (접근성을 위한 contentDescription)
 * @param modifier Modifier
 */
@Composable
private fun ProfileImage(
    imageUrl: String?,
    nickname: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                contentDescription = "${nickname}님의 프로필 이미지",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = null,
            )
        } else {
            // 기본 프로필 아이콘
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "${nickname}님의 기본 프로필 이미지",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun ProfileListItemPreview() {
    ZubZubTheme {
        ProfileListItem(
            profile = UserProfile(
                profileId = "profile-1",
                nickname = "김철수",
                profileImageUrl = "https://picsum.photos/200/200",
                currentGroupName = "가족 장바구니",
                isActive = true,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}

@Preview
@Composable
fun ProfileListItemInactivePreview() {
    ZubZubTheme {
        ProfileListItem(
            profile = UserProfile(
                profileId = "profile-2",
                nickname = "이영희",
                profileImageUrl = null,
                currentGroupName = null,
                isActive = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}
