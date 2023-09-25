package com.devsinc.cipherhive.domain.use_case.crypto

import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.domain.repository.CredentialRepository
import com.devsinc.cipherhive.util.CryptoManager

class UpdateEncryptedCredential(
    private val repository: CredentialRepository
) {
    suspend operator fun invoke(
        credential: Credential
    ) {
        val cryptoManager = CryptoManager("CH_${credential.label}-${credential.username}_KEY")
        val encryptedPassword = cryptoManager.encrypt(credential.password)

        repository.updateCredential(
            credential.copy(password = encryptedPassword)
        )
    }
}
