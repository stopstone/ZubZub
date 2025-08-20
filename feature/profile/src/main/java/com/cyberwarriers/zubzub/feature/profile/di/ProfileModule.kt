package com.cyberwarriers.zubzub.feature.profile.di

import com.cyberwarriers.zubzub.feature.profile.data.repository.ProfileRepositoryImpl
import com.cyberwarriers.zubzub.feature.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 프로필 모듈 DI 설정
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {
    
    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
}
