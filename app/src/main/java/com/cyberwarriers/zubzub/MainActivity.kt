package com.cyberwarriers.zubzub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.cyberwarriers.zubzub.core.navigation.Route
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.auth.navigation.navGraphAuth
import com.cyberwarriers.zubzub.feature.main.navigation.navGraphMain
import com.cyberwarriers.zubzub.feature.splash.navigation.navGraphSplash
import com.cyberwarriers.zubzub.feature.group_create.navigation.navGraphGroupCreate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZubZubTheme {
                ZubZubApp()
            }
        }
    }
}

@Composable
fun ZubZubApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.Splash,
    ) {
        navGraphSplash(navController)
        navGraphAuth(navController)
        navGraphMain(navController)
        navGraphGroupCreate(navController)
    }
}
