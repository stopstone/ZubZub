package com.cyberwarriers.zubzub.feature.group_enter.domain.repository

import com.cyberwarriers.zubzub.feature.group_enter.domain.model.GroupInfo

/**
 * 그룹 입장 Repository 인터페이스
 */
interface GroupEnterRepository {
    
    /**
     * 초대 코드로 그룹 정보 조회
     */
    suspend fun getGroupByInviteCode(inviteCode: String): Result<GroupInfo?>
    
    /**
     * 그룹에 멤버로 참여
     */
    suspend fun joinGroup(groupId: String): Result<Unit>
} 