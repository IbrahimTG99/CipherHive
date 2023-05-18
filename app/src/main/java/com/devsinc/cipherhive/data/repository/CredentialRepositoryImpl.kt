package com.devsinc.cipherhive.data.repository

import com.devsinc.cipherhive.data.db.CredentialDao
import com.devsinc.cipherhive.domain.model.Credential
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CredentialRepositoryImpl @Inject constructor(
    private val credentialDao: CredentialDao
) : CredentialRepository {
    override fun getAllCredentialsStream(): Flow<List<Credential>> = credentialDao.getAllCredentials()

    override fun getCredentialsByUrlStream(url: String): Flow<List<Credential>> = credentialDao.getCredentialsByUrl(url)

    override suspend fun insertCredential(credential: Credential) = credentialDao.insert(credential)

    override suspend fun updateCredential(credential: Credential) = credentialDao.update(credential)

    override suspend fun deleteCredential(credential: Credential) = credentialDao.delete(credential)
}
