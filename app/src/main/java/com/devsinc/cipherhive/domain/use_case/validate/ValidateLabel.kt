package com.devsinc.cipherhive.domain.use_case.validate

class ValidateLabel {
    operator fun invoke(label: String): ValidationResult {
        return if (label.isEmpty()) {
            ValidationResult(
                isValid = false, errorMessage = "The label can't be blank"
            )
        } else
            ValidationResult(true)
    }
}
