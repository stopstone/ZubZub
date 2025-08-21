package com.cyberwarriers.zubzub.feature.profile.di

import com.cyberwarriers.zubzub.feature.profile.data.repository.GalleryRepositoryImpl
import com.cyberwarriers.zubzub.feature.profile.data.repository.ProfileRepositoryImpl
import com.cyberwarriers.zubzub.feature.profile.domain.repository.GalleryRepository
import com.cyberwarriers.zubzub.feature.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Profile 기능의 DI 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {
    
    /**
     * ProfileRepository 인터페이스와 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
    
    /**
     * GalleryRepository 인터페이스와 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindGalleryRepository(
        galleryRepositoryImpl: GalleryRepositoryImpl
    ): GalleryRepository
}