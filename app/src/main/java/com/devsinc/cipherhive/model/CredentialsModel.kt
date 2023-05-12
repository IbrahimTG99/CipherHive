package com.devsinc.cipherhive.model

data class CredentialsModel(
    val id: String,
    val title: String,
    val username: String,
    val password: String,
    val url:String,
    val notes: String?=""
)
