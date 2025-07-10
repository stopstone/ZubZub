package com.cyberwarriers.zubzub.core.data.mapper

import com.cyberwarriers.zubzub.core.data.model.CartGroupFirebaseEntity
import com.cyberwarriers.zubzub.core.data.model.GroupMemberEntity
import com.cyberwarriers.zubzub.core.domain.model.CartGroupDetail
import com.cyberwarriers.zubzub.core.domain.model.CartGroupSummary
import com.cyberwarriers.zubzub.core.domain.model.GroupMember
import com.cyberwarriers.zubzub.core.domain.model.GroupSettings
import com.cyberwarriers.zubzub.core.domain.model.GroupStatus
import com.cyberwarriers.zubzub.core.domain.model.MemberRole

/**
 * Firebase Entity -> UI Model 변환
 */

// Firebase Entity -> CartGroupSummary
fun CartGroupFirebaseEntity.toSummary(currentUserId: String): CartGroupSummary {
    val progressPercentage = if (targetAmount > 0) {
        ((currentAmount.toDouble() / targetAmount.toDouble()) * 100).toInt()
    } else 0
    
    return CartGroupSummary(
        groupId = groupId,
        groupName = groupName,
        memberCount = memberIds.size,
        progressPercentage = progressPercentage,
        createdAt = createdAt?.seconds?.times(1000) ?: System.currentTimeMillis(),
        isOwner = createdBy == currentUserId,
        currentAmount = currentAmount,
        targetAmount = targetAmount
    )
}

// Firebase Entity -> CartGroupDetail (멤버 리스트 필요)
fun CartGroupFirebaseEntity.toDetail(members: List<GroupMember>): CartGroupDetail {
    val progressPercentage = if (targetAmount > 0) {
        ((currentAmount.toDouble() / targetAmount.toDouble()) * 100).toInt()
    } else 0
    
    return CartGroupDetail(
        groupId = groupId,
        groupName = groupName,
        description = description,
        members = members,
        totalAmount = currentAmount,
        targetAmount = targetAmount,
        progressPercentage = progressPercentage,
        createdAt = createdAt?.seconds?.times(1000) ?: System.currentTimeMillis(),
        updatedAt = updatedAt?.seconds?.times(1000) ?: System.currentTimeMillis(),
        settings = settings.toGroupSettings(),
        status = status.toGroupStatus()
    )
}

// GroupMemberEntity -> GroupMember
fun GroupMemberEntity.toGroupMember(): GroupMember {
    return GroupMember(
        userId = userId,
        nickname = nickname,
        profileImage = profileImage,
        role = role.toMemberRole(),
        contributedAmount = contributedAmount,
        itemsAdded = itemsAdded,
        joinedAt = joinedAt?.seconds?.times(1000) ?: System.currentTimeMillis(),
        isActive = isActive
    )
}

/**
 * UI Model -> Firebase Entity 변환
 */

// 그룹 생성용 데이터 -> Firebase Entity
fun createCartGroupEntity(
    groupName: String,
    description: String,
    createdBy: String,
    targetAmount: Long = 0L
): CartGroupFirebaseEntity {
    return CartGroupFirebaseEntity(
        groupName = groupName,
        description = description,
        createdBy = createdBy,
        memberIds = listOf(createdBy), // 생성자를 첫 번째 멤버로
        targetAmount = targetAmount,
        currentAmount = 0L,
        status = "ACTIVE"
    )
}

// 그룹 생성자를 첫 번째 멤버로 추가
fun createOwnerMember(
    userId: String,
    nickname: String,
    profileImage: String = ""
): GroupMemberEntity {
    return GroupMemberEntity(
        userId = userId,
        nickname = nickname,
        profileImage = profileImage,
        role = "OWNER",
        contributedAmount = 0L,
        itemsAdded = 0,
        isActive = true
    )
}

/**
 * 확장 함수들
 */

// String -> GroupStatus
private fun String.toGroupStatus(): GroupStatus {
    return when (this.uppercase()) {
        "ACTIVE" -> GroupStatus.ACTIVE
        "COMPLETED" -> GroupStatus.COMPLETED
        "INACTIVE" -> GroupStatus.INACTIVE
        else -> GroupStatus.ACTIVE
    }
}

// String -> MemberRole
private fun String.toMemberRole(): MemberRole {
    return when (this.uppercase()) {
        "OWNER" -> MemberRole.OWNER
        "MEMBER" -> MemberRole.MEMBER
        else -> MemberRole.MEMBER
    }
}

// Map -> GroupSettings
private fun Map<String, Any>.toGroupSettings(): GroupSettings {
    return GroupSettings(
        isPrivate = this["isPrivate"] as? Boolean ?: false,
        budgetLimit = (this["budgetLimit"] as? Number)?.toLong(),
        allowNotifications = this["allowNotifications"] as? Boolean ?: true,
        maxMembers = (this["maxMembers"] as? Number)?.toInt() ?: 10,
        requireApproval = this["requireApproval"] as? Boolean ?: false
    )
} 