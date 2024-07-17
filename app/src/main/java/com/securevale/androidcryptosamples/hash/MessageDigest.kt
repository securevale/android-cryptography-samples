package com.securevale.androidcryptosamples.hash

import java.security.MessageDigest

/**
 * Sample MessageDigest implementation on Android.
 */
object MessageDigest {

    fun calculateDigest(data: String): ByteArray {
        /**
         * Create MessageDigest instance using provided algorithm, calculate digest and return it.
         *
         * SHA-256 is used as one of NIST-approved hash functions.
         * Any algorithm from the NIST-approved SHA-2 family can be used here, as Android currently does not support SHA-3
         * (list of the NIST-approved hash functions can be found here https://csrc.nist.gov/projects/hash-functions).
         */
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(data.toByteArray())
        return messageDigest.digest()
    }
}
