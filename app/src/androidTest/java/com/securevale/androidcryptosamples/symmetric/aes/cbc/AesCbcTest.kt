package com.securevale.androidcryptosamples.symmetric.aes.cbc

import com.securevale.androidcryptosamples.encryption.symmetric.aes.cbc.AesCbc
import com.securevale.androidcryptosamples.testhelpers.clearTheKeystore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import javax.crypto.BadPaddingException

class AesCbcTest {

    @Test
    fun aesCbcEncryptsAndDecryptsCorrectly() {
        val message = "mymessage"

        val encrypted = AesCbc.encrypt(message)

        val decrypted = AesCbc.decrypt(encrypted.data, encrypted.iv)

        assertEquals(message, decrypted)
    }

    @Test
    fun whenKeyIsInvalidatedDecryptsThrowsAnError() {
        val message = "mymessage"

        val encrypted = AesCbc.encrypt(message)

        clearTheKeystore()

        assertThrows(BadPaddingException::class.java) {
            AesCbc.decrypt(encrypted.data, encrypted.iv)
        }
    }
}