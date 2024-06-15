package org.hse.moodactivities.services

import android.content.Context
import android.location.Location
import android.util.Log
import com.birjuvachhani.locus.Locus

class UserService {
    companion object {
        enum class SettingsType {
            NAME, BIRTH_DAY
        }

        private var settingsType = SettingsType.NAME
        const val ADDRESS = "10.0.2.2"
        const val PORT = 12345

        fun getCurrentLocation(context: Context): Location? {
            var location: Location? = null
            Locus.getCurrentLocation(context) { result ->
                result.location?.let { location = result.location }
                result.error?.let {
                    Log.i("location", "Get error while getting location: " + result.error!!.message)
                }
            }
            return location
        }

        fun setSettingsType(newSettingsType: SettingsType) {
            settingsType = newSettingsType
        }

        fun getSettingsType(): SettingsType {
            return settingsType
        }
    }
}
