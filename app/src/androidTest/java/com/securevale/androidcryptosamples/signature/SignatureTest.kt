package com.securevale.androidcryptosamples.signature

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SignatureTest {

    @Test
    fun signatureProperlyGeneratedAndVerified() {
        val message = "message"

        val signature = Signature.sign(message.toByteArray())

        val verificationResult = Signature.verify(message.toByteArray(), signature)

        assertTrue(verificationResult)
    }

    @Test
    fun signatureVerificationFailsWithDifferentMessage() {
        val message = "message"

        val signature = Signature.sign(message.toByteArray())

        val verificationResult = Signature.verify("new message".toByteArray(), signature)

        assertFalse(verificationResult)
    }

    @Test
    fun signatureVerificationFailsWithDifferentSignature() {
        val message = "message"

        val signature1 = Signature.sign("new message".toByteArray())

        val verificationResult = Signature.verify(message.toByteArray(), signature1)

        assertFalse(verificationResult)
    }
}