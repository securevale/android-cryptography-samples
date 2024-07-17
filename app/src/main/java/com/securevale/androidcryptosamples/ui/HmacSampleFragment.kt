package com.securevale.androidcryptosamples.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.databinding.SampleFragmentBinding
import com.securevale.androidcryptosamples.mac.Hmac
import com.securevale.androidcryptosamples.ui.lifecycle.bindWithLifecycle

class HmacSampleFragment : Fragment() {

    private var hmac: String? = null

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
        input.hint = getString(R.string.hmac_hint)

        encryptionBtn.let {
            it.text = getString(R.string.hmac)
            it.setOnClickListener {
                val data = input.text.toString()
                if (data.isBlank()) {
                    result.text = getString(R.string.nothing_to_hmac)
                } else {
                    hmac = Hmac.computeHmac(data)

                    result.text = hmac
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
                    val computedHmacResult = Hmac.computeHmac(data)

                    result.text = getString(
                        if (computedHmacResult == hmac) R.string.valid_message else
                            R.string.invalid_message
                    )
                }
            }
        }
    }
}