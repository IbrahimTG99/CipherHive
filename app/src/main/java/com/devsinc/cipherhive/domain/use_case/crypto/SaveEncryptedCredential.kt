package com.devsinc.cipherhive.domain.use_case.crypto

import com.devsinc.cipherhive.CryptoManager
import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.domain.repository.CredentialRepository

class SaveEncryptedCredential(
    private val repository: CredentialRepository
) {
    suspend operator fun invoke(
        credential: Credential
    ) {
        val cryptoManager = CryptoManager("CH_${credential.label}-${credential.username}_KEY")
        val encryptedPassword = cryptoManager.encrypt(credential.password)

        repository.insertCredential(
            credential.copy(password = encryptedPassword)
        )
    }
}
