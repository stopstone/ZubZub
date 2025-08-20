package com.cyberwarriers.zubzub.core.domain.repository

import com.cyberwarriers.zubzub.core.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * 인증 관련 기능을 제공하는 Repository 인터페이스
 * 
 * Clean Architecture 원칙에 따라 도메인 계층에서 정의
 */
interface AuthRepository {
    /**
     * 구글 소셜 로그인을 수행합니다.
     * 
     * @param account 구글 로그인 계정 정보 (Any 타입으로 변경하여 의존성 제거)
     * @return 로그인 결과
     */
    suspend fun signWithGoogle(account: Any): Result<Unit>
    
    /**
     * 로그아웃을 수행합니다.
     * 
     * @return 로그아웃 결과
     */
    suspend fun signOut(): Result<Unit>
    
    /**
     * 현재 로그인된 사용자를 반환합니다.
     * 
     * @return 사용자 정보 (로그인되지 않은 경우 null)
     */
    fun getCurrentUser(): Any?
    
    /**
     * 현재 로그인 상태를 확인합니다.
     * 
     * @return 로그인 상태 (true: 로그인됨, false: 로그인되지 않음)
     */
    fun isUserLoggedIn(): Boolean
    
    /**
     * DataStore에 사용자 로그인 정보를 저장합니다.
     * 
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @param displayName 사용자 표시명
     * @param profileImageUrl 사용자 프로필 이미지 URL
     * @param provider 로그인 제공자 (google, facebook 등)
     */
    suspend fun saveUserLoginInfoToDataStore(
        userId: String,
        email: String,
        displayName: String,
        profileImageUrl: String,
        provider: String
    )
    
    /**
     * DataStore에서 사용자 로그인 정보를 삭제합니다.
     */
    suspend fun clearUserLoginInfoFromDataStore()
    
    /**
     * DataStore에서 로그인 상태를 확인합니다.
     * 
     * @return 로그인 상태 Flow
     */
    fun isUserLoggedInFromDataStore(): Flow<Boolean>
    
    /**
     * Firestore에 사용자 정보를 저장합니다.
     * 
     * @param user 저장할 사용자 정보
     * @return 저장 결과
     */
    suspend fun saveUserToFirestore(user: User): Result<Unit>
    
    /**
     * Firestore에서 사용자 정보를 조회합니다.
     * 
     * @param userId 조회할 사용자 ID
     * @return 사용자 정보 (존재하지 않는 경우 null)
     */
    suspend fun getUserFromFirestore(userId: String): Result<User?>
    
    /**
     * Firestore에서 사용자 정보를 업데이트합니다.
     * 
     * @param user 업데이트할 사용자 정보
     * @return 업데이트 결과
     */
    suspend fun updateUserInFirestore(user: User): Result<Unit>
    
    /**
     * 현재 사용자가 프로필을 가지고 있는지 확인합니다.
     * 
     * @return 프로필 존재 여부
     */
    suspend fun hasUserProfile(): Result<Boolean>
}
