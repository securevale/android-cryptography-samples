package com.securevale.androidcryptosamples.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.securevale.androidcryptosamples.R
import com.securevale.androidcryptosamples.helpers.listSupportedAlgorithms

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listSupportedAlgorithms()

        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = Adapter(this@MainActivity)
        }
    }

    fun replace(fragment: Fragment) {
        findViewById<FrameLayout>(R.id.container).apply {
            visibility = View.VISIBLE
            this@MainActivity.findViewById<RecyclerView>(R.id.recycler).visibility = View.GONE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("sample")
                .commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            findViewById<FrameLayout>(R.id.container).visibility = View.GONE
            findViewById<RecyclerView>(R.id.recycler).visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }

    inner class Adapter(private val activity: MainActivity) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        private val fragments = listOf(
            "AES-CBC" to AesCbcFragment(),
            "AES-GCM" to AesGcmFragment(),
            "RSA" to RsaFragment()
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.sample_button, parent, false)
            )

        override fun getItemCount(): Int = fragments.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(fragments[position])
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bind(fragment: Pair<String, Fragment>) {
                itemView.findViewById<Button>(R.id.button).apply {
                    text = fragment.first
                    setOnClickListener { activity.replace(fragment.second) }
                }
            }
        }
    }
}
