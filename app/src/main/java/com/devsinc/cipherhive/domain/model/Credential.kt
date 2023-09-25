package com.devsinc.cipherhive.domain.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credentials")
data class Credential(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val label: String,
    val username: String,
    val password: ByteArray,
    val url: String,
    val packageId: String? = "",
    val notes: String? = "",
    val favicon: Bitmap? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Credential

        if (id != other.id) return false
        if (label != other.label) return false
        if (username != other.username) return false
        if (!password.contentEquals(other.password)) return false
        if (url != other.url) return false
        if (packageId != other.packageId) return false
        if (notes != other.notes) return false
        if (favicon != other.favicon) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + label.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (packageId?.hashCode() ?: 0)
        result = 31 * result + (notes?.hashCode() ?: 0)
        result = 31 * result + (favicon?.hashCode() ?: 0)
        return result
    }
}
