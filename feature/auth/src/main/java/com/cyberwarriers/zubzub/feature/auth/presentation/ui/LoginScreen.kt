package com.cyberwarriers.zubzub.feature.auth.presentation.ui

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwarriers.zubzub.core.ui.components.ZubZubCartIcon
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.core.util.logd
import com.cyberwarriers.zubzub.feature.auth.BuildConfig
import com.cyberwarriers.zubzub.feature.auth.presentation.LoginViewModel
import com.cyberwarriers.zubzub.feature.auth.presentation.components.GoogleSocialButton
import com.cyberwarriers.zubzub.feature.auth.presentation.effect.LoginEffect
import com.cyberwarriers.zubzub.feature.auth.presentation.state.LoginState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToProfileCreate: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState(initial = null)
    val context = LocalContext.current

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    // Google Sign-In 결과 처리
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    logd("Google 로그인 성공, 계정: ${account.email}")
                    viewModel.signInWithGoogle(it)
                }
            } catch (e: ApiException) {
                logd("Google Sign-In 실패: ${e.statusCode}, ${e.message}")
            }
        } else {
            logd("Google Sign-In 취소됨")
        }
    }

    // Effect 처리
    LaunchedEffect(effect) {
        when (effect) {
            LoginEffect.NavigateToHome -> {
                onNavigateToHome()
                viewModel.consumeEffect()
            }
            
            LoginEffect.NavigateToProfileCreate -> {
                onNavigateToProfileCreate()
                viewModel.consumeEffect()
            }

            LoginEffect.ShowLoginFailed -> {
                // 에러 처리 (Toast 또는 Snackbar)
                logd("로그인 실패 효과 처리됨")
                viewModel.consumeEffect()
            }

            null -> { /* 효과 없음 */
            }
        }
    }

    LoginContent(
        onLoginClick = {
            logd("Google 로그인 버튼 클릭")
            launcher.launch(googleSignInClient.signInIntent)
        },
        isLoading = state is LoginState.Loading,
    )
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                ZubZubCartIcon()
            }

            GoogleSocialButton(
                onClick = onLoginClick,
                enabled = true,
                isLoading = isLoading,
            )

            Spacer(modifier.size(104.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginContentPreview() {
    ZubZubTheme {
        LoginContent(
            onLoginClick = {},
            isLoading = false,
        )
    }
}