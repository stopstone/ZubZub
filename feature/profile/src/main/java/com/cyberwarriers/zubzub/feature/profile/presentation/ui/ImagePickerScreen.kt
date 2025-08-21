package com.cyberwarriers.zubzub.feature.profile.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.rememberAsyncImagePainter
import com.cyberwarriers.zubzub.core.ui.components.ZubZubLoadingProgress
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.data.datasource.GalleryPermissionException
import com.cyberwarriers.zubzub.feature.profile.presentation.ImagePickerViewModel

/**
 * 페이징 적용된 이미지 선택 화면
 * 
 * 주요 기능:
 * - Paging 3를 사용한 메모리 효율적인 갤러리 로드
 * - 권한 관리 및 에러 처리
 * - 로딩 상태 및 사용자 피드백
 * - Clean Architecture 적용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerScreen(
    currentSelectedImageUri: String? = null,
    onImageSelected: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: ImagePickerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    
    // ViewModel 상태 관찰
    val selectedImageUri by viewModel.selectedImageUri.collectAsStateWithLifecycle()
    val hasPermission by viewModel.hasPermission.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    
    // 페이징 데이터
    val lazyPagingItems = viewModel.galleryImages.collectAsLazyPagingItems()
    
    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionResult(granted)
        if (granted) {
            logd("갤러리 권한 승인됨")
        } else {
            logd("갤러리 권한 거부됨")
        }
    }
    
    // 권한 확인 및 요청 함수
    fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        when (ContextCompat.checkSelfPermission(context, permission)) {
            PackageManager.PERMISSION_GRANTED -> {
                viewModel.onPermissionResult(true)
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }
    
    // 권한이 없으면 권한 요청
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            checkAndRequestPermission()
        }
    }
    
    // 현재 선택된 이미지가 있으면 설정, 없으면 첫 번째 이미지를 기본 선택
    LaunchedEffect(currentSelectedImageUri, lazyPagingItems.itemCount) {
        if (hasPermission) {
            if (currentSelectedImageUri != null && currentSelectedImageUri.isNotEmpty()) {
                // 현재 선택된 이미지가 있으면 해당 이미지를 선택
                viewModel.selectImage(currentSelectedImageUri)
                logd("현재 선택된 이미지 적용: $currentSelectedImageUri")
            } else if (lazyPagingItems.itemCount > 0 && selectedImageUri == null) {
                // 선택된 이미지가 없으면 첫 번째 이미지를 기본 선택
                lazyPagingItems.peek(0)?.let { firstImage ->
                    viewModel.setFirstImageAsDefault(firstImage)
                    logd("첫 번째 이미지를 기본 선택으로 설정: $firstImage")
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "이미지 선택",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBackClick,
                    ) {
                        Text(
                            text = "취소",
                            fontSize = 16.sp,
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            selectedImageUri?.let { uri ->
                                onImageSelected(uri)
                                logd("이미지 선택 완료: $uri")
                            }
                        },
                        enabled = selectedImageUri != null && hasPermission,
                    ) {
                        Text(
                            text = "완료",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        
        // 권한이 없는 경우
        if (!hasPermission) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "갤러리 접근 권한이 필요합니다",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "프로필 이미지를 선택하기 위해\n갤러리 접근 권한을 허용해주세요.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    Button(
                        onClick = { checkAndRequestPermission() }
                    ) {
                        Text("권한 요청")
                    }
                }
            }
            return@Scaffold
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // 상단: 선택된 이미지 미리보기 (화면의 절반)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(Color.Black),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    // 선택된 이미지가 있는 경우
                    selectedImageUri != null -> {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "선택된 이미지",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )
                    }
                    // 초기 로딩 중인 경우
                    lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(color = Color.White)
                            Text(
                                text = "갤러리를 불러오는 중...",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                    // 기본 상태
                    else -> {
                        Text(
                            text = "이미지를 선택해주세요",
                            color = Color.White,
                            fontSize = 18.sp,
                        )
                    }
                }
            }
            
            // 하단: 페이징된 갤러리 이미지 그리드 (화면의 절반)
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    // 초기 로딩 에러
                    lazyPagingItems.loadState.refresh is LoadState.Error -> {
                        val error = lazyPagingItems.loadState.refresh as LoadState.Error
                        ErrorContent(
                            message = when (error.error) {
                                is GalleryPermissionException -> "갤러리 접근 권한이 필요합니다"
                                else -> "갤러리를 불러올 수 없습니다"
                            },
                            onRetry = { lazyPagingItems.retry() }
                        )
                    }
                    // 정상 로딩
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = lazyPagingItems.itemKey { it }
                            ) { index ->
                                val imageUri = lazyPagingItems[index]
                                imageUri?.let {
                                    GalleryImageItem(
                                        imageUri = it,
                                        isSelected = selectedImageUri == it,
                                        onImageClick = { uri ->
                                            viewModel.selectImage(uri)
                                        },
                                    )
                                }
                            }
                            
                            // 추가 로딩 상태 처리
                            when (lazyPagingItems.loadState.append) {
                                is LoadState.Loading -> {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                                is LoadState.Error -> {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "추가 이미지 로드 실패",
                                                    color = MaterialTheme.colorScheme.error,
                                                    fontSize = 12.sp
                                                )
                                                TextButton(
                                                    onClick = { lazyPagingItems.retry() }
                                                ) {
                                                    Text("재시도", fontSize = 12.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }
                
                // 에러 메시지 스낵바
                errorMessage?.let { message ->
                    LaunchedEffect(message) {
                        // 에러 메시지를 표시한 후 자동으로 제거
                        kotlinx.coroutines.delay(3000)
                        viewModel.clearError()
                    }
                }
            }
        }
    }
}

/**
 * 에러 상태 표시 컴포넌트
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            
            Button(onClick = onRetry) {
                Text("다시 시도")
            }
        }
    }
}

/**
 * 갤러리 이미지 아이템 (기존과 동일하지만 최적화)
 */
@Composable
private fun GalleryImageItem(
    imageUri: String,
    isSelected: Boolean,
    onImageClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onImageClick(imageUri) },
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = imageUri,
                onError = { 
                    logd("이미지 로드 실패: $imageUri") 
                }
            ),
            contentDescription = "갤러리 이미지",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        
        // 선택된 이미지 표시
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp),
                    ),
            )
            
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(20.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(10.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✓",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImagePickerScreenPreview() {
    ZubZubTheme {
        ImagePickerScreen()
    }
}