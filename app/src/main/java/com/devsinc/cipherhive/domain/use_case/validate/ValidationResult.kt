package com.devsinc.cipherhive.domain.use_case.validate

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
