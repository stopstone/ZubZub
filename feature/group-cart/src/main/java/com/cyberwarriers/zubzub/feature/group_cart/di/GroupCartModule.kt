package com.cyberwarriers.zubzub.feature.group_cart.di

import com.cyberwarriers.zubzub.feature.group_cart.data.repository.GroupCartRepositoryImpl
import com.cyberwarriers.zubzub.feature.group_cart.domain.repository.GroupCartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 그룹 카트 모듈 DI
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class GroupCartModule {
    
    @Binds
    @Singleton
    abstract fun bindGroupCartRepository(
        groupCartRepositoryImpl: GroupCartRepositoryImpl
    ): GroupCartRepository
} 