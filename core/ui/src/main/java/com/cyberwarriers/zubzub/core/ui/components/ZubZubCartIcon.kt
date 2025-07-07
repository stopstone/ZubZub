package com.cyberwarriers.zubzub.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.cyberwarriers.zubzub.core.ui.R

@Composable
fun ZubZubCartIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(R.drawable.ic_logo_cart),
        contentDescription = "앱 로고",
        tint = Color.Unspecified,
        modifier = modifier.alpha(0.8f),
    )
}