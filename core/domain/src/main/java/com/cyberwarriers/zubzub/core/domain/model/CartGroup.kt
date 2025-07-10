package com.cyberwarriers.zubzub.core.domain.model

/**
 * 홈 화면용 그룹 요약 정보
 */
data class CartGroupSummary(
    val groupId: String,
    val groupName: String,
    val memberCount: Int,
    val progressPercentage: Int,
    val createdAt: Long,
    val isOwner: Boolean,
    val currentAmount: Long,
    val targetAmount: Long
)

/**
 * 상세 화면용 그룹 전체 정보
 */
data class CartGroupDetail(
    val groupId: String,
    val groupName: String,
    val description: String,
    val members: List<GroupMember>,
    val totalAmount: Long,
    val targetAmount: Long,
    val progressPercentage: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val settings: GroupSettings,
    val status: GroupStatus
)

/**
 * 그룹 멤버 정보
 */
data class GroupMember(
    val userId: String,
    val nickname: String,
    val profileImage: String,
    val role: MemberRole,
    val contributedAmount: Long,
    val itemsAdded: Int,
    val joinedAt: Long,
    val isActive: Boolean
)

/**
 * 그룹 설정 정보
 */
data class GroupSettings(
    val isPrivate: Boolean,
    val budgetLimit: Long?,
    val allowNotifications: Boolean,
    val maxMembers: Int,
    val requireApproval: Boolean
)

/**
 * 멤버 역할
 */
enum class MemberRole {
    OWNER,    // 그룹 생성자
    MEMBER    // 일반 멤버
}

/**
 * 그룹 상태
 */
enum class GroupStatus {
    ACTIVE,      // 진행중
    COMPLETED,   // 완료됨
    INACTIVE     // 비활성
} 