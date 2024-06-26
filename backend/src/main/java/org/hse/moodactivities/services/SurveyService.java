package org.hse.moodactivities.services;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

import org.hse.moodactivities.common.proto.requests.defaults.PeriodType;
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
import org.hse.moodactivities.utils.PromptGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private static Optional<String> metaPromptSender(User user) {
        String requestString = PromptGenerator.generatePrompt(user.getMetas(), PromptGenerator.Service.metaCreator, null, PeriodType.WEEK);
        requestString = PromptGenerator.addFeedBack(requestString, user);
        GptResponse response = GptClientRequest.sendRequest(new GptMessages(GptMessages.GptMessage.Role.user, requestString));
        if (response.statusCode() < HTTP_BAD_REQUEST) {
            return Optional.of(response.message().getContent());
        }
        return Optional.empty();
    }

    private static void updateMeta(User user) {
        if (user.getPromptMetaUpdateDate() == null
                || ChronoUnit.DAYS.between(LocalDate.parse(user.getPromptMetaUpdateDate()), LocalDate.now()) >= 7) {
            Optional<String> updatedMeta = metaPromptSender(user);
            if (updatedMeta.isPresent()) {
                user.setPromptMetaUpdateDate(LocalDate.now().toString());
                user.setPromptMeta(updatedMeta.get());
            }
        }
    }

    public static Optional<String> getMeta(User user) {
        updateMeta(user);
        if (user.getPromptMeta() != null) {
            return Optional.of(user.getPromptMeta());
        }
        return Optional.empty();
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
                queryMap.put("_id", id);
                var existingEntities = MongoDBSingleton.getInstance().getConnection().findEntityWithFilters(User.class, queryMap);
                User user = null;
                if (existingEntities != null && !existingEntities.isEmpty()) {
                    user = existingEntities.get(0);
                    user.updateMeta(newMeta);
                    user.getMetas().getLast().getRecords().getLast().setShortSummary(shortForm.toString());
                    MongoDBSingleton.getInstance().getConnection().updateEntity(user);
                } else {
                    user = new User(id, List.of(newMeta));
                    user.getMetas().getLast().getRecords().getLast().setShortSummary(shortForm.toString());
                    MongoDBSingleton.getInstance().getConnection().saveEntity(user);
                }
                if (request.getLon() != 404) {
                    WeatherService.handler(user, request.getLat(), request.getLon(), request.getMoodRating());
                }
                updateMeta(user);
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
