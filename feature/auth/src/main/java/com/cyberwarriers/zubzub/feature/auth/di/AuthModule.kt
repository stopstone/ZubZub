package com.cyberwarriers.zubzub.feature.auth.di

import com.cyberwarriers.zubzub.feature.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        firebaseAuth: FirebaseAuth,
    ): AuthRepository
}