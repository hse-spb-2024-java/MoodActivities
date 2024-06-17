package org.hse.moodactivities.services;

import io.grpc.stub.StreamObserver;
import org.hse.moodactivities.common.proto.defaults.Empty;
import org.hse.moodactivities.common.proto.requests.stats.UploadFitnessDataRequest;
import org.hse.moodactivities.common.proto.services.HealthServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.FitnessData;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.MongoDBSingleton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthService extends HealthServiceGrpc.HealthServiceImplBase {
    private static User getUser(String userId) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("_id", userId);
        List<User> users = MongoDBSingleton.getInstance().getConnection().findEntityWithFilters(User.class, queryMap);

        if (users != null && !users.isEmpty()) {
            return users.get(0);
        } else {
            User user = new User(userId, new ArrayList<>());
            MongoDBSingleton.getInstance().getConnection().saveEntity(user);
            return user;
        }
    }

    @Override
    public void uploadFitnessData(UploadFitnessDataRequest request, StreamObserver<Empty> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        User user = getUser(userId);
        List<UserDayMeta> metas = user.getMetas();
        UserDayMeta lastMeta = null;
        if (metas == null || metas.isEmpty() || !metas.getLast().getDate().equals(LocalDate.now())) {
            lastMeta = new UserDayMeta(LocalDate.now());
        } else {
            lastMeta = metas.getLast();
        }
        FitnessData fitnessData = lastMeta.getFitnessData();
        if (fitnessData == null) {
            fitnessData = new FitnessData();
        }
        fitnessData.setSteps(request.getStepsForLastDay());
        lastMeta.setFitnessData(fitnessData);
        user.updateMeta(lastMeta);
        MongoDBSingleton.getInstance().getConnection().saveEntity(user);
        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
