package org.hse.moodactivities.services;

import io.grpc.stub.StreamObserver;
import org.hse.moodactivities.utils.MongoDBConnection;
import org.hse.moodactivities.common.proto.services.*;
import org.hse.moodactivities.common.proto.requests.survey.*;
import org.hse.moodactivities.common.proto.responses.survey.*;
import org.hse.moodactivities.utils.GptClientRequest;
import org.hse.moodactivities.utils.GptMessages;
import org.hse.moodactivities.utils.GptRequestFormatter;
import org.hse.moodactivities.utils.GptResponse;
import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyService extends SurveyServiceGrpc.SurveyServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SurveyService.class);

    private static Dotenv dotenv = Dotenv.load();
    private static final String MONGO_HOST = dotenv.get("MONGO_HOST");
    private static final int MONGO_PORT = Integer.valueOf(dotenv.get("MONGO_PORT"));
    private static final String MONGO_DBNAME = dotenv.get("MONGO_DBNAME");

    @Override
    public void longSurvey(LongSurveyRequest request, StreamObserver<LongSurveyResponse> responseObserve) {
        LongSurveyResponse response;
        try (MongoDBConnection connection = new MongoDBConnection(MONGO_HOST, MONGO_PORT, MONGO_DBNAME)) {
            GptMessages.GptMessage message = GptRequestFormatter.surveyRequest(request);
            GptResponse gptResponse = GptClientRequest.sendRequest(new GptMessages(message));
            if (gptResponse.message() != null) {
                String[] words = gptResponse.message().getContent().split("\\s+");
                StringBuilder shortForm = new StringBuilder();
                StringBuilder fullForm = new StringBuilder();
                for (int i = 0; i < words.length; i++) {
                    if (i < 3) {
                        shortForm.append(words[i]);
                    } else {
                        fullForm.append(words[i]);
                    }
                }
                response = LongSurveyResponse.newBuilder().setShortSummary(shortForm.toString()).setFullSummary(fullForm.toString()).build();
            } else {
                response = LongSurveyResponse.newBuilder().build();
            }
        } catch (Exception e) {
            response = LongSurveyResponse.newBuilder().build();
        }
        responseObserve.onNext(response);
        responseObserve.onCompleted();
    }
}
