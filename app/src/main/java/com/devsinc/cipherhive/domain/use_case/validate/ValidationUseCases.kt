package com.devsinc.cipherhive.domain.use_case.validate

data class ValidationUseCases(
    val validateLabel: ValidateLabel,
    val validateUsername: ValidateUsername,
    val validatePassword: ValidatePassword,
    val validateUrl: ValidateUrl
)
