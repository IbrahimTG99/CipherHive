package com.devsinc.cipherhive.domain.use_case.validate

class ValidateUsername {
    operator fun invoke(username: String): ValidationResult {
        return if (username.isEmpty()) {
            ValidationResult(
                isValid = false, errorMessage = "The username can't be blank"
            )
        } else
            ValidationResult(true)
    }
}
