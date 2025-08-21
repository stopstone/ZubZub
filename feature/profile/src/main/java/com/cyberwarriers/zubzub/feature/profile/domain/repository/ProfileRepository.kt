package com.cyberwarriers.zubzub.feature.profile.domain.repository

import com.cyberwarriers.zubzub.feature.profile.domain.model.CreateProfileRequest
import com.cyberwarriers.zubzub.feature.profile.domain.model.Profile
import com.cyberwarriers.zubzub.feature.profile.domain.model.UpdateProfileRequest

/**
 * 프로필 관련 기능을 제공하는 Repository 인터페이스
 * 
 * Clean Architecture 원칙에 따라 도메인 계층에서 정의
 */
interface ProfileRepository {
    
    /**
     * 프로필을 생성합니다.
     * 
     * @param request 프로필 생성 요청 정보
     * @return 생성된 프로필 정보
     */
    suspend fun createProfile(request: CreateProfileRequest): Result<Profile>
    
    /**
     * 프로필을 업데이트합니다.
     * 
     * @param request 프로필 업데이트 요청 정보
     * @return 업데이트된 프로필 정보
     */
    suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile>
    
    /**
     * 현재 사용자의 기본 프로필을 조회합니다.
     * 
     * @return 기본 프로필 정보 (없는 경우 null)
     */
    suspend fun getDefaultProfile(): Result<Profile?>
    
    /**
     * 현재 사용자의 모든 활성 프로필을 조회합니다.
     * 
     * @return 프로필 목록
     */
    suspend fun getAllProfiles(): Result<List<Profile>>
    
    /**
     * 특정 프로필을 조회합니다.
     * 
     * @param profileId 조회할 프로필 ID
     * @return 프로필 정보 (없는 경우 null)
     */
    suspend fun getProfileById(profileId: String): Result<Profile?>
    
    /**
     * 사용자가 프로필을 가지고 있는지 확인합니다.
     * 
     * @return 프로필 존재 여부
     */
    suspend fun hasProfile(): Result<Boolean>
    
    /**
     * 프로필 이미지를 업로드합니다.
     * 
     * @param imageUri 업로드할 이미지 URI
     * @return 업로드된 이미지 URL
     */
    suspend fun uploadProfileImage(imageUri: String): Result<String>
    
    /**
     * 기본 프로필이 없는 경우 첫 번째 프로필을 기본으로 설정합니다.
     * 
     * @return 설정 결과
     */
    suspend fun ensurePrimaryProfileExists(): Result<Unit>
}
