package com.securevale.androidcryptosamples.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.advanced.biometric.BiometricCallback
import com.securevale.androidcryptosamples.advanced.biometric.Purpose
import com.securevale.androidcryptosamples.advanced.biometric.biometricAvailable
import com.securevale.androidcryptosamples.advanced.biometric.decryptWithBiometrics
import com.securevale.androidcryptosamples.advanced.biometric.encryptWithBiometrics

class FingerprintSampleFragment : Fragment() {

    private lateinit var operationResult: Pair<String, ByteArray>

    private lateinit var resultField: TextView

    private var mode = Purpose.ENCRYPTION

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(view)
    }

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

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
    private fun encrypt(data: String) {
        if (biometricAvailable(requireContext())) {
            if(data.isBlank()){
                resultField.text = "Nothing to encrypt, put text for encryption"
            }else{
                encryptWithBiometrics(this, data, callback)
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Biometric not available (or not enabled) on this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun decrypt(encryptedData: Pair<String, ByteArray>) {
        if (biometricAvailable(requireContext())) {
            decryptWithBiometrics(this, encryptedData, callback)
        } else {
            Toast.makeText(
                requireContext(),
                "Biometric not available (or not enabled) on this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val callback = object : BiometricCallback {
        override fun onError(errorCode: Int, errorString: CharSequence) {
            Toast.makeText(requireContext(), errorString, Toast.LENGTH_LONG).show()
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
