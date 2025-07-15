package com.cyberwarriers.zubzub.feature.group_cart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.GroupCartScreen

fun NavGraphBuilder.navGroupCartGraph(
    navController: NavController,
) {
    composable(
        route = Route.GroupCart,
        arguments = listOf(navArgument("groupId") { type = NavType.StringType })
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
        
        logd("그룹 카트 화면 진입: $groupId")
        
        GroupCartScreen(
            onNavigateBack = {
                navController.popBackStack()
                logd("그룹 카트 화면에서 뒤로가기")
            }
        )
    }
}