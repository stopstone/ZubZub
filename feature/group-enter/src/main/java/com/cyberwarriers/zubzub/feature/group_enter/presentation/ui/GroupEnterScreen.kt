package com.cyberwarriers.zubzub.feature.group_enter.presentation.ui

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwarriers.zubzub.core.ui.components.ZubZubInputTextField
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.group_enter.presentation.GroupEnterViewModel
import com.cyberwarriers.zubzub.feature.group_enter.presentation.state.GroupEnterUiState

/**
 * 그룹 입장 코드 입력 화면
 */
@Composable
fun GroupEnterScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToConfirm: (String) -> Unit = {},
    viewModel: GroupEnterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(uiState.navigateToConfirm) {
        uiState.navigateToConfirm?.let { groupId ->
            keyboardController?.hide()
            onNavigateToConfirm(groupId)
            viewModel.onNavigationHandled()
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
                title = "그룹 입장",
                navigationIcon = Icons.Default.Close,
                onNavigationClick = onNavigateBack,
            )

            GroupEnterContent(
                uiState = uiState,
                focusRequester = focusRequester,
                onInviteCodeChanged = viewModel::onInviteCodeChanged,
                onVerifyCodeClicked = viewModel::onVerifyCodeClicked
            )
        }
    }
}

@Composable
private fun GroupEnterContent(
    uiState: GroupEnterUiState,
    focusRequester: FocusRequester,
    onInviteCodeChanged: (String) -> Unit,
    onVerifyCodeClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 안내 텍스트
        Text(
            text = "그룹 초대 코드를\n입력해주세요",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
        )

        // 설명 텍스트
        Text(
            text = "그룹장에게 받은 초대 코드를 입력하면\n해당 그룹의 정보를 확인할 수 있습니다.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // 초대 코드 입력
        ZubZubInputTextField(
            value = uiState.inviteCode,
            onValueChange = onInviteCodeChanged,
            label = "초대 코드",
            placeholder = "예시: 8RpUD4FQ9hj91mcGZx4",
            errorMessage = uiState.inviteCodeError,
            isError = uiState.inviteCodeError.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        Spacer(modifier = Modifier.size(32.dp))

        // 확인 버튼 (로딩 상태 지원)
        Button(
            onClick = onVerifyCodeClicked,
            enabled = uiState.inviteCode.isNotBlank() && !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF388E3C),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFC7C7C7),
                disabledContentColor = Color(0xFFACACAC),
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (uiState.isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
            } else {
                Text(
                    text = "그룹 확인하기",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun GroupEnterScreenPreview() {
    ZubZubTheme {
        GroupEnterScreen()
    }
} 