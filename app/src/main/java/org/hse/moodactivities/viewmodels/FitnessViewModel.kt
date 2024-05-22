import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hse.moodactivities.managers.FitnessDataManager
import org.hse.moodactivities.models.FitnessData

class FitnessViewModel(
    private val fitnessDataManager: FitnessDataManager
) : ViewModel() {

    private val _fitnessData = MutableLiveData<FitnessData>()
    val fitnessData: LiveData<FitnessData> get() = _fitnessData

    fun loadFitnessData() {
        viewModelScope.launch {
            val data = fitnessDataManager.getAggregatedFitnessData()
            _fitnessData.postValue(data)
        }
    }
}
