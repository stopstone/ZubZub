package com.cyberwarriers.zubzub.feature.group_enter.di

import com.cyberwarriers.zubzub.feature.group_enter.data.repository.GroupEnterRepositoryImpl
import com.cyberwarriers.zubzub.feature.group_enter.domain.repository.GroupEnterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupEnterModule {

    @Binds
    @Singleton
    abstract fun bindGroupEnterRepository(
        groupEnterRepositoryImpl: GroupEnterRepositoryImpl
    ): GroupEnterRepository
} 