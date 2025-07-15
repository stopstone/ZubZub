package com.cyberwarriers.zubzub.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme

/**
 * 숫자 입력 전용 텍스트 필드 컴포넌트
 */
@Composable
fun ZubZubNumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true,
    allowZero: Boolean = false,
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
        
        // 숫자 입력 텍스트 필드
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = value,
            onValueChange = { newValue ->
                // 숫자만 입력 가능하도록 필터링
                if (newValue.isEmpty()) {
                    onValueChange(newValue)
                } else if (newValue.all { it.isDigit() }) {
                    val number = newValue.toIntOrNull()
                    if (number != null && (allowZero || number > 0)) {
                        onValueChange(newValue)
                    }
                }
            },
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            enabled = enabled,
            singleLine = true,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
fun ZubZubNumberInputFieldPreview() {
    ZubZubTheme {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            ZubZubNumberInputField(
                value = "5000",
                onValueChange = {},
                label = "가격",
                placeholder = "가격을 입력하세요 (원)"
            )
            
            ZubZubNumberInputField(
                value = "2",
                onValueChange = {},
                label = "수량",
                placeholder = "수량",
                modifier = Modifier.padding(top = 16.dp)
            )
            
            ZubZubNumberInputField(
                value = "",
                onValueChange = {},
                label = "에러 상태",
                placeholder = "필수 입력 항목",
                isError = true,
                errorMessage = "수량은 1 이상이어야 합니다.",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
} 