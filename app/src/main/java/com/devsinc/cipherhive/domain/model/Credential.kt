package com.devsinc.cipherhive.domain.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Contextual

@Entity(tableName = "credentials")
data class Credential(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val label: String,
    val username: String,
    val password: String,
    val url:String,
    val packageId:String,
    val notes: String?="",
    val favicon: Bitmap? = null
)
