package com.securevale.androidcryptosamples.testhelpers

import java.security.KeyStore

fun clearTheKeystore() {
    val keystore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    val aliases = keystore.aliases()

    for (alias in aliases) {
        keystore.deleteEntry(alias)
    }
}
