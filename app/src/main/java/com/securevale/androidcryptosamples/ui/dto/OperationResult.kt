package com.securevale.androidcryptosamples.ui.dto

@Suppress("ArrayInDataClass")
data class OperationResult(
    val data: String = "",
    val iv: ByteArray = byteArrayOf()
) {

    fun hasNoData() = data.isBlank()
}
