package com.cyberwarriers.zubzub.feature.group_enter.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.group_enter.presentation.ui.GroupConfirmScreen
import com.cyberwarriers.zubzub.feature.group_enter.presentation.ui.GroupEnterScreen

fun NavGraphBuilder.navGraphGroupEnter(navController: NavController) {
    // 그룹 입장 코드 입력 화면
    composable(
        route = Route.GroupEnter,
    ) {
        GroupEnterScreen(
            onNavigateBack = {
                navController.popBackStack()
                logd("그룹 입장 화면 나가기")
            },
            onNavigateToConfirm = { groupId ->
                navController.navigate(Route.groupConfirm(groupId))
                logd("그룹 확인 화면으로 이동: $groupId")
            }
        )
    }
    
    // 그룹 확인 화면
    composable(
        route = Route.GroupConfirm,
        arguments = listOf(navArgument("groupId") { type = NavType.StringType })
    ) { backStackEntry ->
        GroupConfirmScreen(
            onNavigateBack = {
                navController.popBackStack()
                logd("그룹 확인 화면에서 뒤로가기")
            },
            onNavigateToEnter = {
                navController.popBackStack(Route.GroupEnter, inclusive = false)
                logd("그룹 입장 화면으로 돌아가기")
            },
            onNavigateToGroup = { groupId ->
                // TODO: 그룹 상세 화면(cart 모듈)으로 이동
                navController.popBackStack(Route.Main, inclusive = false)
                logd("그룹 참여 완료 - 홈화면으로 이동: $groupId")
            }
        )
    }
} 