package org.hse.moodactivities.services

import io.grpc.ManagedChannelBuilder
import org.hse.moodactivities.common.proto.requests.survey.LongSurveyRequest
import org.hse.moodactivities.common.proto.services.SurveyServiceGrpc
import org.hse.moodactivities.models.MoodEvent

class MoodService {
    companion object {
        private lateinit var moodEvent: MoodEvent

        class GptMoodResponse(var shortSummary : String, var fullSummary : String)

        fun setMoodEvent(receivedMoodEvent: MoodEvent) {
            moodEvent = receivedMoodEvent
        }

        fun getDayRate(moodData: MoodEvent): String {
            return "It's okay"
        }

        // GPT describes user's day in one word
        fun getGptShortResponse(): String {
            val answer = "amazing"
            return "Your day was " + answer
        }

        // GPT describes user's day
        fun getGptResponse(): GptMoodResponse {
            val channel = ManagedChannelBuilder.forAddress("10.0.2.2", 50051)
                .usePlaintext()
                .build()

            val stub = SurveyServiceGrpc.newBlockingStub(channel)

            val request = LongSurveyRequest.newBuilder()
                .setMoodRating(moodEvent.getMoodRate()!! + 1)
                .addAllActivities(moodEvent.getChosenActivities() as MutableIterable<String>)
                .addAllEmotions(moodEvent.getChosenEmotions() as MutableIterable<String>)
                .setQuestion("What else can you say about your day?")
                .setAnswer(moodEvent.getUserAnswer())
                .build()

            val response = stub.longSurvey(request)
            return GptMoodResponse(response.shortSummary, response.fullSummary)
        }
    }
}
