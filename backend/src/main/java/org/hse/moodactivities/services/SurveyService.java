package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.survey.LongSurveyRequest;
import org.hse.moodactivities.common.proto.responses.survey.LongSurveyResponse;
import org.hse.moodactivities.common.proto.services.SurveyServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.utils.GptClientRequest;
import org.hse.moodactivities.utils.GptMessages;
import org.hse.moodactivities.utils.GptRequestFormatter;
import org.hse.moodactivities.utils.GptResponse;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.MongoDBSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.stub.StreamObserver;

public class SurveyService extends SurveyServiceGrpc.SurveyServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SurveyService.class);

    private static String getSubstringAfterSecondCapital(String input) {
        int capitalCount = 0;
        int index = 0;
        for (int i = 0; i < input.length(); i++) {
            if (Character.isUpperCase(input.charAt(i))) {
                capitalCount++;
                if (capitalCount == 2) {
                    index = i;
                    break;
                }
            }
        }
        if (capitalCount < 2) {
            return "";
        } else {
            return input.substring(index);
        }
    }

    @Override
    public void longSurvey(LongSurveyRequest request, StreamObserver<LongSurveyResponse> responseObserve) {
        LongSurveyResponse response;
        try {
            GptMessages.GptMessage message = GptRequestFormatter.surveyRequest(request);
            GptResponse gptResponse = GptClientRequest.sendRequest(new GptMessages(message));
            if (gptResponse.message() != null) {
                String[] sentences = gptResponse.message().getContent().split("\\.");
                StringBuilder shortForm = new StringBuilder();
                StringBuilder fullForm = new StringBuilder();
                for (int i = 0; i < sentences.length; i++) {
                    if (i < 1) {
                        shortForm.append(getSubstringAfterSecondCapital(sentences[i]) + ".");
                    } else if (i == 1) {
                        fullForm.append(getSubstringAfterSecondCapital(sentences[i]) + ".");
                    } else {
                        fullForm.append(sentences[i] + ".");
                    }
                }
                UserDayMeta newMeta = new UserDayMeta(request);
                newMeta.setDate(LocalDate.now());
                String id = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("id", id);
                var existingEntities = MongoDBSingleton.getInstance().getConnection().findEntityWithFilters(User.class, queryMap);
                if (existingEntities != null && !existingEntities.isEmpty()) {
                    User existingEntity = existingEntities.get(0);
                    existingEntity.updateMeta(newMeta);
                    MongoDBSingleton.getInstance().getConnection().getDatastore().merge(existingEntity);
                } else {
                    User newEntity = new User(id, List.of(newMeta));
                    MongoDBSingleton.getInstance().getConnection().saveEntity(newEntity);
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
