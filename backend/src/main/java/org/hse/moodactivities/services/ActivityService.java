package org.hse.moodactivities.services;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

import org.hse.moodactivities.common.proto.requests.activity.CheckActivityRequest;
import org.hse.moodactivities.common.proto.requests.activity.GetActivityRequest;
import org.hse.moodactivities.common.proto.requests.activity.RecordActivityRequest;
import org.hse.moodactivities.common.proto.requests.defaults.PeriodType;
import org.hse.moodactivities.common.proto.responses.activity.CheckActivityResponse;
import org.hse.moodactivities.common.proto.responses.activity.GetActivityResponse;
import org.hse.moodactivities.common.proto.responses.activity.RecordActivityResponse;
import org.hse.moodactivities.common.proto.services.ActivityServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.DailyActivity;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.utils.GptClientRequest;
import org.hse.moodactivities.utils.GptMessages;
import org.hse.moodactivities.utils.GptResponse;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.MongoDBSingleton;
import org.hse.moodactivities.utils.PromptGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.grpc.stub.StreamObserver;

public class ActivityService extends ActivityServiceGrpc.ActivityServiceImplBase {

    private Optional<User> getUser(String userId) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("_id", userId);
        List<User> users = MongoDBSingleton.getInstance().getConnection().findEntityWithFilters(User.class, queryMap);
        return (users != null && !users.isEmpty()) ? Optional.of(users.get(0)) : Optional.empty();
    }

    private Optional<String> activityPromptCreator(User user, LocalDate date, String gptMeta) {
        String requestString = PromptGenerator.generatePrompt(user.getMetas(), PromptGenerator.Service.dailyActivity, gptMeta, PeriodType.WEEK);
        requestString = PromptGenerator.addFeedBack(requestString, user);
        GptResponse response = GptClientRequest.sendRequest(new GptMessages(GptMessages.GptMessage.Role.user, requestString));
        if (response.statusCode() < HTTP_BAD_REQUEST) {
            return Optional.of(response.message().getContent());
        }
        return Optional.empty();
    }

    @Override
    public void getActivity(GetActivityRequest request, StreamObserver<GetActivityResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        Optional<User> userOptional = getUser(userId);
        User user = userOptional.orElseGet(User::new);
        if (user.getMetas() == null) {
            user.setMetas(new ArrayList<>());
        }
        LocalDate date = LocalDate.parse(request.getDate());
        Optional<String> activityString = activityPromptCreator(user, date, SurveyService.getMeta(user).orElse(null));
        if (activityString.isEmpty()) {
            responseObserver.onError(new RuntimeException("GPT unavailable"));
        }
        DailyActivity activity = new DailyActivity();
        activity.setTime(LocalTime.now());
        activity.setActivity(activityString.orElse(""));
        if (user.getMetas().isEmpty() || !user.getMetas().getLast().getDate().equals(date)) {
            UserDayMeta newMeta = new UserDayMeta();
            newMeta.setActivity(activity);
            newMeta.setDate(LocalDate.now());
            user.getMetas().add(newMeta);
        } else {
            user.getMetas().getLast().setActivity(activity);
        }
        if (user.getId() == null) {
            user.setId(userId);
            MongoDBSingleton.getInstance().getConnection().saveEntity(user);
        } else {
            MongoDBSingleton.getInstance().getConnection().updateEntity(user);
        }
        GetActivityResponse response = GetActivityResponse.newBuilder().setActivity(activityString.orElse("")).setStatusCode(200).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void recordActivity(RecordActivityRequest request, StreamObserver<RecordActivityResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        Optional<User> userOptional = getUser(userId);
        User user = userOptional.orElseGet(User::new);

        DailyActivity record = user.getMetas().getLast().getActivity();
        record.setTime(LocalTime.now());
        String report = null;
        if (request.getRecord() == null || request.getRecord().isEmpty()) {
            report = "The user completed the activity but decided not to leave a review about it.";
        } else {
            report = request.getRecord();
        }
        record.setReport(report);
        user.getMetas().getLast().setActivity(record);

        MongoDBSingleton.getInstance().getConnection().updateEntity(user);
        RecordActivityResponse response = RecordActivityResponse.newBuilder().setStatusCode(200).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkActivity(CheckActivityRequest request, StreamObserver<CheckActivityResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        Optional<User> userOptional = getUser(userId);
        CheckActivityResponse.Builder responseBuilder = CheckActivityResponse.newBuilder();
        if (userOptional.isPresent()) {
            List<UserDayMeta> metas = userOptional.get().getMetas();
            if (metas != null && !metas.isEmpty()) {
                UserDayMeta meta = metas.getLast();

                if (meta.getDate().equals(LocalDate.parse(request.getDate()))) {
                    DailyActivity report = meta.getActivity();

                    if (report.getActivity() != null) {
                        responseBuilder.setWasCompleted(1);
                        responseBuilder.setActivity(report.getActivity());
                    }
                    if (report.getReport() != null) {
                        responseBuilder.setWasRecorded(1);
                    }
                }
            }
        }

        if (responseBuilder.getWasCompleted() != 1) {
            responseBuilder.setWasCompleted(0);
        }

        responseBuilder.setStatusCode(200);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
