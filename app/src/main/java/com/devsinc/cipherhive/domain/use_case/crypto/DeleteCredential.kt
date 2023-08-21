package com.devsinc.cipherhive.domain.use_case.crypto

import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.domain.repository.CredentialRepository

class DeleteCredential(
    private val repository: CredentialRepository
) {
    suspend operator fun invoke(
        credential: Credential
    ) {
        repository.deleteCredential(credential)
    }
}
