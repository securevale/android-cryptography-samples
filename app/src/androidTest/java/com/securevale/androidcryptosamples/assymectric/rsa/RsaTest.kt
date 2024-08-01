package com.securevale.androidcryptosamples.assymectric.rsa

import android.security.keystore.KeyPermanentlyInvalidatedException
import com.securevale.androidcryptosamples.encryption.assymetric.rsa.Rsa
import com.securevale.androidcryptosamples.testhelpers.clearTheKeystore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RsaTest {

    @Test
    fun rsaEncryptsAndDecryptsCorrectly() {
        val message = "mymessage"

        val encrypted = Rsa.encrypt(message)

        val decrypted = Rsa.decrypt(encrypted)

        assertEquals(message, decrypted)
    }

    @Test
    fun whenKeyIsInvalidatedDecryptsThrowsAnError() {
        val message = "mymessage"

        val encrypted = Rsa.encrypt(message)

        clearTheKeystore()

        assertThrows(KeyPermanentlyInvalidatedException::class.java) {
            Rsa.decrypt(encrypted)
        }
    }
}