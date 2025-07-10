package com.cyberwarriers.zubzub.feature.group_create.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_create.presentation.GroupCreateScreen

fun NavGraphBuilder.navGraphGroupCreate(navController: NavController) {
    composable(
        route = Route.GroupCreate,
    ) {
        GroupCreateScreen(
            onNavigateBack = {
                navController.popBackStack()
                logd("그룹 생성 화면에서 뒤로가기")
            }
        )
    }
} 