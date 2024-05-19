package org.hse.moodactivities.services

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.activity.CheckActivityRequest
import org.hse.moodactivities.common.proto.requests.activity.GetActivityRequest
import org.hse.moodactivities.common.proto.requests.activity.RecordActivityRequest
import org.hse.moodactivities.common.proto.services.ActivityServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.viewmodels.AuthViewModel
import java.time.LocalDate

class ActivityService {
    companion object {
        /**
         * Check activity availability (= check it wasn't recorded today).
         */
        private fun checkActivityAvailability(activity: AppCompatActivity): Boolean {
            val channel = ManagedChannelBuilder.forAddress("10.0.2.2", 12345).usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub =
                ActivityServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

            val request =
                CheckActivityRequest.newBuilder().setDate(LocalDate.now().toString()).build()

            val response = stub.checkActivity(request)
            return response.wasRecorded == 0
        }

        /**
         * Get daily activity for user. Returns `null` when it's unavailable.
         */
        fun getDailyActivity(activity: AppCompatActivity): String? {
            if (!checkActivityAvailability(activity)) {
                return null
            }
            val channel = ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                .usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub =
                ActivityServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

            val request =
                GetActivityRequest.newBuilder().setDate(LocalDate.now().toString()).build()

            val response = stub.getActivity(request)
            return response.activity

        }

        /**
         * Send user's impressions to server.
         */
        fun recordDailyActivity(activity: AppCompatActivity, usersImpressions: String) {
            val channel = ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                .usePlaintext().build()

            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub =
                ActivityServiceGrpc.newBlockingStub(channel).withInterceptors(JwtClientInterceptor {
                    authViewModel.getToken(
                        activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })

            val request = RecordActivityRequest.newBuilder().setRecord(usersImpressions)
                .setDate(LocalDate.now().toString()).build()

            val response = stub.recordActivity(request)
        }
    }
}
