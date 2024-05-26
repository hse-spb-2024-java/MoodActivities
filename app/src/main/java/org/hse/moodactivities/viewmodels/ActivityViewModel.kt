package org.hse.moodactivities.viewmodels

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.activity.CheckActivityRequest
import org.hse.moodactivities.common.proto.requests.activity.GetActivityRequest
import org.hse.moodactivities.common.proto.requests.activity.RecordActivityRequest
import org.hse.moodactivities.common.proto.services.ActivityServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.services.UserService
import java.time.LocalDate

class ActivityViewModel {
    private lateinit var channel: ManagedChannel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var serviceStub: ActivityServiceGrpc.ActivityServiceBlockingStub
    fun onCreateView(
        owner: FragmentActivity
    ) {
        channel =
            ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                .usePlaintext().build()

        authViewModel = ViewModelProvider(owner)[AuthViewModel::class.java]

        serviceStub = ActivityServiceGrpc.newBlockingStub(channel)
            .withInterceptors(
                JwtClientInterceptor {
                    authViewModel.getToken(
                        owner.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })
    }

    fun wasRequested(): Boolean {
        val request = CheckActivityRequest.newBuilder().setDate(LocalDate.now().toString()).build()
        val checker = serviceStub.checkActivity(request)
        return checker.wasCompleted == 1
    }

    fun wasRecorded(request: CheckActivityRequest): Boolean {
        val request = CheckActivityRequest.newBuilder().setDate(LocalDate.now().toString()).build()
        val checker = serviceStub.checkActivity(request)
        return checker.wasRecorded == 1
    }

    fun getActivity(): String? {
        val checkerRequest =
            CheckActivityRequest.newBuilder().setDate(LocalDate.now().toString()).build()
        val checker = serviceStub.checkActivity(checkerRequest)
        if (checker.wasCompleted == 1) {
            return checker.activity
        }
        val getterRequest = GetActivityRequest.newBuilder()
            .setDate(LocalDate.now().toString())
            .build()
        val response = serviceStub.getActivity(getterRequest)
        if (response.statusCode == 200) {
            return response.activity
        }
        return null
    }

    fun recordReport(report: String): Boolean {
        val recorderRequest = RecordActivityRequest.newBuilder()
            .setRecord(report)
            .setDate(LocalDate.now().toString())
            .build()
        val response = serviceStub.recordActivity(recorderRequest)
        return response.statusCode == 200
    }
}
