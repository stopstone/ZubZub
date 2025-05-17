package com.cyberwarriers.zubzub.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyberwarriers.zubzub.core.navigation.Route.BottomNav

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
) {
    object Home: BottomNavItem(
        route = BottomNav.Home,
        icon = Icons.Default.Home,
        label = "Home",
    )

    object Second: BottomNavItem(
        route = BottomNav.Second,
        icon = Icons.Default.AddCircle,
        label = "Second",
    )

    object Third: BottomNavItem(
        route = BottomNav.Third,
        icon = Icons.Default.AddCircle,
        label = "Third",
    )

}