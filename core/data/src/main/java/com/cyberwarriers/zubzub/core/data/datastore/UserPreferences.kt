package com.cyberwarriers.zubzub.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 사용자 로그인 정보를 저장하는 DataStore 클래스
 */
@Singleton
class UserPreferences @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val DATASTORE_NAME = "user_preferences"
        
        // Preferences Keys
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_DISPLAY_NAME_KEY = stringPreferencesKey("user_display_name")
        private val USER_PROFILE_IMAGE_URL_KEY = stringPreferencesKey("user_profile_image_url")
        private val PROVIDER_KEY = stringPreferencesKey("provider")
    }
    
    // DataStore 인스턴스 생성
    private val Context.userPreferencesDataStore by preferencesDataStore(
        name = DATASTORE_NAME
    )
    
    /**
     * 로그인 상태 Flow
     */
    val isLoggedIn: Flow<Boolean> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }
    
    /**
     * 사용자 ID Flow
     */
    val userId: Flow<String?> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }
    
    /**
     * 사용자 이메일 Flow
     */
    val userEmail: Flow<String?> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY]
    }
    
    /**
     * 사용자 표시명 Flow
     */
    val userDisplayName: Flow<String?> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[USER_DISPLAY_NAME_KEY]
    }
    
    /**
     * 사용자 프로필 이미지 URL Flow
     */
    val userProfileImageUrl: Flow<String?> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[USER_PROFILE_IMAGE_URL_KEY]
    }
    
    /**
     * 제공자 Flow
     */
    val provider: Flow<String?> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[PROVIDER_KEY]
    }
    
    /**
     * 사용자 로그인 정보 저장
     */
    suspend fun saveUserLoginInfo(
        userId: String,
        email: String,
        displayName: String,
        profileImageUrl: String,
        provider: String
    ) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = true
            preferences[USER_ID_KEY] = userId
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_DISPLAY_NAME_KEY] = displayName
            preferences[USER_PROFILE_IMAGE_URL_KEY] = profileImageUrl
            preferences[PROVIDER_KEY] = provider
        }
    }
    
    /**
     * 사용자 로그인 정보 삭제
     */
    suspend fun clearUserLoginInfo() {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = false
            preferences.remove(USER_ID_KEY)
            preferences.remove(USER_EMAIL_KEY)
            preferences.remove(USER_DISPLAY_NAME_KEY)
            preferences.remove(USER_PROFILE_IMAGE_URL_KEY)
            preferences.remove(PROVIDER_KEY)
        }
    }
}