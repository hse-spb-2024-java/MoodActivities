package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.stats.FullReportRequest;
import org.hse.moodactivities.common.proto.requests.stats.TopListRequest;
import org.hse.moodactivities.common.proto.requests.stats.UsersMoodRequest;
import org.hse.moodactivities.common.proto.requests.stats.WeeklyReportRequest;
import org.hse.moodactivities.common.proto.responses.stats.FullReportResponse;
import org.hse.moodactivities.common.proto.responses.stats.TopListResponse;
import org.hse.moodactivities.common.proto.responses.stats.UsersMoodResponse;
import org.hse.moodactivities.common.proto.responses.stats.WeeklyReportResponse;
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.utils.MongoDBSingleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.stub.StreamObserver;

public class StatsService extends StatsServiceGrpc.StatsServiceImplBase {

    private static User getUser(String userId) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("id", "123");
        List<User> users = MongoDBSingleton.getInstance().getConnection().findEntityWithFilters(User.class, queryMap);

        if (users != null && !users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void getFullReport(FullReportRequest request, StreamObserver<FullReportResponse> responseObserver) {

    }

    @Override
    public void getEmotionsList(TopListRequest request, StreamObserver<TopListResponse> responseObserver) {

    }

    @Override
    public void getUsersMood(UsersMoodRequest request, StreamObserver<UsersMoodResponse> responseObserver) {

    }

    @Override
    public void getWeeklyReport(WeeklyReportRequest request, StreamObserver<WeeklyReportResponse> responseObserver) {

    }
}
