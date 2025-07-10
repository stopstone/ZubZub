package com.cyberwarriers.zubzub.feature.group_create.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwarriers.zubzub.core.ui.components.ZubZubInputTextField
import com.cyberwarriers.zubzub.core.ui.components.ZubZubSubmitButton
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.group_create.presentation.GroupCreateViewModel
import com.cyberwarriers.zubzub.feature.group_create.presentation.state.GroupCreateUiState

/**
 * 그룹 생성 화면
 */
@Composable
fun GroupCreateScreen(
    onNavigatePop: () -> Unit = {},
    viewModel: GroupCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            // 상단 앱바
            ZubZubTopAppBar(
                title = "그룹 생성",
                navigationIcon = Icons.Default.Close,
                onNavigationClick = onNavigatePop,
            )

            // 메인 컨텐츠
            if (uiState.isLoading) {
                LoadingContent()
            } else {
                GroupCreateContent(
                    uiState = uiState,
                    onGroupNameChanged = viewModel::onGroupNameChanged,
                    onCreateGroupClicked = viewModel::onCreateGroupClicked
                )
            }

            ZubZubSubmitButton(
                text = "그룹 만들기",
                onClick =viewModel::onCreateGroupClicked,
                enable = uiState.isCreateButtonEnabled,
            )
        }
    }
}

@Composable
private fun GroupCreateContent(
    uiState: GroupCreateUiState,
    onGroupNameChanged: (String) -> Unit,
    onCreateGroupClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 20.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 안내 텍스트
        Text(
            text = "새로운 장바구니 그룹을 \n만들어보세요!",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 26.dp)
        )

        // 그룹 이름 입력
        ZubZubInputTextField(
            value = uiState.groupName,
            onValueChange = onGroupNameChanged,
            label = "그룹명",
            errorMessage = uiState.groupNameError,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        // 그룹 생성 버튼
        Button(
            onClick = onCreateGroupClicked,
            enabled = uiState.isCreateButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "그룹 생성하기",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
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

@Preview(showBackground = true)
@Composable
fun LoadingContentPreview() {
    ZubZubTheme {
        LoadingContent()
    }
}