package org.hse.moodactivities.managers

import org.hse.moodactivities.interfaces.GoogleFitRepository
import org.hse.moodactivities.models.FitnessData

class FitnessDataManager(
    private val googleFitRepository: GoogleFitRepository
) {
    suspend fun getAggregatedFitnessData(): FitnessData {
        val googleFitData = googleFitRepository.getFitnessData()
        // Here we can add more sources
        return aggregateData(googleFitData)
    }

    private fun aggregateData(googleFitData: FitnessData): FitnessData {
        val totalSteps = googleFitData.steps
        return FitnessData(steps = totalSteps)
    }
}
