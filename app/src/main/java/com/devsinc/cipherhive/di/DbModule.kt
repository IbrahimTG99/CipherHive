package com.devsinc.cipherhive.di

import android.content.Context
import androidx.room.Room
import com.devsinc.cipherhive.data.db.CredentialDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun provideCredentialDbInstance(@ApplicationContext context: Context): CredentialDb {
        return Room.databaseBuilder(
            context, CredentialDb::class.java, "Credential_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesCredentialDaoInstance(database: CredentialDb) = database.credentialDao()
}
