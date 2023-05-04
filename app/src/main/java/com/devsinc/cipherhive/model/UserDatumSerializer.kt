package com.devsinc.cipherhive.model

import androidx.datastore.core.Serializer
import com.devsinc.cipherhive.CryptoManager
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class UserDatumSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<UserDatum> {
    override val defaultValue: UserDatum = UserDatum()

    override suspend fun readFrom(input: InputStream): UserDatum {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(
                deserializer = UserDatum.serializer(), string = decryptedBytes.decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserDatum, output: OutputStream) {
        cryptoManager.encrypt(
            data = Json.encodeToString(
                serializer = (UserDatum.serializer()), value = t
            ).encodeToByteArray(), outputStream = output
        )
    }
}
