package com.cyberwarriers.zubzub.feature.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.feature.splash.presentation.ui.SplashScreen

fun NavGraphBuilder.navGraphSplash(navController: NavController) {
    composable(route = Route.Splash.ROUTE) {
        SplashScreen(
            onNavigateToLogin = {
                navController.navigate(Route.Auth.LOGIN) {
                    popUpTo(Route.Splash.ROUTE) { inclusive = true }
                }
            }
        )
    }
}