package com.example.flutter_biometric_plugin.helper

import android.security.keystore.KeyGenParameterSpec.Builder
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object BioMetricHelper {

    // Function to create or get a key from the Keystore
    fun createBiometricKey(keyName: String, androidKeyStore: String) {
        try {
            val keyStore = KeyStore.getInstance(androidKeyStore)
            keyStore.load(null)

            // If key already exists, no need to create it again
            if (!keyStore.containsAlias(keyName)) {
                val keyGenerator = KeyGenerator.getInstance("AES", androidKeyStore)
                keyGenerator.init(
                    Builder(
                        keyName,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setUserAuthenticationRequired(true) 
                        .build()
                )
                keyGenerator.generateKey()
            }
        } catch (e: Exception) {
            Log.e("error", "Error generating key")
            e.printStackTrace()
        }
    }

    fun getCipher(keyName: String, androidKeyStore: String): Cipher? {
        return try {
            val keyStore = KeyStore.getInstance(androidKeyStore)
            keyStore.load(null)
            val secretKey = keyStore.getKey(keyName, null) as SecretKey
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher
        } catch (e: KeyPermanentlyInvalidatedException) {
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}