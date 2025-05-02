package com.cyberwarriers.zubzub.feature.splash.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.splash.presentation.effect.SplashEffect
import com.cyberwarriers.zubzub.feature.splash.presentation.state.SplashState
import com.cyberwarriers.zubzub.feature.splash.presentation.SplashViewModel

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState(initial = null)

    LaunchedEffect(effect) {
        when (effect) {
            SplashEffect.NavigateToLogin -> {
                logd("스플래시 화면 종료, 로그인 화면으로 이동")
                onNavigateToLogin()
            }
            null -> { /* 효과 없음 */ }
        }
    }

    // State에 따른 UI 렌더링
    when (state) {
        SplashState.Loading, SplashState.Loaded -> SplashContent()
    }
}

@Composable
fun SplashContent(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "스플래시",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashContentPreview() {
    ZubZubTheme {
        SplashContent()
    }
}