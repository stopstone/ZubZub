package com.cyberwarriers.zubzub.feature.auth.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.auth.presentation.LoginViewModel
import com.cyberwarriers.zubzub.feature.auth.presentation.effect.LoginEffect
import com.cyberwarriers.zubzub.feature.auth.presentation.state.LoginState

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState(initial = null)

    // Effect 처리
    LaunchedEffect(effect) {
        when (effect) {
            LoginEffect.NavigateToHome -> {
                logd("홈 화면으로 이동")
                onNavigateToHome()
                viewModel.consumeEffect()
            }
            LoginEffect.ShowLoginFailed -> {
                logd("로그인 실패 알림")
                viewModel.consumeEffect()
            }
            null -> { /* 효과 없음 */ }
        }
    }

    LoginContent()
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier
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
                text = "로그인",
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
fun LoginContentPreview() {
    ZubZubTheme {
        LoginContent()
    }
}