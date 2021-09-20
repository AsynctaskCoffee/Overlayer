package com.bird.overlayer.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun save(key: String, value: Any?) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        when (value) {
            is Int -> editor.putInt(key, value)
            is String -> editor.putString(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Long -> editor.putLong(key, value)
            else -> return
        }
        editor.apply()
    }

    fun getString(key: String, defValue: String = "") =
        sharedPreferences.getString(key, defValue)

    fun getBoolean(key: String, defValue: Boolean = false) =
        sharedPreferences.getBoolean(key, defValue)
}