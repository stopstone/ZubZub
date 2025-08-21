package com.cyberwarriers.zubzub.core.domain.repository

import com.cyberwarriers.zubzub.core.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Domain Layer - Repository Interface
 * 
 * 프로필 관련 비즈니스 로직에서 필요한 데이터 연산을 정의
 * 구현체는 Data Layer에서 제공
 */
interface ProfileRepository {

    /**
     * 현재 사용자의 모든 프로필 조회 (실시간 업데이트)
     * @return 프로필 목록 Flow
     */
    fun getAllProfilesFlow(): Flow<List<UserProfile>>

    /**
     * 현재 사용자의 모든 프로필 조회 (일회성)
     * @return 프로필 목록
     */
    suspend fun getAllProfiles(): Result<List<UserProfile>>

    /**
     * 특정 프로필 조회
     * @param profileId 조회할 프로필 ID
     * @return 프로필 정보 (없는 경우 null)
     */
    suspend fun getProfileById(profileId: String): Result<UserProfile?>

    /**
     * 프로필 생성
     * @param nickname 프로필 닉네임
     * @param profileImageUrl 프로필 이미지 URL (선택사항)
     * @return 생성된 프로필 정보
     */
    suspend fun createProfile(nickname: String, profileImageUrl: String? = null): Result<UserProfile>

    /**
     * 프로필 업데이트
     * @param profileId 프로필 ID
     * @param nickname 새로운 닉네임 (선택사항)
     * @param profileImageUrl 새로운 이미지 URL (선택사항)
     * @param currentGroupName 현재 사용중인 그룹명 (선택사항)
     * @return 업데이트된 프로필 정보
     */
    suspend fun updateProfile(
        profileId: String,
        nickname: String? = null,
        profileImageUrl: String? = null,
        currentGroupName: String? = null
    ): Result<UserProfile>

    /**
     * 프로필 삭제
     * @param profileId 삭제할 프로필 ID
     * @return 삭제 결과
     */
    suspend fun deleteProfile(profileId: String): Result<Unit>

    /**
     * 사용자가 프로필을 가지고 있는지 확인
     * @return 프로필 존재 여부
     */
    suspend fun hasProfile(): Result<Boolean>
}
