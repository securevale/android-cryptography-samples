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
import com.securevale.androidcryptosamples.encryption.symmetric.aes.cbc.AesCbc

class AesCbcFragment : Fragment() {
    private lateinit var encryptionResult: Pair<String, ByteArray>

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
        val input = view.findViewById<EditText>(R.id.input)

        view.findViewById<Button>(R.id.encryption_btn).setOnClickListener {
            encryptionResult = AesCbc.encrypt(input.text.toString())
            resultField.text = "Encrypted:  $encryptionResult"
        }

        view.findViewById<Button>(R.id.decryption_btn).setOnClickListener {
            if (!this::encryptionResult.isInitialized) {
                resultField.text = "Nothing to decrypt, encrypt first"
            } else {
                val decrypted = AesCbc.decrypt(encryptionResult.first, encryptionResult.second)
                resultField.text = "Decrypted: $decrypted"
            }
        }
    }
}
