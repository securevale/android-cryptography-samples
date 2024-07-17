package com.securevale.androidcryptosamples.advanced.biometric

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.ui.dto.OperationResult
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Sample biometric-bound encryption on Android.
 *
 * While this instance employs the AES-GCM cipher, please note that BiometricPrompt.CryptoObject supports other Ciphers, Mac, and Signature objects.
 */

/**
 * Just hardcoded alias for sample purposes.
 */
private const val KEY_ALIAS = "biometricSample"

private val store: KeyStore by lazy {
    // Initialise Android Keystore.
    KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
}

/**
 * Create BiometricPrompt.
 */
fun createPrompt(
    fragment: Fragment,
    authenticationCallback: BiometricPrompt.AuthenticationCallback
): BiometricPrompt {
    val context = fragment.requireContext()

    val executor = ContextCompat.getMainExecutor(context)

    return BiometricPrompt(fragment, executor, authenticationCallback)
}

/**
 * Check whether this device supports biometric and there is any biometric's (fingerprint, face or iris) enrolled.
 *
 * For more detailed explanation why [BiometricManager.Authenticators.BIOMETRIC_STRONG] is the right choice, check the README.
 */
fun biometricAvailable(context: Context) = BiometricManager.from(context)
    .canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

// Encrypt using biometric-bound key.
fun encryptWithBiometrics(
    fragment: Fragment,
    data: String,
    callback: BiometricCallback
) {
    doCryptoOperationWithBiometric(fragment, Purpose.ENCRYPTION, data, null, callback)
}

// Decrypt using biometric-bound key.
fun decryptWithBiometrics(
    fragment: Fragment,
    encryptedData: OperationResult,
    callback: BiometricCallback
) {
    doCryptoOperationWithBiometric(
        fragment,
        Purpose.DECRYPTION,
        encryptedData.data,
        encryptedData.iv,
        callback
    )
}

// Get generated key from the Keystore or generate one if not present.
private val key: Key
    get() = (store.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey
        ?: generateSecretKey()

// Generate key.
private fun generateSecretKey(): SecretKey {
    /**
     * Set all parameters of the key that is to be generated.
     * This configuration mostly follows the AesGcm sample, with notable distinctions outlined below.
     * For further details, please refer to the AesGcm file
     * @see com.securevale.androidcryptosamples.encryption.symmetric.aes.gcm.AesGcm .
     *
     * keystoreAlias - the alias used to identify the key within the Keystore.
     * purposes - how the key will be used, for encryption and decryption in our case.
     */
    val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setKeySize(256)
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        /**
         * Set whether the key can be used only if the user has been authenticated.
         *
         * The key generation occurs exclusively when a secure lock screen is established.
         * When coupled with [KeyGenParameterSpec.Builder.setUserAuthenticationParameters] or [KeyGenParameterSpec.Builder.setUserAuthenticationValidityDurationSeconds],
         * at least one biometric must be registered.
         *
         * Furthermore, it is important to note that if the secure lock screen is deactivated post
         * key generation, the generated key will be irreversibly invalidated.
         * Any cryptographic operations attempted with such a key will result in KeyPermanentlyInvalidatedException.
         * These limitations applies only to secret and private key operations, public key ones are not restricted.
         */
        .setUserAuthenticationRequired(true)
        .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                /**
                 * Set whether this key should be invalidated on biometric enrollment
                 *
                 * This applies only to keys that requires authentication, established through [KeyGenParameterSpec.Builder.setUserAuthenticationRequired].
                 * Enabling this option results in irreversibly invalidation of the key if new biometric data is enrolled or if all existing biometric data is deleted.
                 */
                setInvalidatedByBiometricEnrollment(true)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                /**
                 * Set the duration (in seconds) and authorization type for which the key is authorized after user is successfully authenticated.
                 *
                 * There are two types available: [KeyProperties.AUTH_BIOMETRIC_STRONG] and [KeyProperties.AUTH_DEVICE_CREDENTIAL] for more detailed
                 * explanation on which type you should use check the README.
                 * If set to 0, user authentication must take place for every use of the key.
                 */
                setUserAuthenticationParameters(0, KeyProperties.AUTH_BIOMETRIC_STRONG)
            } else {
                /**
                 * Set the duration (in seconds), which the key is authorized to be used after user is successfully authenticated.
                 *
                 * If set to -1, user authentication must take place for every use of the key.
                 */
                setUserAuthenticationValidityDurationSeconds(-1)
            }
        }
        .build()

    return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").run {
        // No need to create SecureRandom and pass it manually as the KeyGenerator creates its own instance under the hood.
        init(spec)
        // Generate key and return it.
        generateKey()
    }
}

/**
 * Instantiate a Cipher object, initializing it with the selected mode, supplying the key and GCMParameterSpec (if applicable).
 * Then, encapsulate it within a BiometricPrompt.CryptoObject.
 */
private fun getCryptoObject(mode: Purpose, iv: ByteArray?) = when (mode) {
    Purpose.ENCRYPTION -> BiometricPrompt.CryptoObject(
        Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(Cipher.ENCRYPT_MODE, key)
        })

    Purpose.DECRYPTION -> {
        val gcmParameterSpec = GCMParameterSpec(128, iv)
        BiometricPrompt.CryptoObject(Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(Cipher.DECRYPT_MODE, key, gcmParameterSpec)
        })
    }
}

// Perform crypto operation.
private fun doCryptoOperationWithBiometric(
    fragment: Fragment,
    mode: Purpose,
    data: String,
    iv: ByteArray?,
    callback: BiometricCallback
) {
    // Prompt info configuration.
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(fragment.requireContext().getString(R.string.biometrics_prompt_title))
        /**
         * For more detailed explanation why [BiometricManager.Authenticators.BIOMETRIC_STRONG] is the right choice, check the README.
         */
        .setAllowedAuthenticators(BIOMETRIC_STRONG)
        .setConfirmationRequired(true)
        .setNegativeButtonText("Cancel")
        .build()

    // Callback for BiometricPrompt.
    val innerCallback = object : BiometricPrompt.AuthenticationCallback() {

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            callback.onError(errorCode, errString)
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            callback.onFailed()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            /**
             * Obtain biometric-bound cryptoObject (Cipher instance in our case).
             */
            val cipher = result.cryptoObject!!.cipher!!
            // Encrypt or decrypt based on the selected mode.
            val operationResult = when (mode) {
                Purpose.ENCRYPTION -> OperationResult(
                    Base64.encodeToString(
                        cipher.doFinal(data.toByteArray()),
                        Base64.DEFAULT
                    ),
                    cipher.iv
                )

                Purpose.DECRYPTION -> OperationResult(
                    String(
                        cipher.doFinal(
                            Base64.decode(
                                data,
                                Base64.DEFAULT
                            )
                        )
                    )
                )
            }
            callback.onSuccess(operationResult)
        }
    }

    // Create BiometricPrompt supplying the cryptoObject and show it.
    createPrompt(fragment, innerCallback).apply {
        authenticate(promptInfo, getCryptoObject(mode, iv))
    }
}

// A convenient interface for delivering results to the UI in a more organized manner.
interface BiometricCallback {
    fun onError(errorCode: Int, errorString: CharSequence)
    fun onFailed()
    fun onSuccess(result: OperationResult)
}

// Whether we want to encrypt or decrypt.
enum class Purpose {
    ENCRYPTION, DECRYPTION
}
