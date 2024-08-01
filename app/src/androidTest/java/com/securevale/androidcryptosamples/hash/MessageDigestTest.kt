package com.securevale.androidcryptosamples.hash

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Test

class MessageDigestTest {

    @Test
    fun digestWhenCalculatedTwiceFromTheSameMessageIsTheSame() {
        val message = "message"

        val digest1 = MessageDigest.calculateDigest(message)
        val digest2 = MessageDigest.calculateDigest(message)

        assertArrayEquals(digest1, digest2)
        assertEquals(String(digest1), String(digest2))
    }

    @Test
    fun digestWhenCalculatedTwiceFromDifferentMessagesIsNotTheSame() {
        val message = "message"
        val anotherMessage = "message1"

        val digest1 = MessageDigest.calculateDigest(message)
        val digest2 = MessageDigest.calculateDigest(anotherMessage)

        assertFalse(digest1.contentEquals(digest2))
        assertNotEquals(String(digest1), String(digest2))
    }
}
