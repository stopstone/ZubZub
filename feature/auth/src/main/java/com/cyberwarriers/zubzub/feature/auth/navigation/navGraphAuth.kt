package com.cyberwarriers.zubzub.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.auth.presentation.ui.LoginScreen

fun NavGraphBuilder.navGraphAuth(navController: NavController) {
    composable(route = Route.Login) {
        LoginScreen(
            onNavigateToHome = {
                // 홈 화면 경로가 정의되면 그곳으로 이동
                // navController.navigate(Route.Home) {
                //     popUpTo(Route.Login) { inclusive = true }
                // }
                logd("홈 화면으로 이동 (아직 구현되지 않음)")
            }
        )
    }
}