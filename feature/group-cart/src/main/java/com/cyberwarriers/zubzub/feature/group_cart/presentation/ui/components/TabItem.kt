package com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 그룹 카트 탭 아이템을 정의하는 Enum
 */
enum class TabItem(
    val titleResId: Int,
    val index: Int
) {
    CART(android.R.string.ok, 0),
    MEMBERS(android.R.string.cancel, 1);

    companion object {
        fun fromIndex(index: Int): TabItem? {
            return values().find { it.index == index }
        }
    }
}

/**
 * 탭 행 컴포넌트
 */
@Composable
fun GroupCartTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(selectedTabIndex = selectedTabIndex,) {
        TabItem.entries.forEach { tabItem ->
            Tab(
                selected = selectedTabIndex == tabItem.index,
                onClick = { onTabSelected(tabItem.index) },
                modifier = Modifier.background(Color.White),
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = when (tabItem) {
                        TabItem.CART -> "카트"
                        TabItem.MEMBERS -> "멤버"
                    }
                )
            }
        }
    }
} 