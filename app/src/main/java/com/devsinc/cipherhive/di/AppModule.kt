package com.devsinc.cipherhive.di

import com.devsinc.cipherhive.data.repository.CredentialRepository
import com.devsinc.cipherhive.data.repository.CredentialRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideCredentialRepository(impl: CredentialRepositoryImpl): CredentialRepository = impl
}
