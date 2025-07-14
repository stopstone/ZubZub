package com.cyberwarriers.zubzub.feature.group_cart.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyberwarriers.zubzub.core.ui.theme.ZubZubTheme
import com.cyberwarriers.zubzub.feature.group_cart.presentation.data.Member

/**
 * 멤버 콘텐츠
 */
@Composable
fun MembersContent(
    members: List<Member> = emptyList(),
    onMemberClick: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (members.isEmpty()) {
            // 빈 상태
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "빈 멤버",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "멤버가 없습니다",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "멤버를 초대해보세요",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(members) { member ->
                    MemberCard(
                        member = member,
                        onClick = { onMemberClick(member.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MemberCard(
    member: Member,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        // 멤버 정보
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = member.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (member.isGroupLeader) {
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "그룹장",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "기여도: ${String.format("%,d", member.totalContribution)}원",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// 더미 데이터
private val dummyMembers = listOf(
    Member(
        id = "1",
        name = "김철수",
        email = "kim@example.com",
        isGroupLeader = true,
        joinDate = "2024.01.15",
        totalContribution = 15000
    ),
    Member(
        id = "2",
        name = "이영희",
        email = "lee@example.com",
        isGroupLeader = false,
        joinDate = "2024.01.20",
        totalContribution = 12000
    ),
    Member(
        id = "3",
        name = "박민수",
        email = "park@example.com",
        isGroupLeader = false,
        joinDate = "2024.02.01",
        totalContribution = 8000
    ),
    Member(
        id = "4",
        name = "최지영",
        email = "choi@example.com",
        isGroupLeader = false,
        joinDate = "2024.02.10",
        totalContribution = 9500
    )
)

@Preview(showBackground = true)
@Composable
fun MembersContentPreview() {
    ZubZubTheme {
        MembersContent(
            members = dummyMembers
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MembersContentEmptyPreview() {
    ZubZubTheme {
        MembersContent(
            members = emptyList()
        )
    }
} 