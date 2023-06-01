package com.devsinc.cipherhive.domain.use_case.crypto

import com.devsinc.cipherhive.CryptoManager
import com.devsinc.cipherhive.domain.model.Credential

class GetDecryptedPassword {
    operator fun invoke(
        credential: Credential
    ): String {
        val cryptoManager = CryptoManager("CH_${credential.label}-${credential.username}_KEY")
        return cryptoManager.decrypt(credential.password)
    }
}
