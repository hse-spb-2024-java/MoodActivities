import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hse.moodactivities.common.proto.requests.auth.LoginRequest
import org.hse.moodactivities.common.proto.requests.auth.RegistrationRequest
import org.hse.moodactivities.common.proto.responses.auth.LoginResponse
import org.hse.moodactivities.common.proto.responses.auth.RegistrationResponse
import org.hse.moodactivities.common.proto.services.AuthServiceGrpc

class AuthViewModel : ViewModel() {
    private val channel = ManagedChannelBuilder.forAddress("10.0.2.2", 12345)
        .usePlaintext()
        // TODO: JWT
        .build()
    private val stub = AuthServiceGrpc.newBlockingStub(channel);

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
                // TODO: XDDDDDDDDDDDD
                e.printStackTrace()
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
                // TODO: XDDDDDDDDDDDD
                e.printStackTrace()
            }
        }
        return responseLiveData
    }
}
