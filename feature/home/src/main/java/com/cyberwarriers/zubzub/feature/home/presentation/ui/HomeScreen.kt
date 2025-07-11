package com.cyberwarriers.zubzub.feature.home.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyberwarriers.zubzub.core.domain.model.CartGroupSummary
import com.cyberwarriers.zubzub.core.ui.components.ZubZubTopAppBar
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.home.presentation.HomeViewModel
import com.cyberwarriers.zubzub.feature.home.presentation.ui.components.GroupActionCards
import com.cyberwarriers.zubzub.feature.home.presentation.ui.components.MyGroupItem

@Composable
fun HomeScreen(
    onNavigateToGroupCreate: () -> Unit = {},
    onNavigateToGroupEnter: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            ZubZubTopAppBar(
                title = "홈"
            )

            GroupActionCards(
                onCreateGroupClick = onNavigateToGroupCreate,
                onEnterGroupClick = onNavigateToGroupEnter,
            )

            MyGroupList(
                groups = uiState.groups,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                onGroupClick = viewModel::onGroupClicked,
                onRefresh = viewModel::refreshGroups
            )
        }
    }
}

@Composable
private fun MyGroupList(
    groups: List<CartGroupSummary>,
    isLoading: Boolean,
    errorMessage: String?,
    onGroupClick: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "나의 장바구니 그룹",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
        )

        if (groups.isNotEmpty()) {
            Text(
                text = "모두 보기",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
    }

    LazyColumn {
        when {
            isLoading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            errorMessage != null -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            groups.isEmpty() -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "아직 속한 그룹이 없습니다.\n새로운 그룹을 만들어보세요!",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            else -> {
                items(groups) { group ->
                    MyGroupItem(
                        group = group,
                        onClick = onGroupClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    ZubZubTheme {
        HomeScreen()
    }
}