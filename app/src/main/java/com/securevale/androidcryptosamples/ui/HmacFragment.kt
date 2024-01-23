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
import com.securevale.androidcryptosamples.mac.Hmac

class HmacFragment : Fragment() {

    private var hmac: String? = null

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
            hint = "Paste message to create Hmac from"
        }

        view.findViewById<Button>(R.id.encryption_btn).let {
            it.text = "Hmac"
            it.setOnClickListener {
                val data = input.text.toString()
                if (data.isBlank()) {
                    resultField.text = "No text provided to make hmac"
                } else {
                    hmac = Hmac.computeHmac(data)

                    resultField.text = hmac
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
                    val result = Hmac.computeHmac(data)

                    val verificationResult =
                        if (result == hmac) "Message is valid" else "Message is invalid"

                    resultField.text = "$verificationResult"
                }
            }
        }
    }
}