package com.securevale.androidcryptosamples.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.databinding.ActivityMainBinding
import com.securevale.androidcryptosamples.helpers.listCipherAlgorithms
import com.securevale.androidcryptosamples.helpers.listMacAlgorithms
import com.securevale.androidcryptosamples.helpers.listMessageDigestAlgorithms
import com.securevale.androidcryptosamples.helpers.listSignatureAlgorithms
import com.securevale.androidcryptosamples.helpers.listStrongRandom
import com.securevale.androidcryptosamples.ui.adapter.SamplesAdapter
import com.securevale.androidcryptosamples.ui.lifecycle.bindWithLifecycle

class MainActivity : AppCompatActivity(), SamplesAdapter.SampleClickListener {

    private var binding: ActivityMainBinding by bindWithLifecycle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recycler.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = SamplesAdapter(this@MainActivity)
            }
        }

        // List just for educational purposes. TODO create dedicated screen for presenting these
        listMessageDigestAlgorithms()
        listCipherAlgorithms()
        listSignatureAlgorithms()
        listMacAlgorithms()
        listStrongRandom()
    }

    private fun replace(fragment: Fragment) {
        with(binding) {
            container.apply {
                changeComponentsVisibility(true)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onBackPressed() {
        // Ugly but hey it's just a sample app for cryptography
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            changeComponentsVisibility(false)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSampleClick(sampleFragment: Class<out Fragment>) =
        replace(sampleFragment.newInstance())

    private fun changeComponentsVisibility(containerVisible: Boolean) = with(binding) {
        container.isVisible = containerVisible
        recycler.isVisible = !containerVisible
    }
}
