package org.hse.moodactivities.services

import android.content.Context
import android.location.Location
import android.util.Log
import com.birjuvachhani.locus.Locus

class UserService {
    private class UserInfo {
        // flag to show that user info was uploaded from server after settings fragment created
        var isUpdatedFromServer = false

        var name: String? = null
        var birthDate: String? = null
        var login: String? = null
        var email: String? = null
    }
    companion object {
        enum class SettingsType {
            NAME, BIRTH_DAY, PASSWORD
        }

        private var settingsType = SettingsType.NAME
        private var userInfo: UserInfo = UserInfo()
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

        fun uploadUserInfoFromServer() {
            // todo: upload

            userInfo.isUpdatedFromServer = true
        }

        fun checkOldPassword(oldPassword: String): Boolean {
            // todo: check

            return true
        }

        fun getUsername(): String? {
            return userInfo.name
        }

        fun setUsername(newName : String) {
            userInfo.name = newName
        }

        fun getBirthDate(): String? {
            return userInfo.birthDate
        }

        fun setBirthDate(newBirthDate : String) {
            userInfo.birthDate = newBirthDate
        }

        fun getLogin(): String? {
            return userInfo.login
        }

        fun setLogin(newLogin : String) {
            userInfo.login = newLogin
        }

        fun getEmail(): String? {
            return userInfo.email
        }

        fun setEmail(newEmail : String) {
            userInfo.email = newEmail
        }
    }
}
