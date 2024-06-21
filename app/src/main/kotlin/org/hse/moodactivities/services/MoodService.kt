package org.hse.moodactivities.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.defaults.PeriodType
import org.hse.moodactivities.common.proto.requests.stats.AiRequest
import org.hse.moodactivities.common.proto.requests.stats.DaysMoodRequest
import org.hse.moodactivities.common.proto.requests.survey.LongSurveyRequest
import org.hse.moodactivities.common.proto.responses.survey.LongSurveyResponse
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc
import org.hse.moodactivities.common.proto.services.SurveyServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.models.MoodEvent
import org.hse.moodactivities.responses.WeekAnalyticsResponse
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
        @SuppressLint("MissingPermission", "Range")
        fun getGptResponse(activity: AppCompatActivity): GptMoodResponse {
            val channel = ManagedChannelBuilder.forAddress("10.0.2.2", 12345).usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]

            val stub =
                SurveyServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                        authViewModel.getToken(
                            activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                        )!!
                    })

            var lat = 404.0
            var lon = 404.0
            var response = LongSurveyResponse.newBuilder().build()
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
                val locationTask = fusedLocationClient.getLastLocation()
                val location =
                    Tasks.await(locationTask) // Ожидаем результат задачи с таймаутом или выбрасываем исключение при ошибке
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    val request = LongSurveyRequest.newBuilder().setDate(LocalDate.now().toString())
                        .setMoodRating(moodEvent?.getMoodRate()!! + 1)
                        .addAllActivities(moodEvent?.getChosenActivities() as MutableIterable<String>)
                        .addAllEmotions(moodEvent?.getChosenEmotions() as MutableIterable<String>)
                        .setQuestion(moodEvent?.getQuestion() ?: "")
                        .setAnswer(moodEvent?.getUserAnswer() ?: "").setLat(lat).setLon(lon).build()

                    response = stub.longSurvey(request)
                } else {
                    throw RuntimeException();
                }
            } catch (e: Exception) {
                Log.e("MoodService", "Failed to get location")
                val request = LongSurveyRequest.newBuilder().setDate(LocalDate.now().toString())
                    .setMoodRating(moodEvent?.getMoodRate()!! + 1)
                    .addAllActivities(moodEvent?.getChosenActivities() as MutableIterable<String>)
                    .addAllEmotions(moodEvent?.getChosenEmotions() as MutableIterable<String>)
                    .setQuestion(moodEvent?.getQuestion() ?: "")
                    .setAnswer(moodEvent?.getUserAnswer() ?: "").setLat(59.980418).setLon(30.323990)
                    .build()

                response = stub.longSurvey(request)
            }
            return GptMoodResponse(response.shortSummary, response.fullSummary)
        }

        fun getUserDailyMood(activity: AppCompatActivity): Int {
            return getUserDailyMood(activity, LocalDate.now())
        }

        fun getUserDailyMood(activity: AppCompatActivity, date: LocalDate): Int {
            val channel = ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                .usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]

            val statsServiceBlockingStub =
                StatsServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                        authViewModel.getToken(
                            activity.getSharedPreferences(
                                "userPreferences",
                                Context.MODE_PRIVATE
                            )
                        )!!
                    })

            val formattedDate = getFormattedDate(date)

            val daysMood = statsServiceBlockingStub.getDaysMood(
                DaysMoodRequest.newBuilder().setDate(formattedDate).build()
            ).score.toInt()
            channel.shutdown()
            Log.i("getUserDailyMood response", daysMood.toString())
            return daysMood - 1
        }

        fun getWeekAnalytics(activity: AppCompatActivity): WeekAnalyticsResponse {
            val channel = ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                .usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]

            val statsServiceBlockingStub =
                StatsServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                        authViewModel.getToken(
                            activity.getSharedPreferences(
                                "userPreferences",
                                Context.MODE_PRIVATE
                            )
                        )!!
                    })

            val request = AiRequest.newBuilder().setPeriod(PeriodType.WEEK).build()
            val response = statsServiceBlockingStub.getAiAnalytics(request)

            return WeekAnalyticsResponse(response.text, response.advice)
        }

        private fun getFormattedDate(localDate: LocalDate): String {
            val formattedDate = StringBuilder()
            formattedDate.append(localDate.year.toString() + "-")
            val month = localDate.month.value.toString()
            formattedDate.append((if (month.length == 2) month else "0$month") + "-")
            val day = localDate.dayOfMonth.toString()
            formattedDate.append(if (day.length == 2) day else "0$day")
            return formattedDate.toString()
        }
    }
}
