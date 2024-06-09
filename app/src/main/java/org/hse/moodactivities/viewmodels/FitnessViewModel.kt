import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.launch
import org.hse.moodactivities.common.proto.requests.stats.UploadFitnessDataRequest
import org.hse.moodactivities.managers.FitnessDataManager
import org.hse.moodactivities.models.FitnessData
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc
import org.hse.moodactivities.common.proto.defaults.Empty
import org.hse.moodactivities.fragments.InsightsScreenFragment
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.services.UserService
import org.hse.moodactivities.viewmodels.AuthViewModel

class FitnessViewModel(
    private val fitnessDataManager: FitnessDataManager
) : ViewModel() {

    private val _fitnessData = MutableLiveData<FitnessData>()
    val fitnessData: LiveData<FitnessData> get() = _fitnessData

    private lateinit var authViewModel: AuthViewModel
    private lateinit var channel: ManagedChannel
    private lateinit var stub: StatsServiceGrpc.StatsServiceBlockingStub

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun onCreate(
        owner: FragmentActivity
    ) {
        channel =
            ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                .usePlaintext().build()

        authViewModel = ViewModelProvider(owner)[AuthViewModel::class.java]

        stub = StatsServiceGrpc.newBlockingStub(channel)
            .withInterceptors(
                JwtClientInterceptor {
                    authViewModel.getToken(
                        owner.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })
    }

    fun loadFitnessData() {
        viewModelScope.launch {
            val data = fitnessDataManager.getAggregatedFitnessData()
            _fitnessData.postValue(data)
        }
    }

    fun loadAndSendFitnessData() {
        val requestBuilder = UploadFitnessDataRequest.newBuilder()
        val responseLiveData = MutableLiveData<Empty>()
        viewModelScope.launch {
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
                _errorMessage.value = "Network error"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
