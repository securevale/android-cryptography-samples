package com.securevale.androidcryptosamples.ui.lifecycle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty

class ClearOnDestroyObserver<T>(private val lifecycle: () -> Lifecycle) : DefaultLifecycleObserver {

    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        checkNotNull(value) { "Value not initialised or being outside of lifecycle" }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        lifecycle().removeObserver(this)
        this.value = value
        lifecycle().addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        value = null
    }
}

fun <T> AppCompatActivity.bindWithLifecycle() = ClearOnDestroyObserver<T> { lifecycle }
fun <T> Fragment.bindWithLifecycle() = ClearOnDestroyObserver<T> { viewLifecycleOwner.lifecycle }