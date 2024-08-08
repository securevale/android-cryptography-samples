package com.securevale.androidcryptosamples.derivation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.nio.charset.Charset
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

class KeyDerivationTest {

    private val testMessage = "message"

    @Test
    fun whenKeyDerivedFromTheSamePasswordDecryptsCorrectly() {
        val derivedKey = KeyDerivation.deriveKey("password", "some_salt".toByteArray())

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.ENCRYPT_MODE, derivedKey)
        }

        val encrypted = cipher.doFinal(testMessage.toByteArray(Charset.defaultCharset()))

        val iv = cipher.iv

        val secondDerivedKey = KeyDerivation.deriveKey("password", "some_salt".toByteArray())

        val ivParameterSpec = IvParameterSpec(iv)

        val decryptionCipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.DECRYPT_MODE, secondDerivedKey, ivParameterSpec)
        }

        val decrypted = decryptionCipher.doFinal(encrypted)
        assertEquals(testMessage, String(decrypted))
    }

    @Test
    fun whenKeyDerivedFromDifferentPasswordsDecryptionFails() {
        val derivedKey = KeyDerivation.deriveKey("password", "some_salt".toByteArray())

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.ENCRYPT_MODE, derivedKey)
        }

        val encrypted = cipher.doFinal(testMessage.toByteArray(Charset.defaultCharset()))

        val iv = cipher.iv

        val secondDerivedKey =
            KeyDerivation.deriveKey("different_password", "some_salt".toByteArray())

        val ivParameterSpec = IvParameterSpec(iv)

        val decryptionCipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.DECRYPT_MODE, secondDerivedKey, ivParameterSpec)
        }

        assertThrows(BadPaddingException::class.java) {
            decryptionCipher.doFinal(encrypted)
        }
    }

    @Test
    fun whenKeyDerivedFromTheSamePasswordButDifferentSaltDecryptionFails() {
        val derivedKey = KeyDerivation.deriveKey("password", "some_salt".toByteArray())

        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.ENCRYPT_MODE, derivedKey)
        }

        val encrypted = cipher.doFinal(testMessage.toByteArray(Charset.defaultCharset()))

        val iv = cipher.iv

        val secondDerivedKey =
            KeyDerivation.deriveKey("password", "different_salt".toByteArray())

        val ivParameterSpec = IvParameterSpec(iv)

        val decryptionCipher = Cipher.getInstance("AES/CBC/PKCS7Padding").apply {
            init(Cipher.DECRYPT_MODE, secondDerivedKey, ivParameterSpec)
        }

        assertThrows(BadPaddingException::class.java) {
            decryptionCipher.doFinal(encrypted)
        }
    }


}