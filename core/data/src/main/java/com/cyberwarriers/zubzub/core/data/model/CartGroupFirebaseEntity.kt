package com.cyberwarriers.zubzub.core.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

@Keep
data class CartGroupFirebaseEntity(
    @DocumentId
    val groupId: String = "",
    
    val groupName: String = "",
    val description: String = "",
    val createdBy: String = "",
    
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    
    @ServerTimestamp  
    val updatedAt: Timestamp? = null,
    
    val memberIds: List<String> = emptyList(),
    val status: String = "ACTIVE", // ACTIVE, COMPLETED, INACTIVE
    val targetAmount: Long = 0L,
    val currentAmount: Long = 0L,
    
    // 설정 정보
    val settings: Map<String, Any> = mapOf(
        "isPrivate" to false,
        "budgetLimit" to 0L,
        "allowNotifications" to true,
        "maxMembers" to 10,
        "requireApproval" to false
    )
)

@Keep
data class GroupMemberEntity(
    @DocumentId
    val userId: String = "",
    val nickname: String = "",
    val profileImage: String = "",
    val role: String = "MEMBER", // OWNER, MEMBER
    val contributedAmount: Long = 0L,
    val itemsAdded: Int = 0,
    
    @ServerTimestamp
    val joinedAt: Timestamp? = null,
    
    @ServerTimestamp
    val lastActiveAt: Timestamp? = null,
    
    val isActive: Boolean = true
) 