package com.cyberwarriers.zubzub.feature.profile.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.presentation.ui.ProfileCreateScreen
import com.cyberwarriers.zubzub.feature.profile.presentation.ui.ImagePickerScreen

/**
 * 프로필 관련 네비게이션 그래프
 */
fun NavGraphBuilder.navGraphProfile(navController: NavController) {
    composable(
        route = Route.ProfileCreate,
        arguments = listOf(
            navArgument("selectedImageUri") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        // Navigation Arguments에서 선택된 이미지 URI 가져오기
        val selectedImageUri = backStackEntry.arguments?.getString("selectedImageUri")
        
        logd("ProfileCreate - Navigation Arguments에서 받은 이미지: '$selectedImageUri'")
        
        ProfileCreateScreen(
            selectedImageUri = selectedImageUri,
            onNavigateToHome = {
                navController.navigate(Route.Main) {
                    popUpTo(Route.ProfileCreate) { inclusive = true }
                }
                logd("프로필 생성 완료 - 홈 화면으로 이동")
            },
            onNavigateToImagePicker = { currentProfileImageUri ->
                // Navigation Arguments를 사용하여 ImagePicker로 이동
                navController.navigate(Route.imagePicker(currentProfileImageUri))
                logd("이미지 선택 화면으로 이동 - 현재 이미지: $currentProfileImageUri")
            },
        )
    }
    
    // 이미지 선택 화면
    composable(
        route = Route.ImagePicker,
        arguments = listOf(
            navArgument("currentImageUri") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        LaunchedEffect(Unit) {
            logd("이미지 선택 화면 진입")
        }
        
        // Navigation Arguments에서 현재 선택된 이미지 URI 가져오기
        val currentSelectedImageUri = backStackEntry.arguments?.getString("currentImageUri")
        
        logd("ImagePicker - Navigation Arguments에서 받은 현재 이미지: '$currentSelectedImageUri'")
        
        ImagePickerScreen(
            currentSelectedImageUri = currentSelectedImageUri,
            onImageSelected = { selectedImageUri ->
                // Navigation Arguments를 사용하여 이전 화면으로 결과 전달
                logd("이미지 선택 완료 - 전달할 URI: $selectedImageUri")
                navController.navigate(Route.profileCreate(selectedImageUri)) {
                    popUpTo(Route.ProfileCreate) { inclusive = true }
                }
                logd("이미지 선택 완료 및 ProfileCreate로 이동")
            },
            onBackClick = {
                navController.popBackStack()
                logd("이미지 선택 취소")
            },
        )
    }
}
