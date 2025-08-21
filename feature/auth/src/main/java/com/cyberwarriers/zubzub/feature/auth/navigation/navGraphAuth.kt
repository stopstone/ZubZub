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
                navController.navigate(Route.Main) {
                    popUpTo(Route.Login) { inclusive = true }
                }
                logd("홈 화면으로 이동")
            },
            onNavigateToProfileCreate = {
                navController.navigate(Route.ProfileCreate) {
                    popUpTo(Route.Login) { inclusive = true }
                }
                logd("프로필 생성 화면으로 이동")
            }
        )
    }
}