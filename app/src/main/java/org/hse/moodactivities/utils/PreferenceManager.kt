package org.hse.moodactivities.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private const val PREFERENCES_FILE_NAME = "InternalPrefs"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun saveData(context: Context, key: String, value: String) {
        val editor: SharedPreferences.Editor = getPreferences(context).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(context: Context, key1: String): String? {
        val sharedPreferences = getPreferences(context)
        return sharedPreferences.getString(key1, null)
    }
}
