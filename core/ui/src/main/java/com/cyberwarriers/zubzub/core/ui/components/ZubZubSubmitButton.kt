package com.cyberwarriers.zubzub.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
fun ZubZubSubmitButton(
    modifier: Modifier = Modifier,
    text: String = "확인",
    onClick: () -> Unit = {},
    enable: Boolean = true,
) {
    // 그룹 생성 버튼
    Button(
        onClick = onClick,
        enabled = enable,
        colors = ButtonColors(
            containerColor = Color(0xFF388E3C),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFC7C7C7),
            disabledContentColor = Color(0xFFACACAC),
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun ZubZubSubmitButtonPreview() {
    ZubZubTheme {
        ZubZubSubmitButton()
    }
}

@Preview
@Composable
fun ZubZubSubmitButtonDisabledPreview() {
    ZubZubTheme {
        ZubZubSubmitButton(
            enable = false,
        )
    }
}