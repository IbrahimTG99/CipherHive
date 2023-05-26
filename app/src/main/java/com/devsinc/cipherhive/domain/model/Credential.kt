package com.devsinc.cipherhive.domain.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credentials")
data class Credential(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val label: String = "",
    val username: String = "",
    val password: String = "",
    val url: String = "",
    val packageId: String = "",
    val notes: String? = "",
    val favicon: Bitmap? = null
)
