package com.devsinc.cipherhive.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager(private val keyAlias: String) {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    private fun getDecryptCipherForIv(iv: ByteArray) = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(BLOCK_MODE).setEncryptionPaddings(PADDING)
                    .setRandomizedEncryptionRequired(true).setUserAuthenticationRequired(false)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(data: ByteArray): ByteArray {
        val encryptedData = encryptCipher.doFinal(data)
        val result = ByteArray(1 + encryptCipher.iv.size + 1 + encryptedData.size)
        result[0] = encryptCipher.iv.size.toByte()
        encryptCipher.iv.copyInto(result, 1)
        result[1 + encryptCipher.iv.size] = encryptedData.size.toByte()
        encryptedData.copyInto(result, 1 + encryptCipher.iv.size + 1)
        Log.d("CryptoManager", "encrypt: data: ${result.contentToString()}")
        return result
    }

    fun decrypt(data: ByteArray): String {
        Log.d("CryptoManager", "decrypt: data: ${data.contentToString()}")
        val ivSize = data[0].toInt()
        val iv = data.copyOfRange(1, 1 + ivSize)
        val encryptedDataSize = data[1 + ivSize].toInt()
        val encryptedData = data.copyOfRange(1 + ivSize + 1, 1 + ivSize + 1 + encryptedDataSize)
        return String(getDecryptCipherForIv(iv).doFinal(encryptedData))
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}
