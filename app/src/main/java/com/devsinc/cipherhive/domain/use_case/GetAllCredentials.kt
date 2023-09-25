package com.devsinc.cipherhive.domain.use_case

import com.devsinc.cipherhive.domain.repository.CredentialRepository

class GetAllCredentials(
    private val repository: CredentialRepository
) {
    operator fun invoke() = repository.getAllCredentialsStream()
}
