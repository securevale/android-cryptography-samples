package com.securevale.androidcryptosamples.encryption.symmetric.aes.gcm

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.securevale.androidcryptosamples.ui.dto.OperationResult
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyStore
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Sample AES-GCM implementation on Android.
 */
object AesGcm {

    private val store: KeyStore by lazy {
        // Initialise Android Keystore.
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
    }

    private const val AAD: String = "additional tag"

    /**
     *  Just random alias per session of the app for sample purposes.
     *  In real use cases, you should use something permanent as an alias.
     */
    private val alias: String = UUID.randomUUID().toString()

    /**
     * Set all parameters of the key that is to be generated.
     *
     * keystoreAlias - the alias used to identify the key within the Keystore.
     * purposes - how the key will be used, for encryption and decryption in our case.
     */
    private val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        alias,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        /**
         * Set the key size (for AES-GCM it can be 128, 192 and 256).
         *
         * We will use 256 bits for sample purposes, it's little slower than 128 but on Android we usually don't encrypt large data sets so it's not an issue.
         * For more detailed explanation which key size to use check the "FAQ" section in README.
         */
        .setKeySize(256)
        // The block mode (GCM in our case).
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        /**
         * Padding configuration for GCM.
         *
         * Internally, GCM is an implementation of CTR mode alongside with a polynomial hashing function applied to the ciphertext, so it doesn't need padding.
         * For more detailed explanation, check the GCM "mathematical basis" section on Wikipedia.
         */
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .build()

    // Get generated key from the Keystore or generate one if not present.
    private val key: Key
        get() = (store.getEntry(alias, null) as? KeyStore.SecretKeyEntry)?.secretKey
            ?: generateSecretKey()

    // Generate key.
    private fun generateSecretKey(): SecretKey =
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").run {
            // No need to create SecureRandom and pass it manually as the KeyGenerator creates its own instance under the hood.
            init(spec)
            // Generate key and return it.
            generateKey()
        }

    fun encrypt(data: String): OperationResult {
        // Create Cipher instance and init it in encryption mode supplying the key.
        val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(Cipher.ENCRYPT_MODE, key)
        }

        /**
         * *Optional* Use Additional Authenticated Data (AAD).
         *
         * For more detailed explanation, check the README.
         */
        cipher.updateAAD(AAD.toByteArray())

        // Encrypt.
        val encrypted = cipher.doFinal(data.toByteArray(Charset.defaultCharset()))

        /**
         * *Optional* Encode using Base64.
         *
         * Base64 encoding is used as a standard for encoding data that is sent between different parties
         * in order to preserve both data's format and information and allow it to be easily decoded and not corrupted during the transfer.
         */
        val encoded = Base64.encodeToString(encrypted, Base64.DEFAULT)

        /**
         * Return the Base64-encoded ciphertext alongside with the Initialization Vector (IV).
         * IV needs to be saved alongside with the ciphertext as it will be needed for decryption.
         * The most common way to do it is just to append/prepend IV to the ciphertext itself.
         */
        return OperationResult(
            encoded,
            cipher.iv
        )
    }

    fun decrypt(ciphertext: String, iv: ByteArray): String {
        /**
         * GCMParameterSpec with authentication tag length (in bits so 16 * 8 = 128) and the IV.
         * The generated tag is always 16 bytes long.
         */
        val gcmParameterSpec = GCMParameterSpec(128, iv)

        // Create Cipher instance and init it in decryption mode supplying key and GCMParameterSpec.
        val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(Cipher.DECRYPT_MODE, key, gcmParameterSpec)
        }

        // Decode Base64-encoded ciphertext to raw bytes.
        val decoded = Base64.decode(ciphertext, Base64.DEFAULT)

        // *Optional* Update AAD.
        cipher.updateAAD(AAD.toByteArray())

        // Decrypt.
        val decrypted = cipher.doFinal(decoded)

        // Return the decrypted plaintext as a String.
        return String(decrypted)
    }
}
