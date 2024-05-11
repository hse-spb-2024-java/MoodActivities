package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.activity.CheckActivityRequest;
import org.hse.moodactivities.common.proto.requests.activity.GetActivityRequest;
import org.hse.moodactivities.common.proto.requests.activity.RecordActivityRequest;
import org.hse.moodactivities.common.proto.responses.activity.CheckActivityResponse;
import org.hse.moodactivities.common.proto.responses.activity.GetActivityResponse;
import org.hse.moodactivities.common.proto.responses.activity.RecordActivityResponse;
import org.hse.moodactivities.common.proto.services.ActivityServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.DailyActivity;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.MongoDBSingleton;

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

    private String activityPromptCreator(List<UserDayMeta> metas, LocalDate date) {
        // TODO: idea for gpt
        return "touch the grass";
    }

    @Override
    public void getActivity(GetActivityRequest request, StreamObserver<GetActivityResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        Optional<User> userOptional = getUser(userId);
        User user = userOptional.isEmpty() ? new User() : userOptional.get();
        if (user.getMetas() == null) {
            user.setMetas(new ArrayList<>());
        }
        LocalDate date = LocalDate.parse(request.getDate());
        String activityString = activityPromptCreator(user.getMetas(), date);
        DailyActivity activity = new DailyActivity();
        activity.setTime(LocalTime.now());
        activity.setActivity(activityString);
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
    }

    @Override
    public void recordActivity(RecordActivityRequest request, StreamObserver<RecordActivityResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        Optional<User> userOptional = getUser(userId);
        User user = userOptional.isEmpty() ? new User() : userOptional.get();
        DailyActivity record = user.getMetas().getLast().getActivity();
        record.setTime(LocalTime.now());
        record.setReport(request.getRecord());
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
        if (!userOptional.isEmpty()) {
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
