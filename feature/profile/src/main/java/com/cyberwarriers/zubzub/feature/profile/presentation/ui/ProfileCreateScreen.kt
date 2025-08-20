package com.cyberwarriers.zubzub.feature.profile.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyberwarriers.zubzub.core.ui.components.ZubZubInputTextField
import com.cyberwarriers.zubzub.core.ui.components.ZubZubLoadingProgress
import com.cyberwarriers.zubzub.core.ui.components.ZubZubSubmitButton
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.presentation.ProfileCreateViewModel
import com.cyberwarriers.zubzub.feature.profile.presentation.components.ProfileImageSelector
import com.cyberwarriers.zubzub.feature.profile.presentation.effect.ProfileCreateEffect
import com.cyberwarriers.zubzub.feature.profile.presentation.state.ProfileCreateState

/**
 * 프로필 생성 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreateScreen(
    onNavigateToHome: () -> Unit = {},
    viewModel: ProfileCreateViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle()
    
    // 이펙트 처리
    LaunchedEffect(effect) {
        when (effect) {
            ProfileCreateEffect.NavigateToHome -> {
                logd("프로필 생성 성공 - 홈으로 이동")
                onNavigateToHome()
            }
            ProfileCreateEffect.ShowCreateFailed -> {
                logd("프로필 생성 실패")
            }
            ProfileCreateEffect.ShowCreateSuccess -> {
                logd("프로필 생성 성공")
            }
            null -> {} // 이펙트 없음
        }
        
        // 이펙트 소비
        if (effect != null) {
            viewModel.consumeEffect()
        }
    }
    
    // 로딩 상태 처리
    if (state is ProfileCreateState.Loading) {
        ZubZubLoadingProgress()
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "프로필 설정",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // 안내 텍스트
            Text(
                text = "프로필을 설정해주세요",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "다른 사용자들이 보게 될\n프로필 정보를 입력해주세요",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // 프로필 이미지 선택
            ProfileImageSelector(
                profileImageUrl = uiState.profileImageUrl,
                onImageClick = {
                    // TODO: 이미지 선택 기능 구현
                    logd("프로필 이미지 선택 클릭")
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 프로필 이름 입력
            ZubZubInputTextField(
                value = uiState.profileName,
                onValueChange = viewModel::updateProfileName,
                label = "프로필 이름",
                placeholder = "프로필 이름을 입력하세요 (20글자 이하)",
                isError = uiState.isProfileNameError,
                errorMessage = uiState.profileNameErrorMessage,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            // 글자 수 표시
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${uiState.profileName.length}/20",
                    fontSize = 12.sp,
                    color = if (uiState.profileName.length > 20) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 에러 메시지 표시
            if (state is ProfileCreateState.Error) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = (state as ProfileCreateState.Error).message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // 프로필 생성 버튼
            ZubZubSubmitButton(
                text = "프로필 생성",
                onClick = {
                    logd("프로필 생성 버튼 클릭")
                    viewModel.createProfile()
                },
                enable = uiState.isSubmitButtonEnabled(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCreateScreenPreview() {
    ZubZubTheme {
        ProfileCreateScreen()
    }
}
