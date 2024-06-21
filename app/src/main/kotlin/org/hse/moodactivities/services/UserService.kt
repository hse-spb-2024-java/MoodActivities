package org.hse.moodactivities.services

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.birjuvachhani.locus.Locus
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.profile.ChangeInfoRequest
import org.hse.moodactivities.common.proto.requests.profile.CheckPasswordRequest
import org.hse.moodactivities.common.proto.requests.profile.GetInfoRequest
import org.hse.moodactivities.common.proto.services.ProfileServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.viewmodels.AuthViewModel

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

        fun uploadUserInfoFromServer(activity: AppCompatActivity) {
            val channel = ManagedChannelBuilder.forAddress(ADDRESS, PORT).usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub =
                ProfileServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

            val request = GetInfoRequest.newBuilder().build()

            val response = stub.getInfo(request)

            userInfo.name = response.name
            userInfo.birthDate = response.dateOfBirth
            userInfo.email = response.email
            userInfo.login = response.login

            userInfo.isUpdatedFromServer = true
        }

        fun checkOldPassword(oldPassword: String, activity: AppCompatActivity): Boolean {
            val channel = ManagedChannelBuilder.forAddress(ADDRESS, PORT).usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub =
                ProfileServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

            val request = CheckPasswordRequest.newBuilder().setPassword(oldPassword).build()

            val response = stub.checkPassword(request)

            return response.correct
        }

        fun getUsername(): String? {
            return userInfo.name
        }

        fun setUsername(newName: String, activity: AppCompatActivity): Boolean {
            val channel = ManagedChannelBuilder.forAddress(ADDRESS, PORT).usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub =
                ProfileServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

            val request = ChangeInfoRequest.newBuilder().setName(newName).build()

            val response = stub.changeInfo(request)

            if (response.completed) {
                userInfo.name = newName
            }

            return response.completed
        }

        fun getBirthDate(): String? {
            return userInfo.birthDate
        }

        fun setBirthDate(newBirthDate: String, activity: AppCompatActivity): Boolean {
            val channel = ManagedChannelBuilder.forAddress(ADDRESS, PORT).usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub =
                ProfileServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

            val request = ChangeInfoRequest.newBuilder().setDateOfBirth(newBirthDate).build()

            val response = stub.changeInfo(request)

            if (response.completed) {
                userInfo.birthDate = newBirthDate
            }
            return response.completed
        }

        fun setPassword(newPassword: String, activity: AppCompatActivity): Boolean {
            val channel = ManagedChannelBuilder.forAddress(ADDRESS, PORT).usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub =
                ProfileServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

            val request = ChangeInfoRequest.newBuilder().setPassword(newPassword).build()

            val response = stub.changeInfo(request)

            return response.completed
        }

        fun getLogin(): String? {
            return userInfo.login
        }

        fun getEmail(): String? {
            return userInfo.email
        }
    }
}
