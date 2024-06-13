package org.hse.moodactivities.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.*
import org.hse.moodactivities.common.proto.defaults.Empty
import org.hse.moodactivities.common.proto.requests.stats.UploadFitnessDataRequest
import org.hse.moodactivities.common.proto.services.HealthServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.managers.FitnessDataManager
import org.hse.moodactivities.models.FitnessData
import org.hse.moodactivities.viewmodels.AuthViewModel

class HealthService(
    private var activity: AppCompatActivity, private val fitnessDataManager: FitnessDataManager
) : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _fitnessData = MutableLiveData<FitnessData>()
    val fitnessData: LiveData<FitnessData> get() = _fitnessData

    private lateinit var channel: ManagedChannel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var stub: HealthServiceGrpc.HealthServiceBlockingStub
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage


    override fun onCreate() {
        channel =
            ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                .usePlaintext().build()

        authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]

        stub = HealthServiceGrpc.newBlockingStub(channel)
            .withInterceptors(
                JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", MODE_PRIVATE)
                    )!!
                })
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun loadAndSendFitnessData() {
        scope.launch {
            val requestBuilder = UploadFitnessDataRequest.newBuilder()
            val responseLiveData = MutableLiveData<Empty>()
            val data = fitnessDataManager.getAggregatedFitnessData()
            _fitnessData.postValue(data)

            val request = requestBuilder
                .setStepsForLastDay(data.steps)
                .build()
            try {
                val response = stub.uploadFitnessData(request)
                responseLiveData.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.postValue("Network error")
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}