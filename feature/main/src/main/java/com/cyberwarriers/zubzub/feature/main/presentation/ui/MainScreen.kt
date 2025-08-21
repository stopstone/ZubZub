package com.cyberwarriers.zubzub.feature.main.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cyberwarriers.zubzub.core.navigation.BottomNavItem
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.home.presentation.ui.HomeScreen
import com.cyberwarriers.zubzub.feature.profile.presentation.ui.ProfileScreen

@Composable
fun MainScreen(
    onNavigateToGroupCreate: () -> Unit = {},
    onNavigateToGroupEnter: () -> Unit = {},
    onNavigateToGroupCart: (String) -> Unit = {},
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = currentBackStackEntry?.destination?.route

                BottomNavItem.items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            logd("바텀 네비게이션 클릭: ${item.label}")
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    onNavigateToGroupCreate = onNavigateToGroupCreate,
                    onNavigateToGroupEnter = onNavigateToGroupEnter,
                    onNavigateToGroupCart = onNavigateToGroupCart,
                )
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onProfileClick = { profileId ->
                        logd("프로필 클릭: $profileId")
                        // TODO: 프로필 상세 화면으로 이동
                    },
                    onAddProfileClick = {
                        logd("프로필 추가 클릭")
                        // TODO: 프로필 생성 화면으로 이동
                    }
                )
            }

            composable(BottomNavItem.Third.route) {
                ThirdScreen()
            }
        }
    }
}

@Composable
fun ThirdScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            ZubZubTopAppBar(
                title = "세번째 화면",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = { /* TODO: 뒤로가기 처리 */ }
            )

            Text(
                text = "세번째 화면",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}