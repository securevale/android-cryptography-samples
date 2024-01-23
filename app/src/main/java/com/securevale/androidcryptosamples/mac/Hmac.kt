package com.securevale.androidcryptosamples.mac

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyStore
import java.util.UUID
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey

/**
 * Sample HMAC implementation on Android.
 */
object Hmac {

    private val store: KeyStore by lazy {
        // Initialise Android Keystore.
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
    }

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
        KeyProperties.PURPOSE_SIGN
    ).build()

    // Get generated key from the Keystore or generate one if not present.
    private val key: Key
        get() = (store.getEntry(alias, null) as? KeyStore.SecretKeyEntry)?.secretKey
            ?: generateSecretKey()

    // Generate key.
    private fun generateSecretKey(): SecretKey =
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_HMAC_SHA256, "AndroidKeyStore").run {
            // No need to create SecureRandom and pass it manually as the KeyGenerator creates its own instance under the hood.
            init(spec)
            // Generate key and return it.
            generateKey()
        }

    /**
     * In order to verify integrity and authenticity of a message after creating the original hmac
     * you should generate it again with message you received and compare to the original one (whether they match).
     * You can check such a sample check in [com.securevale.androidcryptosamples.ui.HmacFragment].
     */
    fun computeHmac(data: String): String {
        // Create Mac instance and initialise it with chosen mode.
        val hmac = Mac.getInstance("HmacSHA256")

        // Init it providing secret key.
        hmac.init(key)

        // Calculate HMAC.
        val result = hmac.doFinal(data.toByteArray(Charset.defaultCharset()))

        /**
         * *Optional* Encode using Base64.
         *
         * Base64 encoding is used as a standard for encoding data that is sent between different parties
         * in order to preserve both data's format and information and allow it to be easily decoded and not corrupted during the transfer.
         */
        return Base64.encodeToString(result, Base64.DEFAULT)
    }

}