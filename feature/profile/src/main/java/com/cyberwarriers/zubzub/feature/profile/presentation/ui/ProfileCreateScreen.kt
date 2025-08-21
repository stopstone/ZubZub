package com.cyberwarriers.zubzub.feature.profile.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
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
    onNavigateToImagePicker: () -> Unit = {},
    selectedImageUri: String? = null,
    viewModel: ProfileCreateViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle()
    
    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            logd("갤러리 권한 허용됨 - 이미지 선택 화면으로 이동")
            onNavigateToImagePicker()
        } else {
            logd("갤러리 권한 거부됨")
        }
    }
    
    // 권한 체크 함수
    fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        when (ContextCompat.checkSelfPermission(context, permission)) {
            PackageManager.PERMISSION_GRANTED -> {
                logd("갤러리 권한 이미 허용됨 - 이미지 선택 화면으로 이동")
                onNavigateToImagePicker()
            }
            else -> {
                logd("갤러리 권한 요청")
                permissionLauncher.launch(permission)
            }
        }
    }
    
    // 선택된 이미지 URI가 변경되면 ViewModel 업데이트
    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { uri ->
            if (uri.isNotEmpty()) {
                viewModel.updateProfileImageUrl(uri)
                logd("선택된 이미지 적용: $uri")
            }
        }
    }
    
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
                        text = "프로필 생성",
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
                    logd("프로필 이미지 클릭 - 권한 체크 시작")
                    checkAndRequestPermission()
                },
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
