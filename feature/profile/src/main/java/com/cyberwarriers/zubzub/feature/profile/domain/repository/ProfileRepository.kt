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
     * 현재 사용자의 프로필을 조회합니다.
     * 
     * @return 프로필 정보 (없는 경우 null)
     */
    suspend fun getProfile(): Result<Profile?>
    
    /**
     * 프로필 이미지를 업로드합니다.
     * 
     * @param imageUri 업로드할 이미지 URI
     * @return 업로드된 이미지 URL
     */
    suspend fun uploadProfileImage(imageUri: String): Result<String>
}
