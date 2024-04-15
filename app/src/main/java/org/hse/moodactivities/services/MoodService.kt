package org.hse.moodactivities.services

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.survey.LongSurveyRequest
import org.hse.moodactivities.common.proto.services.SurveyServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.viewmodels.AuthViewModel
import java.time.LocalDate

class MoodService {
    companion object {
        private var moodEvent: MoodEvent? = null

        class GptMoodResponse(var shortSummary : String, var fullSummary : String)

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
                JwtClientInterceptor { authViewModel.getToken(
                    activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                )!! })

            val request = LongSurveyRequest.newBuilder()
                .setDate(LocalDate.now().toString())
                .setMoodRating(moodEvent?.getMoodRate()!! + 1)
                .addAllActivities(moodEvent?.getChosenActivities() as MutableIterable<String>)
                .addAllEmotions(moodEvent?.getChosenEmotions() as MutableIterable<String>)
                .setQuestion("What else can you say about your day?")
                .setAnswer(moodEvent?.getUserAnswer() ?: "" )
                .build()

            val response = stub.longSurvey(request)
            return GptMoodResponse(response.shortSummary, response.fullSummary)
        }

        fun getUserDailyMood() : Int? {
            // todo: ask server to get today's average mood
            if (moodEvent != null) {
                return moodEvent?.getMoodRate()
            }
            return -1
        }

        fun getEmotionId() {

        }
    }
}
