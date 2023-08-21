package com.devsinc.cipherhive.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devsinc.cipherhive.domain.model.Credential
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(credential: Credential)

    @Update(entity = Credential::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(credential: Credential)

    @Delete
    fun delete(credential: Credential)

    @Query("SELECT * FROM credentials ORDER BY label ASC")
    fun getAllCredentials(): Flow<List<Credential>>

    @Query("SELECT * FROM credentials WHERE url LIKE :url")
    fun getCredentialsByUrl(url: String): Flow<List<Credential>>
}
