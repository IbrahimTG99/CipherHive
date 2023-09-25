package com.devsinc.cipherhive.di

import com.devsinc.cipherhive.data.repository.CredentialRepositoryImpl
import com.devsinc.cipherhive.domain.repository.CredentialRepository
import com.devsinc.cipherhive.domain.use_case.GetAllCredentials
import com.devsinc.cipherhive.domain.use_case.crypto.DbUseCases
import com.devsinc.cipherhive.domain.use_case.crypto.DeleteCredential
import com.devsinc.cipherhive.domain.use_case.crypto.GetDecryptedPassword
import com.devsinc.cipherhive.domain.use_case.crypto.SaveEncryptedCredential
import com.devsinc.cipherhive.domain.use_case.crypto.UpdateEncryptedCredential
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
    fun provideSaveEncryptedCredential(repository: CredentialRepositoryImpl) =
        SaveEncryptedCredential(repository)

    @Provides
    @Singleton
    fun provideGetDecryptedPassword() = GetDecryptedPassword()

    @Provides
    @Singleton
    fun provideUpdateEncryptedCredential(repository: CredentialRepositoryImpl) =
        UpdateEncryptedCredential(repository)

    @Provides
    @Singleton
    fun provideDeleteCredential(repository: CredentialRepositoryImpl) = DeleteCredential(repository)

    @Provides
    @Singleton
    fun provideDbUseCases(
        getDecryptedPassword: GetDecryptedPassword,
        saveEncryptedCredential: SaveEncryptedCredential,
        updateEncryptedCredential: UpdateEncryptedCredential,
        deleteCredential: DeleteCredential
    ) = DbUseCases(
        getDecryptedPassword = getDecryptedPassword,
        saveEncryptedCredential = saveEncryptedCredential,
        updateEncryptedCredential = updateEncryptedCredential,
        deleteCredential = deleteCredential
    )

    @Provides
    @Singleton
    fun provideGetAllCredentials(repository: CredentialRepositoryImpl) =
        GetAllCredentials(repository)
}
