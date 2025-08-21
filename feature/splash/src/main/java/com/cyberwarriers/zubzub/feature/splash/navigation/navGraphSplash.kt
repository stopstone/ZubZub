package com.cyberwarriers.zubzub.feature.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.feature.splash.presentation.ui.SplashScreen

fun NavGraphBuilder.navGraphSplash(navController: NavController) {
    composable(route = Route.Splash) {
        SplashScreen(
            onNavigateToLogin = {
                navController.navigate(Route.Login) {
                    popUpTo(Route.Splash) { inclusive = true }
                }
            },
            onNavigateToHome = {
                navController.navigate(Route.Main) {
                    popUpTo(Route.Splash) { inclusive = true }
                }
            },
            onNavigateToProfileCreate = {
                navController.navigate(Route.ProfileCreate) {
                    popUpTo(Route.Splash) { inclusive = true }
                }
            }
        )
    }
}