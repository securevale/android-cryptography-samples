package com.securevale.androidcryptosamples.helpers

import android.util.Log
import java.security.Security

// List all providers available.
fun listProviders() {
    for (provider in Security.getProviders()) {
        Log.d("Provider ", provider.name)
    }
}

// List supported algorithms applying filter.
fun listSupportedAlgorithms(filter: String = "") {
    for (provider in Security.getProviders()) {
        val name = provider.name

        for (service in provider.services) {
            if (filter.isBlank()) {
                Log.d("$name algorithm: ", service.algorithm)
            } else {
                val algorithm = service.algorithm
                if (algorithm.contains(filter)) {
                    Log.d("$name algorithm: ", service.algorithm)

                }
            }
        }
    }
}

// List supported MessageDigest algorithms.
fun listMessageDigestAlgorithms() =
    Log.d("securevale: MessageDigest ", Security.getAlgorithms("MessageDigest").toString())

// List supported Cipher algorithms.
fun listCipherAlgorithms() =
    Log.d("securevale: Cipher ", Security.getAlgorithms("Cipher").toString())

// List supported Signature algorithms.
fun listSignatureAlgorithms() =
    Log.d("securevale: Signature ", Security.getAlgorithms("Signature").toString())

// List supported Mac algorithms.
fun listMacAlgorithms() =
    Log.d("securevale: Mac ", Security.getAlgorithms("Mac").toString())

// List available strong SecureRandom(s).
fun listStrongRandom() =
    Log.d("securevale: StrongRandom ", Security.getProperty("securerandom.strongAlgorithms"))