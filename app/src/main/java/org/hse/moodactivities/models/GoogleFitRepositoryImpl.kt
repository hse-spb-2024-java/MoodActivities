package org.hse.moodactivities.models

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.result.DailyTotalResult
import kotlinx.coroutines.tasks.await
import org.hse.moodactivities.interfaces.GoogleFitRepository

class GoogleFitRepositoryImpl(private val context: Context) : GoogleFitRepository {
    companion object {
        const val DEFAULT_STEPS = 0
    }

    override suspend fun getFitnessData(): FitnessData {
        val account = GoogleSignIn.getLastSignedInAccount(context) ?: throw Exception("Not signed in")
        val dailyTotalResult: DataSet = Fitness.getHistoryClient(context, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .await()

        val totalSteps = if (!dailyTotalResult.isEmpty) {
            dailyTotalResult.dataPoints.firstOrNull()?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0
        } else {
            DEFAULT_STEPS
        }

        return FitnessData(steps = totalSteps)
    }
}
