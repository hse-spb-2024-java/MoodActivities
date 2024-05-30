package org.hse.moodactivities.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch
import org.hse.moodactivities.app.MoodActivitiesApp
import org.hse.moodactivities.common.proto.requests.auth.LoginRequest
import org.hse.moodactivities.common.proto.requests.auth.RegistrationRequest
import org.hse.moodactivities.common.proto.responses.auth.LoginResponse
import org.hse.moodactivities.common.proto.responses.auth.RegistrationResponse
import org.hse.moodactivities.common.proto.services.AuthServiceGrpc
import org.hse.moodactivities.utils.PreferenceManager

class AuthViewModel : ViewModel() {
    private val channel = ManagedChannelBuilder.forAddress("10.0.2.2", 12345)
        .usePlaintext()
        .build()
    private val stub = AuthServiceGrpc.newBlockingStub(channel)
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun register(username: String, password: String): LiveData<RegistrationResponse> {
        val responseLiveData = MutableLiveData<RegistrationResponse>()
        val request = RegistrationRequest.newBuilder()
            .setUsername(username)
            .setPassword(password)
            .build()

        viewModelScope.launch {
            try {
                val response = stub.registration(request)
                responseLiveData.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Network error"
            }
        }
        return responseLiveData
    }

    fun login(username: String, password: String): LiveData<LoginResponse> {
        val responseLiveData = MutableLiveData<LoginResponse>()
        val request = LoginRequest.newBuilder()
            .setType(LoginRequest.loginType.LOGIN)
            .setUserInfo(username)
            .setPassword(password)
            .build()

        viewModelScope.launch {
            try {
                val response = stub.login(request)
                responseLiveData.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Network error"
            }
        }
        return responseLiveData
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun saveToken(sharedPreferences: SharedPreferences, token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("jwtToken", token)
        PreferenceManager.saveData(MoodActivitiesApp.applicationContext(), "jwtToken", token)
        editor.apply()
    }

    fun getToken(sharedPreferences: SharedPreferences): String? {
        return sharedPreferences.getString("jwtToken", null)
    }
}
