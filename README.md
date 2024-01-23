# Android Crypto Samples

This repo is a collection of code samples that supplement
the [official documentation](https://developer.android.com/guide/topics/security/cryptography),
explaining how to correctly implement common cryptographic operations on Android.

### Symmetric encryption

- [AES-CBC](https://github.com/securevale/android-crypto-samples/blob/master/app/src/main/java/com/securevale/androidcryptosamples/encryption/symmetric/aes/cbc/AesCbc.kt)
- [AES-ECB](https://github.com/securevale/android-crypto-samples/blob/master/app/src/main/java/com/securevale/androidcryptosamples/encryption/symmetric/aes/ecb/AesEcb.kt)
- [AES-GCM](https://github.com/securevale/android-crypto-samples/blob/master/app/src/main/java/com/securevale/androidcryptosamples/encryption/symmetric/aes/gcm/AesGcm.kt)

### Asymmetric encryption

- [RSA](https://github.com/securevale/android-crypto-samples/blob/master/app/src/main/java/com/securevale/androidcryptosamples/encryption/assymetric/rsa/Rsa.kt)

### Message digest

- [Message Digest](https://github.com/securevale/android-crypto-samples/blob/master/app/src/main/java/com/securevale/androidcryptosamples/hash/MessageDigest.kt)

### Signature

- [Signing/Verifying](https://github.com/securevale/android-crypto-samples/blob/master/app/src/main/java/com/securevale/androidcryptosamples/signature/Signature.kt)

### MAC

- [HMAC](https://github.com/securevale/android-crypto-samples/blob/master/app/src/main/java/com/securevale/androidcryptosamples/mac/Hmac.kt)

### Advanced use cases

- [Biometric-bound encryption](https://github.com/securevale/android-crypto-samples/blob/master/app/src/main/java/com/securevale/androidcryptosamples/advanced/biometric/Biometric.kt)

> **Note**
> This is still WIP. The collection of code samples will be continuously updated with other
> cryptographic algorithms and more advanced use cases.

## FAQ

- [What key size should I use for AES?](https://crypto.stackexchange.com/questions/5118/is-aes-256-weaker-than-192-and-128-bit-versions?rq=1)
- [What key size should I use for RSA?](https://stackoverflow.com/questions/589834/what-rsa-key-length-should-i-use-for-my-ssl-certificates/589850#589850)
- [What is AAD in AES-GCM and what is it used for?](https://crypto.stackexchange.com/questions/89303/what-is-auth-data-in-aes-gcm/89306#89306)
- [What padding should be used for AES-CBC?](https://crypto.stackexchange.com/a/48631/107088)
- [Why should I use Base64 for encoding?](https://stackoverflow.com/questions/3538021/why-do-we-use-base64)
- [What are the reasons to consider utilizing biometrics paired with a CryptoObject?](https://medium.com/androiddevelopers/using-biometricprompt-with-cryptoobject-how-and-why-aace500ccdb7)
- [Why should I avoid using 'pure' cryptographic hash functions for password hashing?](https://security.stackexchange.com/questions/195563/why-is-sha-256-not-good-for-passwords)
- [What is the main difference between Keystore and Keychain?](https://developer.android.com/privacy-and-security/keystore#WhichShouldIUse)

## Glossary

- [AES-CBC](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_block_chaining_(CBC))
- [AES-ECB](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Electronic_codebook_(ECB))
- [AES-GCM](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Galois/counter_(GCM))
- [Android Keystore](https://developer.android.com/training/articles/keystore)
- [Authenticated Encryption](https://en.wikipedia.org/wiki/Authenticated_encryption)
- [Biometric Security Measure Methodology](https://source.android.com/docs/security/features/biometric/measure)
- [Block Cipher Mode Of Operation](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation)
- [Crypto Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cryptographic_Storage_Cheat_Sheet.html)
- [Cryptographic Hash Function](https://en.wikipedia.org/wiki/Cryptographic_hash_function)
- [Digital Signature](https://en.wikipedia.org/wiki/Digital_signature)
- [HMAC](https://en.wikipedia.org/wiki/HMAC)
- [Initialization Vector](https://en.wikipedia.org/wiki/Initialization_vector)
- [Message Digest](https://csrc.nist.gov/glossary/term/message_digest)
- [Message Authentication Code](https://en.wikipedia.org/wiki/Message_authentication_code)
- [Padding](https://en.wikipedia.org/wiki/Padding_(cryptography))
- [Public-Key Cryptography](https://en.wikipedia.org/wiki/Public-key_cryptography)
- [PRNG](https://en.wikipedia.org/wiki/Pseudorandom_number_generator)
- [RSA](https://en.wikipedia.org/wiki/RSA_(cryptosystem))
- [Semantic Security](https://en.wikipedia.org/wiki/Semantic_security)
- [Symmetric Cryptography](https://en.wikipedia.org/wiki/Symmetric-key_algorithm)