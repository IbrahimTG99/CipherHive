package com.devsinc.cipherhive.domain.use_case.validate

class ValidatePassword {
    operator fun invoke(password: String): ValidationResult {
        return if (password.isEmpty()) {
            ValidationResult(
                isValid = false, errorMessage = "The password can't be blank"
            )
        } else if (password.length < 8) {
            ValidationResult(
                isValid = false, errorMessage = "The password must be at least 8 characters long"
            )
        } else ValidationResult(true)
    }
}
