package com.cyberwarriers.zubzub.feature.group_create.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.cyberwarriers.zubzub.core.ui.components.ZubZubLoadingProgress
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

    // 그룹 생성 성공 시 화면 닫기
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            onNavigatePop()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {

            // 메인 컨텐츠
            if (uiState.isLoading) {
                ZubZubLoadingProgress()
            } else {
                ZubZubTopAppBar(
                    title = "그룹 생성",
                    navigationIcon = Icons.Default.Close,
                    onNavigationClick = onNavigatePop,
                )

                GroupCreateContent(
                    uiState = uiState,
                    onGroupNameChanged = viewModel::onGroupNameChanged,
                    onCreateGroupClicked = viewModel::onCreateGroupClicked
                )
            }

            ZubZubSubmitButton(
                text = "그룹 만들기",
                onClick = viewModel::onCreateGroupClicked,
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
    // 포커스 요청을 위한 FocusRequester
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 화면 진입 시 자동 포커스 및 키보드 올리기
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // 화면 나갈 때 키보드 숨기기
    DisposableEffect(Unit) {
        onDispose {
            keyboardController?.hide()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
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
            isError = uiState.groupNameError.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        Spacer(modifier = Modifier.size(48.dp))

        // 그룹 생성 버튼
        ZubZubSubmitButton(
            text = "그룹 만들기",
            onClick = onCreateGroupClicked,
            enable = uiState.isCreateButtonEnabled,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupCreateScreenPreview() {
    ZubZubTheme {
        GroupCreateScreen()
    }
}