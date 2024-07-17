package com.securevale.androidcryptosamples.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.databinding.SampleFragmentBinding
import com.securevale.androidcryptosamples.signature.Signature
import com.securevale.androidcryptosamples.ui.lifecycle.bindWithLifecycle

class SignatureSampleFragment : Fragment() {

    private var signature: ByteArray? = null
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
        input.hint = getString(R.string.signing_hint)

        encryptionBtn.apply {
            text = getString(R.string.sign)
            setOnClickListener {
                val data = input.text.toString()
                if (data.isBlank()) {
                    result.text = getString(R.string.nothing_to_sign)
                } else {
                    signature = Signature.sign(data.toByteArray())
                    result.text = getString(R.string.signed)
                }
            }
        }

        decryptionBtn.apply {
            text = getString(R.string.verify)
            setOnClickListener {
                val data = input.text.toString()
                if (data.isBlank()) {
                    result.text = getString(R.string.nothing_to_verify)
                } else {
                    val verificationResult = Signature.verify(data.toByteArray(), signature!!)

                    val verdict =
                        getString(if (verificationResult) R.string.valid_signature else R.string.invalid_signature)

                    result.text = verdict
                }
            }
        }
    }
}
