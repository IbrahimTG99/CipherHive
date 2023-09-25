package com.devsinc.cipherhive.presentation.credential

data class CredentialFormState(
    val label: String = "",
    val labelError: String? = null,
    val username: String = "",
    val usernameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val url: String = "",
    val urlError: String? = null,
    val notes: String = "",
)
