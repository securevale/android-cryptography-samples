package com.securevale.androidcryptosamples.helpers

import android.util.Log
import java.security.Security

// List all providers available.
fun listProviders() {
    for (provider in Security.getProviders()) {
        Log.d("Provider ", provider.name)
    }
}

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

// List available strong SecureRandom(s).
fun listStrongRandom() =
    Log.d("securevale: StrongRandom ", Security.getProperty("securerandom.strongAlgorithms"))