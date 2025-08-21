package com.cyberwarriers.zubzub.feature.profile.presentation.ui

import android.content.ContentUris
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.core.util.logd

/**
 * 이미지 선택 화면
 * 
 * 상단에는 선택된 이미지를 큰 화면으로 보여주고,
 * 하단에는 갤러리의 이미지들을 3열 그리드로 표시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerScreen(
    onImageSelected: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var galleryImages by remember { mutableStateOf<List<String>>(emptyList()) }
    
    // 갤러리 이미지 로드 (권한은 이미 프로필 생성 화면에서 확인됨)
    LaunchedEffect(Unit) {
        loadGalleryImages(context) { images ->
            galleryImages = images
            if (images.isNotEmpty()) {
                selectedImageUri = images.first()
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
                            }
                        },
                        enabled = selectedImageUri != null,
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
                selectedImageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "선택된 이미지",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                    )
                } ?: run {
                    Text(
                        text = "이미지를 선택해주세요",
                        color = Color.White,
                        fontSize = 18.sp,
                    )
                }
            }
            
            // 하단: 갤러리 이미지 그리드 (화면의 절반)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(galleryImages) { imageUri ->
                        GalleryImageItem(
                            imageUri = imageUri,
                            isSelected = selectedImageUri == imageUri,
                            onImageClick = { selectedImageUri = it },
                        )
                    }
                }

        }
    }
}

/**
 * 갤러리 이미지 아이템
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
            painter = rememberAsyncImagePainter(imageUri),
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
                        Color.Blue.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp),
                    ),
            )
            
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(20.dp)
                    .background(
                        Color.Blue,
                        RoundedCornerShape(10.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

/**
 * 갤러리에서 이미지 목록을 로드하는 함수
 */
private fun loadGalleryImages(
    context: android.content.Context,
    onImagesLoaded: (List<String>) -> Unit,
) {
    try {
        val images = mutableListOf<String>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        
        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            sortOrder,
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            
            while (cursor.moveToNext() && images.size < 100) { // 최대 100개만 로드
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(uri, id)
                images.add(contentUri.toString())
            }
        }
        
        onImagesLoaded(images)
        logd("갤러리 이미지 로드 완료: ${images.size}개")
        
    } catch (e: Exception) {
        logd("갤러리 이미지 로드 실패: ${e.message}")
        onImagesLoaded(emptyList())
    }
}

@Preview(showBackground = true)
@Composable
fun ImagePickerScreenPreview() {
    ZubZubTheme {
        ImagePickerScreen()
    }
}