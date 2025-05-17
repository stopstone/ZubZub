package com.cyberwarriers.zubzub.feature.main.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyberwarriers.zubzub.core.navigation.BottomNavItem

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {

        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen()
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
fun HomeScreen() {
    ScreenContent(text = "홈 화면")
}

@Composable
fun SecondScreen() {
    ScreenContent(text = "두번째 화면")
}

@Composable
fun ThirdScreen() {
    ScreenContent(text = "세번째 화면")
}

@Composable
fun ScreenContent(text: String) {
    Box(
        modifier = Modifier.padding(16.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}