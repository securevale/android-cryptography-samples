package com.securevale.androidcryptosamples.encryption.assymetric.rsa

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.UUID
import javax.crypto.Cipher

/**
 * Sample RSA implementation on Android.
 */
object Rsa {

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
     * purposes - how the key will be used, for signing and verification in our case.
     */
    private val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        alias,
        KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
    )
        /**
         * Set the key size.
         *
         * The current recommended RSA key size is 2048 bits, but if you want to be more future proof consider 3072 or 4096 bits.
         * It is worth noting that RSA can only encrypt data of which size does not exceed the selected key size (2048 bits = 256 bytes in our case) minus
         * any padding and header data.
         * For more detailed explanation check the "FAQ" section in README.
         */
        .setKeySize(2048)
        /**
         * Set mode of operation.
         *
         * It is not implementing the ECB mode (like in AES) as it encrypts/decrypts only single block of data.
         * The BLOCK_MODE_ECB is used just to mimic the cipher string for block ciphers.
         */
        .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
        .build()

    // Get generated keypair from the Keystore or generate one if not present.
    private val keyPair: KeyPair
        get() {
            val privateKey = store.getKey(alias, null) as PrivateKey?
            val publicKey = store.getCertificate(alias)?.publicKey

            return if (privateKey != null && publicKey != null) {
                KeyPair(publicKey, privateKey)
            } else {
                generateKeyPair()
            }
        }

    // Get the public key.
    private val publicKey = keyPair.public

    // Get the private key.
    private val privateKey = keyPair.private

    // Generate key pair.
    private fun generateKeyPair(): KeyPair =
        KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore").run {
            // Initialise with spec.
            initialize(spec)
            // Generate key pair and return it.
            generateKeyPair()
        }

    fun encrypt(data: String): String {
        /**
         * Create Cipher instance and init it in encryption mode supplying the public key.
         *
         * DISCLAIMER:
         * The OAEPPadding should be chosen over the PKCS1Padding (see https://crypto.stackexchange.com/questions/47436/how-much-safer-is-rsa-oaep-compared-to-rsa-with-pkcs1-v1-5-padding),
         * but its use involves strange workarounds due to a bug in Android (see https://issuetracker.google.com/issues/36708951#comment15). This problem will be
         * addressed in the future as a separate sample, let's stick to the PKCS1Padding for now.
         *
         * Might be also RSA/NONE/PKCS1Padding as the "ECB" part is only used as a placeholder for block cipher and there is no block mode used under the hood,
         * thus "NONE" also indicates that there is no mode of operation used here.
         */
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
            init(Cipher.ENCRYPT_MODE, publicKey)
        }

        // Encrypt.
        val encrypted = cipher.doFinal(data.toByteArray())

        /**
         * Return the Base64-encoded ciphertext.
         * Encoding is optional. You could just as well return byteArray if it better fits your needs.
         */
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }


    fun decrypt(ciphertext: String): String {
        /**
         * Create Cipher instance and init it in decryption mode supplying the private key.
         *
         * Might be also RSA/NONE/PKCS1Padding as the "ECB" part is only used as a placeholder for block cipher and there is no block mode used under the hood,
         * thus "NONE" also indicates that there is no mode of operation used here.
         */
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
            init(Cipher.DECRYPT_MODE, privateKey)
        }

        // Decode Base64-encoded ciphertext to raw bytes.
        val decoded = Base64.decode(ciphertext, Base64.DEFAULT)

        // Decrypt.
        val decrypted = cipher.doFinal(decoded)

        // Return the decrypted plaintext as a String.
        return String(decrypted)
    }
}
