package com.securevale.androidcryptosamples.ui

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.databinding.SampleFragmentBinding
import com.securevale.androidcryptosamples.hash.MessageDigest
import com.securevale.androidcryptosamples.ui.lifecycle.bindWithLifecycle

class MessageDigestSampleFragment : Fragment() {

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
        input.hint = getString(R.string.digest_hint)
        encryptionBtn.apply {
            text = getString(R.string.digest)
            setOnClickListener {
                val data = input.text.toString()
                if (data.isBlank()) {
                    result.text = getString(R.string.nothing_to_digest)
                } else {
                    val digest = MessageDigest.calculateDigest(data)
                    // Make digest Base64-encoded for showing in UI.
                    result.text = getString(
                        R.string.digest_result,
                        Base64.encodeToString(digest, Base64.DEFAULT)
                    )
                }
            }
        }
        decryptionBtn.isVisible = false
    }
}
