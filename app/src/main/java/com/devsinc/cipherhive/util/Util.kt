package com.devsinc.cipherhive.util

import kotlinx.serialization.json.Json


object Util {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
}
