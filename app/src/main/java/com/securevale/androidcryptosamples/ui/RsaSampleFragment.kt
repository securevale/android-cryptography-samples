package com.securevale.androidcryptosamples.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.databinding.SampleFragmentBinding
import com.securevale.androidcryptosamples.encryption.assymetric.rsa.Rsa
import com.securevale.androidcryptosamples.ui.dto.OperationResult
import com.securevale.androidcryptosamples.ui.lifecycle.bindWithLifecycle

class RsaSampleFragment : Fragment() {

    private var encryptionResult: OperationResult = OperationResult()
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
            val text = input.text.toString()
            if (text.isEmpty()) {
                result.text = getString(R.string.nothing_to_encrypt)
            } else {
                encryptionResult = OperationResult(Rsa.encrypt(input.text.toString()))
                result.text = getString(R.string.encrypted, encryptionResult.data)
            }
        }

        decryptionBtn.setOnClickListener {
            if (encryptionResult.hasNoData()) {
                result.text = getString(R.string.nothing_to_decrypt)
            } else {
                val decrypted = OperationResult(Rsa.decrypt(encryptionResult.data))
                result.text = getString(R.string.decrypted, decrypted.data)
            }
        }
    }
}
