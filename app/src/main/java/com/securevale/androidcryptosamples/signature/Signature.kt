package com.securevale.androidcryptosamples.signature

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Signature
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.security.auth.x500.X500Principal

/**
 * Sample Signing/Verifying with RSA implementation on Android.
 */
object Signature {

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
    private fun spec(notBefore: Date, notAfter: Date): KeyGenParameterSpec =
        KeyGenParameterSpec.Builder(
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
             * Set digest with which the key can be used.
             */
            .setDigests(KeyProperties.DIGEST_SHA256)
            /**
             * Key's validity start date. It's just a sample, in real scenarios you should use date that best suits your use-cases.
             */
            .setKeyValidityStart(notBefore)
            /**
             * Key's validity end date. It's just a sample, in real scenarios you should use date that best suits your use-cases.
             */
            .setKeyValidityEnd(notAfter)
            /**
             * CertificateSubject. It's just a sample, in real scenarios you should use subject that best suits your use-cases.
             */
            .setCertificateSubject(X500Principal("CN=example"))
            /**
             * *Optional* It defaults to one.
             */
            .setCertificateSerialNumber(BigInteger.ONE)
            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            .build()


    fun sign(data: ByteArray): ByteArray {
        // Create Signature instance and init it with chosen mode.
        val signature = Signature.getInstance("SHA256withRSA")

        // Init in signing mode supplying the private key.
        signature.initSign(privateKey)

        // Update the Signature instance with data to sign.
        signature.update(data)

        // Sign.
        return signature.sign()
    }

    fun verify(data: ByteArray, sign: ByteArray): Boolean {
        // Create Signature instance and init it with chosen mode.
        val signature = Signature.getInstance("SHA256withRSA")

        // Init in verification mode supplying the public key.
        signature.initVerify(publicKey)

        // Update the Signature instance with data to verify.
        signature.update(data)

        // Verify
        return signature.verify(sign)
    }

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
            initialize(
                spec(
                    Calendar.getInstance().apply { add(Calendar.YEAR, -1) }.time,
                    Calendar.getInstance().apply { add(Calendar.YEAR, 5) }.time
                )
            )
            // Generate key pair and return it.
            generateKeyPair()
        }
}
