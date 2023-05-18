package com.devsinc.cipherhive.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devsinc.cipherhive.domain.model.BitmapTypeConverter
import com.devsinc.cipherhive.domain.model.Credential

@Database(entities = [Credential::class], version = 1, exportSchema = false)
@TypeConverters(BitmapTypeConverter::class)
abstract class CredentialDb: RoomDatabase() {
    abstract fun credentialDao(): CredentialDao
}
