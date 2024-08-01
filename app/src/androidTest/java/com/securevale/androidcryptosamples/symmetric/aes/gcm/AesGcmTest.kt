package com.securevale.androidcryptosamples.symmetric.aes.gcm

import com.securevale.androidcryptosamples.encryption.symmetric.aes.gcm.AesGcm
import com.securevale.androidcryptosamples.testhelpers.clearTheKeystore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import javax.crypto.BadPaddingException

class AesGcmTest {

    @Test
    fun aesGcmEncryptsAndDecryptsCorrectly() {
        val message = "mymessage"

        val encrypted = AesGcm.encrypt(message)

        val decrypted = AesGcm.decrypt(encrypted.data, encrypted.iv)

        assertEquals(message, decrypted)
    }

    @Test
    fun whenKeyIsInvalidatedDecryptsThrowsAnError() {
        val message = "mymessage"

        val encrypted = AesGcm.encrypt(message)

        clearTheKeystore()

        assertThrows(BadPaddingException::class.java) {
            AesGcm.decrypt(encrypted.data, encrypted.iv)
        }
    }
}
