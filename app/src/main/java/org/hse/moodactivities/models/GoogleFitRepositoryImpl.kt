package org.hse.moodactivities.models

import android.content.Context
import org.hse.moodactivities.interfaces.GoogleFitRepository

class GoogleFitRepositoryImpl(private val context: Context) : GoogleFitRepository {
    override suspend fun getFitnessData(): FitnessData {
        // Implement fetching data from Google Fit
        // Placeholder implementation
        return FitnessData(steps = 1000)
    }
}
