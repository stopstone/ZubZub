package com.cyberwarriers.zubzub.core.data.di

import android.content.Context
import com.cyberwarriers.zubzub.core.data.datastore.UserPreferences
import com.cyberwarriers.zubzub.core.data.repository.CartGroupRepositoryImpl
import com.cyberwarriers.zubzub.core.domain.repository.CartGroupRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Data 계층 관련 의존성 주입 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    
    /**
     * CartGroupRepository 인터페이스를 CartGroupRepositoryImpl 구현체에 바인딩합니다.
     */
    @Binds
    @Singleton
    abstract fun bindCartGroupRepository(
        cartGroupRepositoryImpl: CartGroupRepositoryImpl
    ): CartGroupRepository
    
    companion object {
        /**
         * UserPreferences 인스턴스를 제공합니다.
         * 
         * @param context Android Application Context
         * @return UserPreferences 인스턴스
         */
        @Provides
        @Singleton
        fun provideUserPreferences(
            @ApplicationContext context: Context
        ): UserPreferences {
            return UserPreferences(context)
        }
    }
} 