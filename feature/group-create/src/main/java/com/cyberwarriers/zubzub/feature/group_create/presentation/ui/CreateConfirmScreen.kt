package com.cyberwarriers.zubzub.feature.group_create.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.components.ZubZubSubmitButton
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.group_create.R

@Composable
fun CreateConfirmScreen() {
    // 텍스트 필드 상태 관리
    var textValue by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            
            Spacer(modifier = Modifier.weight(1f)) // 상단 여백 자동 조정
            
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

            inviteCopyField()

            Spacer(modifier = Modifier.weight(1f))

            ZubZubSubmitButton(
                text = "완료",
                onClick = {},
            )
            
            Spacer(modifier = Modifier.size(56.dp))
        }
    }
}

@Composable
private fun inviteCopyField() {
    OutlinedTextField(
        value = "ZUBZUB-2024-001",
        onValueChange = { },
        enabled = false,
        modifier = Modifier
            .fillMaxWidth(),
        readOnly = true,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        label = { Text("그룹 초대 코드") },
        trailingIcon = {
            Button(
                onClick = {
                    // TODO: 클립보드에 복사 로직
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
        CreateConfirmScreen()
    }
}