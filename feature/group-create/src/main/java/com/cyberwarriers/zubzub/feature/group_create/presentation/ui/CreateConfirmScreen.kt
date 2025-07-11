package com.cyberwarriers.zubzub.feature.group_create.presentation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwarriers.zubzub.core.ui.components.ZubZubSubmitButton
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.group_create.R
import com.cyberwarriers.zubzub.feature.group_create.presentation.CreateConfirmViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateConfirmScreen(
    onNavigateToHome: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    viewModel: CreateConfirmViewModel = hiltViewModel(),
) {
    val groupId by viewModel.groupId.collectAsState()

    BackHandler {
        onBackPressed()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .systemBarsPadding(),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                
                Spacer(modifier = Modifier.weight(1f))
                
                Image(
                    modifier = Modifier.size(192.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF388E3C)),
                    painter = painterResource(R.drawable.ic_check_circle_48px),
                    contentDescription = "그룹 생성 성공",
                )

                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = "코드를 복사해서\n멤버들을 초대해보세요!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                )

                Spacer(modifier = Modifier.size(24.dp))

                InviteCopyField(
                    groupId = groupId,
                )

                Spacer(modifier = Modifier.weight(1f))

                ZubZubSubmitButton(
                    text = "완료",
                    onClick = onNavigateToHome,
                )
                
                Spacer(modifier = Modifier.size(56.dp))
            }
        }
    }
}

@Composable
private fun InviteCopyField(
    groupId: String,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = groupId,
        onValueChange = { },
        enabled = false,
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        label = { Text("그룹 초대 코드") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.None
        ),
        interactionSource = interactionSource,
        trailingIcon = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        clipboardManager.setText(AnnotatedString(groupId))
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF388E3C),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(40.dp)
            ) {
                Text(
                    text = "복사",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun CreateConfirmScreenPreview() {
    ZubZubTheme {
        CreateConfirmScreen(
            onNavigateToHome = {},
            onBackPressed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InviteCopyFieldPreview() {

    ZubZubTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            InviteCopyField(
                groupId = "asdasdasasdasdasasdasdasasdasdasasdasdaasdsadsadsads",
            )
        }
    }
}