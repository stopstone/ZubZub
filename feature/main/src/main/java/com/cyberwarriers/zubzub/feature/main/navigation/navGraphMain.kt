package com.cyberwarriers.zubzub.feature.main.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.main.presentation.ui.MainScreen

fun NavGraphBuilder.navGraphMain(navController: NavController) {
    composable(
        route = Route.Main,
    ) {
        LaunchedEffect(Unit) {
            logd("메인 화면 진입")
        }
        
        MainScreen()
    }
}