package com.devsinc.cipherhive.domain.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class BitmapTypeConverter {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        if (byteArray == null) return null

        val inputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(inputStream)
    }
}
