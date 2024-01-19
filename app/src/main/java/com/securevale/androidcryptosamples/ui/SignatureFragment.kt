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
import com.securevale.androidcryptosamples.signature.Signature

class SignatureFragment : Fragment() {

    private var signature: ByteArray? = null

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
        val resultField = view.findViewById<TextView>(R.id.result)
        val input = view.findViewById<EditText>(R.id.input).apply {
            hint = "Paste message to sign"
        }

        view.findViewById<Button>(R.id.encryption_btn).let {
            it.text = "Sign"
            it.setOnClickListener {
                val data = input.text.toString()
                if (data.isBlank()) {
                    resultField.text = "No text provided to make signing"
                } else {
                    signature = Signature.sign(data.toByteArray())

                    resultField.text = "Signed"
                }
            }
        }

        view.findViewById<Button>(R.id.decryption_btn).let {
            it.text = "Verify"
            it.setOnClickListener {
                val data = input.text.toString()
                if (data.isBlank()) {
                    resultField.text = "No text provided to verifying"
                } else {
                    val result = Signature.verify(data.toByteArray(), signature!!)

                    val verificationResult =
                        if (result) "Signature is valid" else "Signature is invalid"

                    resultField.text = "$verificationResult"
                }
            }
        }
    }
}
