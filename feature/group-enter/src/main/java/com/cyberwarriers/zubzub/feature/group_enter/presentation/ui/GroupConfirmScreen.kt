package com.cyberwarriers.zubzub.feature.group_enter.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwarriers.zubzub.core.ui.components.ZubZubLoadingProgress
import com.cyberwarriers.zubzub.core.ui.components.ZubZubSubmitButton
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.util.toKoreanDateString
import com.cyberwarriers.zubzub.feature.group_enter.domain.model.GroupInfo
import com.cyberwarriers.zubzub.feature.group_enter.presentation.GroupConfirmViewModel
import com.cyberwarriers.zubzub.feature.group_enter.presentation.effect.GroupConfirmEffect

/**
 * 그룹 정보 확인 화면
 */
@Composable
fun GroupConfirmScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToEnter: () -> Unit = {},
    onNavigateToGroup: (String) -> Unit = {},
    viewModel: GroupConfirmViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GroupConfirmEffect.NavigateToHome -> {
                    val groupInfo = uiState.groupInfo
                    groupInfo?.let {
                        onNavigateToGroup(it.groupId)
                    }
                }
                is GroupConfirmEffect.ShowError -> {
                    // TODO: 토스트 또는 스낵바로 에러 메시지 표시
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            // 상단 앱바
            ZubZubTopAppBar(
                title = "그룹 확인",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = onNavigateBack,
            )

            when {
                uiState.isLoading -> {
                    ZubZubLoadingProgress()
                }
                uiState.groupInfo != null -> {
                    GroupInfoContent(
                        groupInfo = uiState.groupInfo!!,
                        isJoining = uiState.isJoining,
                        onJoinClick = viewModel::onJoinGroupClicked,
                        onRetryClick = onNavigateToEnter
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupInfoContent(
    groupInfo: GroupInfo,
    isJoining: Boolean,
    onJoinClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 제목
        Text(
            text = "이 그룹이 맞나요?",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(16.dp))

        // 그룹 정보 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 그룹 아이콘과 이름
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "그룹",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = groupInfo.groupName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // 구분선
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(vertical = 8.dp)
                )

                // 그룹 정보들
                GroupInfoRow("멤버 수", "${groupInfo.memberCount}명")

                if (groupInfo.description.isNotEmpty()) {
                    GroupInfoRow("설명", groupInfo.description)
                }

                GroupInfoRow("생성일", groupInfo.createdAt.toKoreanDateString())
            }
        }

        Spacer(modifier = Modifier.size(24.dp))

        // 버튼들
        if (isJoining) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "그룹에 참여하는 중...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else if (groupInfo.isAlreadyMember) {
            // 이미 멤버인 경우
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ZubZubSubmitButton(
                    text = "이미 참여 중인 그룹입니다",
                    onClick = { },
                    enable = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "이미 이 그룹의 멤버입니다.\n홈 화면에서 그룹을 확인해보세요.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.size(16.dp))

                OutlinedButton(
                    onClick = onRetryClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "다시 입력하기",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            ZubZubSubmitButton(
                text = "이 그룹에 참여하기",
                onClick = onJoinClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.size(12.dp))

            OutlinedButton(
                onClick = onRetryClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "다시 입력하기",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun GroupInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.6f),
            textAlign = TextAlign.End
        )
    }
}