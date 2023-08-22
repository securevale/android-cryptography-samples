package com.securevale.androidcryptosamples.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.advanced.biometric.BiometricCallback
import com.securevale.androidcryptosamples.advanced.biometric.Purpose
import com.securevale.androidcryptosamples.advanced.biometric.biometricAvailable
import com.securevale.androidcryptosamples.advanced.biometric.decryptWithBiometrics
import com.securevale.androidcryptosamples.advanced.biometric.encryptWithBiometrics

class FingerprintFragment : Fragment() {

    private lateinit var operationResult: Pair<String, ByteArray>

    private lateinit var resultField: TextView

    private var mode = Purpose.ENCRYPTION

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(view)
    }

    @SuppressLint("SetTextI18n")
    private fun initFields(view: View) {
        resultField = view.findViewById(R.id.result)
        val input = view.findViewById<EditText>(R.id.input)

        view.findViewById<Button>(R.id.encryption_btn).setOnClickListener {
            encrypt(input.text.toString())
            mode = Purpose.ENCRYPTION
        }

        view.findViewById<Button>(R.id.decryption_btn).setOnClickListener {
            decrypt(operationResult)
            mode = Purpose.DECRYPTION
        }
    }

    private fun reloadViews(mode: Purpose) {
        if (!this::operationResult.isInitialized) {
            resultField.text = "Nothing to decrypt, encrypt first"
        } else {
            when (mode) {
                Purpose.ENCRYPTION -> {
                    resultField.text = "Encrypted: ${operationResult.first}"
                }

                Purpose.DECRYPTION -> {
                    resultField.text = "Decrypted: ${operationResult.first}"
                }
            }
        }
    }

    private fun encrypt(data: String) {
        if (biometricAvailable(requireContext())) {
            encryptWithBiometrics(this, data, callback)
        }
    }

    private fun decrypt(encryptedData: Pair<String, ByteArray>) {
        if (biometricAvailable(requireContext())) {
            decryptWithBiometrics(this, encryptedData, callback)
        }
    }

    private val callback = object : BiometricCallback {
        override fun onError(errorCode: Int, errorString: CharSequence) {
            // Something went wrong, handle accordingly.
        }

        override fun onFailed() {
            // Something went wrong, handle accordingly.
        }

        override fun onSuccess(result: Pair<String, ByteArray>) {
            operationResult = result
            reloadViews(mode)
        }
    }
}
