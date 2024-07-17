package com.securevale.androidcryptosamples.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.advanced.biometric.BiometricCallback
import com.securevale.androidcryptosamples.advanced.biometric.Purpose
import com.securevale.androidcryptosamples.advanced.biometric.biometricAvailable
import com.securevale.androidcryptosamples.advanced.biometric.decryptWithBiometrics
import com.securevale.androidcryptosamples.advanced.biometric.encryptWithBiometrics
import com.securevale.androidcryptosamples.databinding.SampleFragmentBinding
import com.securevale.androidcryptosamples.ui.dto.OperationResult
import com.securevale.androidcryptosamples.ui.lifecycle.bindWithLifecycle

class BiometricSampleFragment : Fragment() {

    private var operationResult: OperationResult = OperationResult()

    private var mode = Purpose.ENCRYPTION

    private var binding: SampleFragmentBinding by bindWithLifecycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SampleFragmentBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    private fun initFields() = with(binding) {
        encryptionBtn.setOnClickListener {
            encrypt(input.text.toString())
            mode = Purpose.ENCRYPTION
        }

        decryptionBtn.setOnClickListener {
            decrypt(operationResult)
            mode = Purpose.DECRYPTION
        }
    }

    private fun updateViews(mode: Purpose) = with(binding) {
        when (mode) {
            Purpose.ENCRYPTION -> {
                result.text = getString(R.string.encrypted, operationResult.data)
            }

            Purpose.DECRYPTION -> {
                result.text = getString(R.string.decrypted, operationResult.data)
            }
        }
    }

    private fun encrypt(data: String) {
        if (biometricAvailable(requireContext())) {
            if (data.isBlank()) {
                binding.result.text = getString(R.string.nothing_to_encrypt)
            } else {
                encryptWithBiometrics(this, data, callback)
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.biometric_not_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun decrypt(encryptedData: OperationResult) {
        if (biometricAvailable(requireContext())) {
            decryptWithBiometrics(this, encryptedData, callback)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.biometric_not_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val callback = object : BiometricCallback {
        override fun onError(errorCode: Int, errorString: CharSequence) {
            Toast.makeText(requireContext(), errorString, Toast.LENGTH_LONG).show()
            // Something went wrong, you need to handle accordingly.
        }

        override fun onFailed() {
            // Something went wrong, you need to handle accordingly.
        }

        override fun onSuccess(result: OperationResult) {
            operationResult = result
            updateViews(mode)
        }
    }
}
