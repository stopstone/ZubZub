package com.cyberwarriers.zubzub.feature.main.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
fun MainScreen(
    onNavigateToGroupCreate: () -> Unit = {},
    onNavigateToGroupEnter: () -> Unit = {},
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
                    onNavigateToGroupEnter = onNavigateToGroupEnter
                )
            }

            composable(BottomNavItem.Second.route) {
                SecondScreen()
            }

            composable(BottomNavItem.Third.route) {
                ThirdScreen()
            }
        }
    }
}

@Composable
fun SecondScreen() {
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
                title = "두번째 화면",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = { /* TODO: 뒤로가기 처리 */ },
                actions = {
                    IconButton(onClick = { /* TODO: 설정 클릭 처리 */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "설정"
                        )
                    }
                }
            )

            Text(
                text = "두번째 화면",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
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