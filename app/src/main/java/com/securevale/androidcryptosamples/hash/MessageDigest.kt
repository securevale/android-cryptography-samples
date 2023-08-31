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
         * Basically any algorithm from SHA-2 family NIST-approved algorithms can be used here as Android is currently not supporting SHA-3,
         * (NIST-approved hash functions list can be found here https://csrc.nist.gov/projects/hash-functions).
         */
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(data.toByteArray())
        return messageDigest.digest()
    }
}
