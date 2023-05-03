package com.devsinc.cipherhive.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDatum(
    val username: String? = null,
    val password: String? = null
)
