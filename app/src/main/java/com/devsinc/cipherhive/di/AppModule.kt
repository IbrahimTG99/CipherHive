package com.devsinc.cipherhive.di

import com.devsinc.cipherhive.domain.repository.CredentialRepository
import com.devsinc.cipherhive.data.repository.CredentialRepositoryImpl
import com.devsinc.cipherhive.domain.use_case.GetAllCredentials
import com.devsinc.cipherhive.domain.use_case.crypto.GetDecryptedPassword
import com.devsinc.cipherhive.domain.use_case.crypto.SaveEncryptedCredential
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCredentialRepository(impl: CredentialRepositoryImpl): CredentialRepository = impl

    @Provides
    @Singleton
    fun provideSaveEncryptedCredential(repository: CredentialRepositoryImpl) = SaveEncryptedCredential(repository)

    @Provides
    @Singleton
    fun provideGetDecryptedPassword() = GetDecryptedPassword()

    @Provides
    @Singleton
    fun provideGetAllCredentials(repository: CredentialRepositoryImpl) = GetAllCredentials(repository)
}
