package com.devsinc.cipherhive.domain.use_case.crypto

data class DbUseCases(
    val getDecryptedPassword: GetDecryptedPassword,
    val saveEncryptedCredential: SaveEncryptedCredential,
    val updateEncryptedCredential: UpdateEncryptedCredential,
    val deleteCredential: DeleteCredential
)
