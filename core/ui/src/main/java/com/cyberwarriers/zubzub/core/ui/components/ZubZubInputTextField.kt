package com.cyberwarriers.zubzub.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme

@Composable
fun ZubZubInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
) {
    Column(
        modifier = modifier,
    ) {
        // 상단 레이블
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        // 텍스트 필드
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            enabled = enabled,
            singleLine = singleLine,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFE0E0E0),
                errorContainerColor = Color(0xFFFFF5F5),
                disabledContainerColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            )
        )
        
        // 에러 메시지
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ZubZubInputTextFieldPreview() {
    ZubZubTheme {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            ZubZubInputTextField(
                value = "가족 장바구니",
                onValueChange = {},
                label = "그룹 이름",
                placeholder = ""
            )
            
            ZubZubInputTextField(
                value = "테스트 그룹",
                onValueChange = {},
                label = "그룹 설명",
                placeholder = "그룹 설명을 입력하세요",
                modifier = Modifier.padding(top = 16.dp)
            )
            
            ZubZubInputTextField(
                value = "",
                onValueChange = {},
                label = "에러 상태",
                placeholder = "필수 입력 항목",
                isError = true,
                errorMessage = "그룹 이름은 필수 입력 항목입니다.",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

