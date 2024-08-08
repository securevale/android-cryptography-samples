package com.securevale.androidcryptosamples.derivation

import android.os.Build
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Sample Key-derivation implementation on Android.
 */
object KeyDerivation {

    /**
     * For salt generation please check the [com.securevale.androidcryptosamples.ui.KeyDerivationSampleFragment]
     */
    fun deriveKey(password: String, salt: ByteArray): SecretKey {

        /**
         * DISCLAIMER:
         * The iterationsCount value provided below is for sample purposes only and should not be considered secure.
         *
         * While OWASP recommends 210.000 iterations for PBKDF2withHmacSHA512 and 1.3 million for
         * PBKDF2-HMAC-SHA (see https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html#pbkdf2)
         * these values are likely to be too high for an Android device due to performance constraints.
         *
         * You need to choose a number of iterations that balances security and performance for your specific use case
         * (for guidance, see https://security.stackexchange.com/a/3993/284386).
         */
        val iterationCount = 12_000

        /**
         * In this example, we are assume that we are generating a key for AES-CBS, which requires 256 bits.
         * However, please note that your case may differ, so the derived key length might need to be of different size.
         */
        val keyLength = 256

        val spec = PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength)

        val factory = if (Build.VERSION.SDK_INT >= 26) {
            /**
             * A brief explanation of why SHA512 was used over SHA256:
             * https://stackoverflow.com/questions/11624372/best-practice-for-hashing-passwords-sha256-or-sha512
             * https://stackoverflow.com/questions/18080445/difference-between-hmacsha256-and-hmacsha512
             */
            SecretKeyFactory.getInstance("PBKDF2withHmacSHA512")
        } else {
            /**
             * Using PBKDF2withHmacSHA1 is not ideal, but it is unavoidable in this scenario,
             * given my assumption in this repository to rely on platform APIs rather than external dependencies.
             * Note that support for PBKDF2withHmacSHA512 is only available starting from API level 26:
             * https://developer.android.com/reference/javax/crypto/SecretKeyFactory.
             */
            SecretKeyFactory.getInstance("PBKDF2withHmacSHA1")
        }

        return factory.generateSecret(spec)
    }
}