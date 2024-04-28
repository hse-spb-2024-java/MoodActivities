package org.hse.moodactivities.services;

import static java.net.HttpURLConnection.HTTP_OK;

import org.hse.moodactivities.common.proto.requests.dailyQuestion.AnswerRequest;
import org.hse.moodactivities.common.proto.requests.dailyQuestion.CheckAnswerRequest;
import org.hse.moodactivities.common.proto.requests.dailyQuestion.QuestionRequest;
import org.hse.moodactivities.common.proto.responses.dailyQuestion.AnswerResponse;
import org.hse.moodactivities.common.proto.responses.dailyQuestion.CheckAnswerResponse;
import org.hse.moodactivities.common.proto.responses.dailyQuestion.QuestionResponse;
import org.hse.moodactivities.common.proto.services.QuestionServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.MongoDBSingleton;
import org.hse.moodactivities.utils.StringGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.grpc.stub.StreamObserver;

public class QuestionService extends QuestionServiceGrpc.QuestionServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionService.class);

    @Override
    public void getDailyQuestion(QuestionRequest request, StreamObserver<QuestionResponse> responseObserver) {
        String question = StringGenerationService.getLastGeneratedString();
        QuestionResponse serviceResponse = QuestionResponse.newBuilder().setQuestion(question).setStatusCode(HTTP_OK).build();
        responseObserver.onNext(serviceResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getRandomQuestion(QuestionRequest request, StreamObserver<QuestionResponse> responseObserver) {
        String question = StringGenerationService.getRandomQuestion();
        QuestionResponse serviceResponse = QuestionResponse.newBuilder().setQuestion(question).setStatusCode(HTTP_OK).build();
        responseObserver.onNext(serviceResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void checkDailyQuestion(CheckAnswerRequest request, StreamObserver<CheckAnswerResponse> responseObserver) {
        Map<String, Object> queryMap = new HashMap<>();
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        queryMap.put("_id", userId);
        List<User> users = MongoDBSingleton.getInstance().getConnection().findEntityWithFilters(User.class, queryMap);
        User user;
        CheckAnswerResponse response = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
            if (user.getMetas() != null && !user.getMetas().isEmpty()) {
                UserDayMeta lastMeta = user.getMetas().getLast();
                if (Objects.equals(lastMeta.getDate(), LocalDate.now()) && lastMeta.getQuestion().getAnswer() != null) {
                    response = CheckAnswerResponse.newBuilder().setHasAnswer(1).setAnswer(lastMeta.getQuestion().getAnswer()).build();
                }
            }
        }
        if (response == null) {
            response = CheckAnswerResponse.newBuilder().setHasAnswer(0).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendDailyQuestionAnswer(AnswerRequest request, StreamObserver<AnswerResponse> responseObserver) {
        Map<String, Object> queryMap = new HashMap<>();
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        queryMap.put("_id", userId);
        List<User> users = MongoDBSingleton.getInstance().getConnection().findEntityWithFilters(User.class, queryMap);
        User user;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        } else {
            user = new User(userId, new ArrayList<>());
            MongoDBSingleton.getInstance().getConnection().saveEntity(user);
        }
        if (user.getMetas() == null || user.getMetas().isEmpty() || user.getMetas().getLast().getDate() != LocalDate.now()) {
            UserDayMeta newMeta = new UserDayMeta(LocalDate.now());
            newMeta.getQuestion().setAnswer(request.getAnswer());
            newMeta.getQuestion().setTime(LocalTime.now());
            user.updateMeta(newMeta);
            LOGGER.info("New record for:" + userId);
            MongoDBSingleton.getInstance().getConnection().updateEntity(user);
        } else {
            UserDayMeta oldMeta = user.getMetas().getLast();
            if (oldMeta.getQuestion().getAnswer() == null) {
                oldMeta.getQuestion().setAnswer(request.getAnswer());
                oldMeta.getQuestion().setTime(LocalTime.now());
                user.updateMeta(oldMeta);
                LOGGER.info("Update record for:" + userId);
                MongoDBSingleton.getInstance().getConnection().updateEntity(user);
            }
        }
        AnswerResponse response = AnswerResponse.newBuilder().setStatusCode(200).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
