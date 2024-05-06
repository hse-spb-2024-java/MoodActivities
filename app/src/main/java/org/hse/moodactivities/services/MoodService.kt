package org.hse.moodactivities.services

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.stats.DaysMoodRequest
import org.hse.moodactivities.common.proto.requests.survey.LongSurveyRequest
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc
import org.hse.moodactivities.common.proto.services.SurveyServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.viewmodels.AuthViewModel
import java.time.LocalDate

class MoodService {
    companion object {
        private var moodEvent: MoodEvent? = null

        class GptMoodResponse(var shortSummary: String, var fullSummary: String)

        fun setMoodEvent(receivedMoodEvent: MoodEvent) {
            moodEvent = receivedMoodEvent
        }

        // GPT describes user's day
        fun getGptResponse(activity: AppCompatActivity): GptMoodResponse {
            val channel = ManagedChannelBuilder.forAddress("10.0.2.2", 12345)
                .usePlaintext()
                .build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]

            val stub = SurveyServiceGrpc.newBlockingStub(channel)
                .withInterceptors(
                    JwtClientInterceptor {
                        authViewModel.getToken(
                            activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                        )!!
                    })

            val request = LongSurveyRequest.newBuilder()
                .setDate(LocalDate.now().toString())
                .setMoodRating(moodEvent?.getMoodRate()!! + 1)
                .addAllActivities(moodEvent?.getChosenActivities() as MutableIterable<String>)
                .addAllEmotions(moodEvent?.getChosenEmotions() as MutableIterable<String>)
                .setQuestion(moodEvent?.getQuestion() ?: "")
                .setAnswer(moodEvent?.getUserAnswer() ?: "")
                .build()

            val response = stub.longSurvey(request)
            return GptMoodResponse(response.shortSummary, response.fullSummary)
        }

        fun getUserDailyMood(activity: AppCompatActivity): Int {
            val channel =
                ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                    .usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]

            val statsServiceBlockingStub = StatsServiceGrpc.newBlockingStub(channel)
                .withInterceptors(
                    JwtClientInterceptor {
                        authViewModel.getToken(
                            activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                        )!!
                    })

            val date = getDate()

            val daysMood = statsServiceBlockingStub.getDaysMood(
                DaysMoodRequest.newBuilder().setDate(date).build()
            ).score.toInt()
            channel.shutdown()
            Log.i("mood", daysMood.toString())
            return daysMood - 1
        }

        private fun getDate(): String {
            val localDate = LocalDate.now()
            val date = StringBuilder()
            date.append(localDate.year.toString() + "-")
            val month = localDate.month.value.toString()
            date.append((if (month.length == 2) month else "0" + month) + "-")
            val day = localDate.dayOfMonth.toString()
            date.append(if (day.length == 2) day else "0" + day)
            return date.toString()
        }

    }
}
