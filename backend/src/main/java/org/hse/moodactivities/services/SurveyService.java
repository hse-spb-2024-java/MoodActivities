package org.hse.moodactivities.services;

import io.grpc.stub.StreamObserver;

import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.data.utils.MongoDBConnection;
import org.hse.moodactivities.data.utils.MongoDBConnection;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SurveyService extends SurveyServiceGrpc.SurveyServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SurveyService.class);

    @Override
    public void longSurvey(LongSurveyRequest request, StreamObserver<LongSurveyResponse> responseObserve) {
        LongSurveyResponse response;
        try (MongoDBConnection connection = new MongoDBConnection()) {
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
                UserDayMeta newMeta = new UserDayMeta(request);
                newMeta.setDate(LocalDate.parse(request.getDate()));
                String id = "123"; // TODO: add map user -> id
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("_id", id);
                User existingEntity = connection.findEntityWithFilters(User.class, queryMap).get(0);
                if (existingEntity != null) {
                    existingEntity.updateMeta(newMeta);
                    connection.getDatastore().merge(existingEntity);
                } else {
                    User newEntity = new User("123", Arrays.asList(newMeta));
                    connection.saveEntity(newEntity);
                }
                response = LongSurveyResponse.newBuilder().setShortSummary(shortForm.toString()).setFullSummary(fullForm.toString()).build();
            } else {
                response = LongSurveyResponse.newBuilder().setFullSummary(gptResponse.response()).build();
            }
        } catch (Throwable e) {
            LOGGER.error("An error occurred:", e);
            response = LongSurveyResponse.newBuilder().build();
        }
        responseObserve.onNext(response);
        responseObserve.onCompleted();
    }
}
