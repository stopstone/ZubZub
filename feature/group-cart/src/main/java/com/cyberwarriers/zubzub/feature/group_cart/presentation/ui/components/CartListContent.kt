package com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.CartItem

/**
 * 카트 목록 콘텐츠
 */
@Composable
fun CartListContent(
    cartItems: List<CartItem> = emptyList(),
    onAddItem: () -> Unit = {},
    onItemToggle: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (cartItems.isEmpty()) {
            // 빈 상태
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "빈 카트",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "카트가 비어있습니다",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "아이템을 추가해보세요",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // 카트 아이템 목록
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems) { item ->
                    CartItemCard(
                        item = item,
                        onToggle = { onItemToggle(item.id) }
                    )
                }
            }
        }

        // 플로팅 액션 버튼
        FloatingActionButton(
            onClick = onAddItem,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "아이템 추가"
            )
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onToggle,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 아이템 정보
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (item.isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Spacer(modifier = Modifier.size(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${String.format("%,d", item.price)}원",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "by ${item.addedBy}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 원형 체크박스
            Surface(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape),
                shape = CircleShape,
                color = if (item.isCompleted) {
                    Color(0xFF0064FF)
                } else {
                    Color.Transparent
                },
                border = if (!item.isCompleted) {
                    BorderStroke(2.dp, Color(0xFFE0E0E0))
                } else {
                    null
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "완료됨",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// 더미 데이터
private val dummyCartItems = listOf(
    CartItem(
        id = "1",
        name = "사과",
        price = 5000,
        quantity = 2,
        addedBy = "김철수",
        isCompleted = false
    ),
    CartItem(
        id = "2",
        name = "바나나",
        price = 3000,
        quantity = 1,
        addedBy = "이영희",
        isCompleted = true
    ),
    CartItem(
        id = "3",
        name = "우유",
        price = 4500,
        quantity = 1,
        addedBy = "박민수",
        isCompleted = false
    ),
    CartItem(
        id = "4",
        name = "빵",
        price = 2500,
        quantity = 3,
        addedBy = "김철수",
        isCompleted = false
    )
)

@Preview(showBackground = true)
@Composable
fun CartListContentPreview() {
    ZubZubTheme {
        CartListContent(
            cartItems = dummyCartItems
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartListContentEmptyPreview() {
    ZubZubTheme {
        CartListContent(
            cartItems = emptyList()
        )
    }
} 