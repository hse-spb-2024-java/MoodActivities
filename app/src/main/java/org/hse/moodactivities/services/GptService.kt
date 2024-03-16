package org.hse.moodactivities.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.hse.moodactivities.common.proto.requests.gpt.GptSessionRequest
import org.hse.moodactivities.common.proto.responses.gpt.GptSessionResponse
import org.hse.moodactivities.common.proto.services.GptServiceGrpc
import org.hse.moodactivities.utils.GptResponseListener
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_OK
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class GptService : Service(), GptResponseListener {

    private lateinit var channel: ManagedChannel
    private lateinit var gptServiceStub: GptServiceGrpc.GptServiceStub
    private lateinit var requestStreamObserver: StreamObserver<GptSessionRequest>
    private lateinit var responseStreamObserver: StreamObserver<GptSessionResponse>
    private lateinit var responseListener: GptResponseListener
    private lateinit var responseMessage: String
    private var responseCode: Int = 0

    private val semaphore = Semaphore(0)

    override fun onCreate() {
        super.onCreate()

        channel = ManagedChannelBuilder.forAddress("10.0.2.2", PORT)
            .usePlaintext()
            .build()
        gptServiceStub = GptServiceGrpc.newStub(channel)

        responseListener = this

        requestStreamObserver =
            gptServiceStub.gptSession(object : StreamObserver<GptSessionResponse> {
                override fun onNext(response: GptSessionResponse) {
                    throw NotImplementedError("do not use for request");
                }

                override fun onError(t: Throwable) {
                    responseListener?.onError(t)
                }

                override fun onCompleted() {
                    responseListener?.onStreamCompleted()
                }
            })

        responseStreamObserver = object : StreamObserver<GptSessionResponse> {
            override fun onNext(response: GptSessionResponse) {
                if (response.responseCode < HTTP_BAD_REQUEST) {
                    val messageFromServer = response.message
                    responseListener?.onResponseReceived(messageFromServer)
                    semaphore.release()
                } else {
                    responseListener?.onError(RuntimeException("bad response code ${response.responseCode}"))
                    semaphore.release()
                }
            }

            override fun onError(t: Throwable) {
                responseListener?.onError(t)
                semaphore.release()
            }

            override fun onCompleted() {
                responseListener?.onStreamCompleted()
                semaphore.release()
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    fun sendRequest(message: String) {
        val request = GptSessionRequest.newBuilder()
            .setMessage(message)
            .build()
        requestStreamObserver.onNext(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val PORT = 12345
    }

    override fun onResponseReceived(message: String) {
        responseMessage = message
        responseCode = HTTP_OK
    }

    override fun onError(error: Throwable) {
        responseMessage = error.toString()
        responseCode = HTTP_BAD_REQUEST
    }

    override fun onStreamCompleted() {
        responseStreamObserver.onCompleted()
        requestStreamObserver.onCompleted()
        responseMessage = "Session successfully ended"
        responseCode = 0
    }

    fun getResponse(): Pair<Int, String> {
        return Pair(responseCode, responseMessage)
    }

    fun waitForResponse() {
        semaphore.acquire()
    }
}
