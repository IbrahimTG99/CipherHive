package com.devsinc.cipherhive.domain.use_case.validate

import android.webkit.URLUtil

class ValidateUrl {
    operator fun invoke(url: String): ValidationResult {
        return if (url.isEmpty()) {
            ValidationResult(
                isValid = false, errorMessage = "The url can't be blank"
            )
        } else if (URLUtil.isValidUrl(url)) {
            ValidationResult(
                isValid = false, errorMessage = "The url is not valid"
            )
        } else ValidationResult(true)
    }
}
