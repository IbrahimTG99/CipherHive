package com.devsinc.cipherhive.data.repository

import com.devsinc.cipherhive.domain.model.Credential
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {
    fun getAllCredentialsStream(): Flow<List<Credential>>
    fun getCredentialsByUrlStream(url: String): Flow<List<Credential>>
    suspend fun insertCredential(credential: Credential)
    suspend fun updateCredential(credential: Credential)
    suspend fun deleteCredential(credential: Credential)
}
