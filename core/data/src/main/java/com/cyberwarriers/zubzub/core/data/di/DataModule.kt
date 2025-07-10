package com.cyberwarriers.zubzub.core.data.di

import com.cyberwarriers.zubzub.core.data.repository.CartGroupRepositoryImpl
import com.cyberwarriers.zubzub.core.domain.repository.CartGroupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindCartGroupRepository(
        cartGroupRepositoryImpl: CartGroupRepositoryImpl
    ): CartGroupRepository
} 