package com.cyberwarriers.zubzub.feature.profile.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.presentation.ui.ProfileCreateScreen
import com.cyberwarriers.zubzub.feature.profile.presentation.ui.ImagePickerScreen

/**
 * 프로필 관련 네비게이션 그래프
 */
fun NavGraphBuilder.navGraphProfile(navController: NavController) {
    composable(
        route = Route.ProfileCreate
    ) { backStackEntry ->
        LaunchedEffect(Unit) {
            logd("프로필 생성 화면 진입")
        }
        
        val selectedImageUri = backStackEntry.savedStateHandle.get<String>("selected_image_uri")
        
        ProfileCreateScreen(
            onNavigateToHome = {
                navController.navigate(Route.Main) {
                    popUpTo(Route.ProfileCreate) { inclusive = true }
                }
                logd("프로필 생성 완료 - 홈 화면으로 이동")
            },
            onNavigateToImagePicker = {
                navController.navigate(Route.ImagePicker)
                logd("이미지 선택 화면으로 이동")
            },
            selectedImageUri = selectedImageUri,
        )
        
        // 사용 후 제거
        LaunchedEffect(selectedImageUri) {
            selectedImageUri?.let { _ ->
                backStackEntry.savedStateHandle.remove<String>("selected_image_uri")
            }
        }
    }
    
    // 이미지 선택 화면
    composable(
        route = Route.ImagePicker
    ) {
        LaunchedEffect(Unit) {
            logd("이미지 선택 화면 진입")
        }
        
        ImagePickerScreen(
            onImageSelected = { selectedImageUri ->
                // 선택된 이미지 URI를 이전 화면으로 전달
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("selected_image_uri", selectedImageUri)
                
                navController.popBackStack()
                logd("이미지 선택 완료: $selectedImageUri")
            },
            onBackClick = {
                navController.popBackStack()
                logd("이미지 선택 취소")
            },
        )
    }
}
