package com.securevale.androidcryptosamples.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.securevale.androidcryptosamples.databinding.SampleButtonBinding
import com.securevale.androidcryptosamples.ui.AesCbcSampleFragment
import com.securevale.androidcryptosamples.ui.AesGcmSampleFragment
import com.securevale.androidcryptosamples.ui.BiometricSampleFragment
import com.securevale.androidcryptosamples.ui.HmacSampleFragment
import com.securevale.androidcryptosamples.ui.MessageDigestSampleFragment
import com.securevale.androidcryptosamples.ui.RsaSampleFragment
import com.securevale.androidcryptosamples.ui.SignatureSampleFragment

class SamplesAdapter(private val clickListener: SampleClickListener) :
    RecyclerView.Adapter<SamplesAdapter.ViewHolder>() {

    private val fragments = listOf(
        "AES-CBC" to AesCbcSampleFragment::class.java,
        "AES-GCM" to AesGcmSampleFragment::class.java,
        "BIOMETRIC" to BiometricSampleFragment::class.java,
        "HMAC" to HmacSampleFragment::class.java,
        "MESSAGE DIGEST" to MessageDigestSampleFragment::class.java,
        "RSA" to RsaSampleFragment::class.java,
        "SIGNATURE" to SignatureSampleFragment::class.java,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            SampleButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = fragments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fragments[position])
    }

    inner class ViewHolder(private val binding: SampleButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fragment: Pair<String, Class<out Fragment>>) = with(binding) {
            button.apply {
                text = fragment.first
                setOnClickListener { clickListener.onSampleClick(fragment.second) }
            }
        }
    }

    interface SampleClickListener {
        fun onSampleClick(sampleFragment : Class<out Fragment>)
    }
}