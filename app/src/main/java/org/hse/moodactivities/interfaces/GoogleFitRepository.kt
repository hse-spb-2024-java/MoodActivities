package org.hse.moodactivities.interfaces

import org.hse.moodactivities.models.FitnessData

interface GoogleFitRepository {
    suspend fun getFitnessData(): FitnessData
}
