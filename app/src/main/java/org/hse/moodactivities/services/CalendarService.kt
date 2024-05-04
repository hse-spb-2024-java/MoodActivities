package org.hse.moodactivities.services

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.stats.AllDayRequest
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.responses.FullDayReportResponse
import org.hse.moodactivities.viewmodels.AuthViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarService {
    companion object {
        private const val LOG_TAG = "calendar service"
        private lateinit var chosenDate: LocalDate

        fun setDate(date: LocalDate) {
            chosenDate = date
        }

        fun getDate(): LocalDate {
            return chosenDate
        }

        fun getFullDayReportResponse(
            date: LocalDate,
            activity: AppCompatActivity
        ): FullDayReportResponse {
            val channel =
                ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                    .usePlaintext().build()
            val authViewModel = ViewModelProvider(activity)[AuthViewModel::class.java]
            val stub = StatsServiceGrpc.newBlockingStub(channel)
                .withInterceptors(
                    JwtClientInterceptor {
                        authViewModel.getToken(
                            activity.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                        )!!
                    })

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = date.format(formatter)

            Log.i(LOG_TAG, "Ask server for day information for the date $formattedDate")
            val request = AllDayRequest.newBuilder().setDate(formattedDate).build()
            val serverResponse = stub.allDayReport(request)
            channel.shutdown()

            // parse server response
            val response = FullDayReportResponse()
            response.init(serverResponse)
            return response
        }
    }
}
