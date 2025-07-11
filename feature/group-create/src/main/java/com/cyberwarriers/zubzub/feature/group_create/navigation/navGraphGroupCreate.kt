package com.cyberwarriers.zubzub.feature.group_create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_create.presentation.ui.GroupCreateScreen
import com.cyberwarriers.zubzub.feature.group_create.presentation.ui.CreateConfirmScreen

fun NavGraphBuilder.navGraphGroupCreate(navController: NavController) {
    composable(
        route = Route.GroupCreate,
    ) {
        GroupCreateScreen(
            onNavigatePop = {
                navController.popBackStack()
                logd("그룹 생성 화면 나가기")
            },
            onNavigateToConfirm = { groupId ->
                navController.navigate(Route.createConfirm(groupId))
                logd("그룹 생성 확인 화면으로 이동: $groupId")
            }
        )
    }
    
    composable(
        route = Route.CreateConfirm,
        arguments = listOf(navArgument("groupId") { type = NavType.StringType })
    ) { backStackEntry ->
        CreateConfirmScreen(
            onNavigateToHome = {
                navController.popBackStack(Route.Main, inclusive = false)
                logd("그룹 생성 완료 - 홈화면으로 이동")
            },
            onBackPressed = {
                navController.popBackStack(Route.Main, inclusive = false)
                logd("그룹 생성 확인 화면에서 뒤로가기 - 홈화면으로 이동")
            }
        )
    }
} 