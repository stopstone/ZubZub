package com.cyberwarriers.zubzub.feature.profile.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.profile.presentation.ui.ProfileCreateScreen

/**
 * 프로필 관련 네비게이션 그래프
 */
fun NavGraphBuilder.navGraphProfile(navController: NavController) {
    composable(
        route = Route.ProfileCreate
    ) {
        LaunchedEffect(Unit) {
            logd("프로필 생성 화면 진입")
        }
        
        ProfileCreateScreen(
            onNavigateToHome = {
                navController.navigate(Route.Main) {
                    popUpTo(Route.ProfileCreate) { inclusive = true }
                }
                logd("프로필 생성 완료 - 홈 화면으로 이동")
            }
        )
    }
}
