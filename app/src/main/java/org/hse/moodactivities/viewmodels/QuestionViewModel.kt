package org.hse.moodactivities.viewmodels

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.dailyQuestion.CheckAnswerRequest
import org.hse.moodactivities.common.proto.requests.dailyQuestion.QuestionRequest
import org.hse.moodactivities.common.proto.services.QuestionServiceGrpc
import org.hse.moodactivities.interceptors.JwtClientInterceptor
import org.hse.moodactivities.services.UserService

class QuestionViewModel {
    private lateinit var channel: ManagedChannel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var gptServiceStub: QuestionServiceGrpc.QuestionServiceBlockingStub
    fun onCreateView(
        owner: FragmentActivity
    ) {
        channel =
            ManagedChannelBuilder.forAddress(UserService.ADDRESS, UserService.PORT)
                .usePlaintext().build()

        authViewModel = ViewModelProvider(owner)[AuthViewModel::class.java]

        gptServiceStub = QuestionServiceGrpc.newBlockingStub(channel)
            .withInterceptors(
                JwtClientInterceptor {
                    authViewModel.getToken(
                        owner.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
                    )!!
                })
    }

    fun check(request: CheckAnswerRequest): Boolean {
        val checker = gptServiceStub.checkDailyQuestion(request)
        return checker.hasAnswer == 1
    }

    fun getRandomQuestion(): String? {
        return gptServiceStub.getRandomQuestion(QuestionRequest.getDefaultInstance()).question
    }
}
