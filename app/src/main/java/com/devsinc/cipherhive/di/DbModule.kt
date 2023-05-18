package com.devsinc.cipherhive.di

import android.content.Context
import androidx.room.Room
import com.devsinc.cipherhive.data.db.CredentialDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    fun provideCredentialDbInstance(@ApplicationContext context: Context): CredentialDb {
        return Room.databaseBuilder(
            context, CredentialDb::class.java, "Credential_database"
        ).build()
    }

    @Provides
    fun providesCredentialDaoInstance(database: CredentialDb) = database.credentialDao()
}
