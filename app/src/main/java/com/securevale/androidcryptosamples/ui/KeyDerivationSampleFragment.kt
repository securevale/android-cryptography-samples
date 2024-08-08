package com.securevale.androidcryptosamples.ui

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.databinding.SampleFragmentBinding
import com.securevale.androidcryptosamples.derivation.KeyDerivation
import com.securevale.androidcryptosamples.ui.dto.OperationResult
import com.securevale.androidcryptosamples.ui.lifecycle.bindWithLifecycle
import java.nio.charset.Charset
import java.security.SecureRandom
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

class KeyDerivationSampleFragment : Fragment() {

    private var encryptionResult: OperationResult = OperationResult()
    private var binding: SampleFragmentBinding by bindWithLifecycle()

    private val sampleMessage = "This is your decrypted message!"

    private val salt: ByteArray by lazy {
        /**
         * Create salt using random generator, as it should be unique for each encryption key created.
         * Store the salt used for each generated key, and ensure you use the same salt during decryption.
         */
        val secureRandom = SecureRandom()

        /**
         * Public key cryptography standard recommends salt length of at least 64 bits https://datatracker.ietf.org/doc/html/rfc8018#section-4.
         */
        val salt = ByteArray(512)

        secureRandom.nextBytes(salt)

        salt
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SampleFragmentBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    private fun initFields() = with(binding) {

        input.hint = getString(R.string.key_derivation_hint)

        encryptionBtn.setOnClickListener {
            val password = input.text.toString()

            if (password.isBlank()) {
                result.text = getString(R.string.no_password_to_derive_key_from)
            } else {
                encryptionResult = encrypt(password)
                result.text = getString(R.string.encrypted, encryptionResult.data)
            }
        }

        decryptionBtn.setOnClickListener {
            if (encryptionResult.hasNoData()) {
                result.text = getString(R.string.nothing_to_decrypt)
            } else {
                val password = input.text.toString()

                if (password.isBlank()) {
                    result.text = getString(R.string.no_password_to_derive_key_from)
                } else {
                    val decrypted = decrypt(password)

                    if (decrypted.isBlank()) {
                        result.text = getString(R.string.decryption_with_derived_key_failed)
                    } else {
                        result.text = getString(
                            R.string.decrypted,
                            decrypted
                        )
                    }
                }
            }
        }
    }

    // Separated methods for encryption/decryption to keep the sample clear and simple.
    private fun encrypt(password: String): OperationResult {
        val derivedKey = KeyDerivation.deriveKey(password, salt)

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.ENCRYPT_MODE, derivedKey)
        }

        val encoded = Base64.encodeToString(
            cipher.doFinal(sampleMessage.toByteArray(Charset.defaultCharset())),
            Base64.DEFAULT
        )

        return OperationResult(encoded, cipher.iv)
    }

    private fun decrypt(password: String): String {
        val derivedKey = KeyDerivation.deriveKey(password, salt)

        val ivParameterSpec = IvParameterSpec(encryptionResult.iv)

        // I used AES-CBC as an example, but you can use the derived key with other ciphers, adjusting the parameters as needed for your specific use case.
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.DECRYPT_MODE, derivedKey, ivParameterSpec)
        }

        val decoded = Base64.decode(encryptionResult.data, Base64.DEFAULT)


        return try {
            String(cipher.doFinal(decoded))
        } catch (e: BadPaddingException) {
            ""
        }
    }
}