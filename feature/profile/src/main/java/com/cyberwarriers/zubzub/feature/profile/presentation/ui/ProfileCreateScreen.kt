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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.presentation.ProfileCreateViewModel
import com.cyberwarriers.zubzub.feature.profile.presentation.components.ProfileImageSelector
import com.cyberwarriers.zubzub.feature.profile.presentation.effect.ProfileCreateEffect
import com.cyberwarriers.zubzub.feature.profile.presentation.state.ProfileCreateState
import com.cyberwarriers.zubzub.feature.profile.presentation.state.ProfileCreateUiState

/**
 * 프로필 생성 화면
 */
@Composable
fun ProfileCreateScreen(
    selectedImageUri: String? = null,
    onNavigateToHome: () -> Unit = {},
    onNavigateToImagePicker: (String?) -> Unit = {},
    viewModel: ProfileCreateViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle()
    
    // Navigation Arguments에서 선택된 이미지 URI가 변경되면 ViewModel 업데이트
    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { uri ->
            if (uri.isNotEmpty()) {
                logd("Navigation Arguments에서 ViewModel로 이미지 URI 전달: $uri")
                viewModel.updateProfileImageUrl(uri)
            }
        }
    }

    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            logd("갤러리 권한 허용됨 - 이미지 선택 화면으로 이동")
            // 현재 선택된 이미지 URI를 전달
            onNavigateToImagePicker(uiState.profileImageUrl.takeIf { it.isNotEmpty() })
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
                // 현재 선택된 이미지 URI를 전달
                onNavigateToImagePicker(uiState.profileImageUrl.takeIf { it.isNotEmpty() })
            }
            else -> {
                logd("갤러리 권한 요청")
                permissionLauncher.launch(permission)
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
    }
    
    // 로딩 상태 처리
    if (state is ProfileCreateState.Loading) {
        ZubZubLoadingProgress()
        return
    }
    
    ProfileCreateContent(
        uiState = uiState,
        state = state,
        onImageClick = {
            logd("프로필 이미지 클릭 - 권한 체크 시작, 현재 이미지: ${uiState.profileImageUrl}")
            checkAndRequestPermission()
        },
        onProfileNameChange = viewModel::updateProfileName,
        onCreateClick = {
            logd("프로필 생성 버튼 클릭")
            viewModel.createProfile()
        }
    )
}

/**
 * 프로필 생성 화면 Content
 * ViewModel 없이 State만으로 구성하여 프리뷰 지원
 */
@Composable
fun ProfileCreateContent(
    uiState: ProfileCreateUiState,
    state: ProfileCreateState,
    onImageClick: () -> Unit = {},
    onProfileNameChange: (String) -> Unit = {},
    onCreateClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            ZubZubTopAppBar(
                title = "프로필 생성"
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
                onImageClick = onImageClick
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 프로필 이름 입력
            ZubZubInputTextField(
                value = uiState.profileName,
                onValueChange = onProfileNameChange,
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
                        text = state.message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // 프로필 생성 버튼
            ZubZubSubmitButton(
                text = "프로필 생성",
                onClick = onCreateClick,
                enable = uiState.isSubmitButtonEnabled(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, name = "기본 상태")
@Composable
fun ProfileCreateContentPreview() {
    ZubZubTheme {
        ProfileCreateContent(
            uiState = ProfileCreateUiState(),
            state = ProfileCreateState.Initial
        )
    }
}

@Preview(showBackground = true, name = "입력된 상태")
@Composable
fun ProfileCreateContentFilledPreview() {
    ZubZubTheme {
        ProfileCreateContent(
            uiState = ProfileCreateUiState(
                profileName = "김개발자",
                profileImageUrl = "https://example.com/profile.jpg"
            ),
            state = ProfileCreateState.Initial
        )
    }
}

@Preview(showBackground = true, name = "에러 상태")
@Composable
fun ProfileCreateContentErrorPreview() {
    ZubZubTheme {
        ProfileCreateContent(
            uiState = ProfileCreateUiState(
                profileName = "아주아주아주아주아주긴이름이스무글자넘음",
                isProfileNameError = true,
                profileNameErrorMessage = "프로필 이름은 20글자 이하로 입력해주세요."
            ),
            state = ProfileCreateState.Error("네트워크 오류가 발생했습니다.")
        )
    }
}

@Preview(showBackground = true, name = "전체 화면")
@Composable
fun ProfileCreateScreenPreview() {
    ZubZubTheme {
        ProfileCreateScreen()
    }
}
