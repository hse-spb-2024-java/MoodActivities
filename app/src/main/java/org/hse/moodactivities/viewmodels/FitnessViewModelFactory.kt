package org.hse.moodactivities.viewmodels

import FitnessViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.hse.moodactivities.managers.FitnessDataManager

class FitnessViewModelFactory(
    private val fitnessDataManager: FitnessDataManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FitnessViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FitnessViewModel(fitnessDataManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
