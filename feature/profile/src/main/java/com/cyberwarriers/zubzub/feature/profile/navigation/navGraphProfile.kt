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
        route = Route.ProfileCreate
    ) { backStackEntry ->
        // SavedStateHandle에서 이미지 URI 가져오기 (이미지 선택 후 돌아올 때)
        val savedImageUri = backStackEntry.savedStateHandle.get<String>("selectedImageUri")
        
        // 이미지가 선택되었으면 로그 출력하고 SavedStateHandle에서 제거
        savedImageUri?.let { imageUri ->
            logd("ProfileCreate - 선택된 이미지 URI: '$imageUri'")
            backStackEntry.savedStateHandle.remove<String>("selectedImageUri")
        }
        
        ProfileCreateScreen(
            selectedImageUri = savedImageUri,
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
                // 이전 화면으로 결과 전달하고 돌아가기
                logd("이미지 선택 완료 - 전달할 URI: $selectedImageUri")
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedImageUri", selectedImageUri)
                navController.popBackStack()
                logd("이미지 선택 완료 및 이전 화면으로 돌아가기")
            },
            onBackClick = {
                navController.popBackStack()
                logd("이미지 선택 취소")
            },
        )
    }
}
