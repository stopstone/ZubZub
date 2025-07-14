package com.cyberwarriers.zubzub.feature.group_cart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.GroupCartScreen

fun NavGraphBuilder.navGroupCartGraph(
    navController: NavController,
) {
    composable(
        route = Route.GroupCart,
    ) {
        GroupCartScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}