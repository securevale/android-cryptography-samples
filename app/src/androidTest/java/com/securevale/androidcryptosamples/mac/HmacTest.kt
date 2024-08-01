package com.securevale.androidcryptosamples.mac

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class HmacTest {

    @Test
    fun hmacWhenCalculatedTwiceFromTheSameMessageIsTheSame() {
        val message = "message"

        val digest1 = Hmac.computeHmac(message)
        val digest2 = Hmac.computeHmac(message)

        assertEquals(digest1, digest2)
    }

    @Test
    fun hmacWhenCalculatedTwiceFromDifferentMessagesIsNotTheSame() {
        val message = "message"
        val anotherMessage = "different message"

        val digest1 = Hmac.computeHmac(message)
        val digest2 = Hmac.computeHmac(anotherMessage)

        assertNotEquals(digest1, digest2)
    }
}