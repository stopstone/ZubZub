package com.cyberwarriers.zubzub.feature.profile.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

/**
 * Firebase Firestore 프로필 엔티티
 */
@Keep
data class ProfileFirebaseEntity(
    @DocumentId
    val userId: String = "",
    val profileName: String = "",
    val profileImageUrl: String = "",
    
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    
    @ServerTimestamp
    val updatedAt: Timestamp? = null
)
