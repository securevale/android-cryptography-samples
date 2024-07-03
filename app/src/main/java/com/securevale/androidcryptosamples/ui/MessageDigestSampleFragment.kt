package com.securevale.androidcryptosamples.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.hash.MessageDigest

class MessageDigestSampleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(view)
    }

    @SuppressLint("SetTextI18n")
    private fun initFields(view: View) {
        val resultField = view.findViewById<TextView>(R.id.result)
        val input = view.findViewById<EditText>(R.id.input).apply {
            hint = "Paste message to create digest from"
        }

        view.findViewById<Button>(R.id.encryption_btn).let {
            it.text = "Digest"
            it.setOnClickListener {
                val data = input.text.toString()
                if (data.isBlank()) {
                    resultField.text = "No text provided to calculate digest."
                } else {
                    val digest = MessageDigest.calculateDigest(data)
                    // Make digest Base64-encoded for showing in UI.
                    resultField.text = "Digest:  ${Base64.encodeToString(digest, Base64.DEFAULT)}"
                }
            }
        }

        view.findViewById<Button>(R.id.decryption_btn).visibility = View.GONE
    }
}
