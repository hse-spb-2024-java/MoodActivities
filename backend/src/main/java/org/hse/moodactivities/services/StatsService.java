package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.defaults.DayOfWeek;
import org.hse.moodactivities.common.proto.requests.defaults.PeriodType;
import org.hse.moodactivities.common.proto.requests.stats.FullReportRequest;
import org.hse.moodactivities.common.proto.requests.stats.ReportType;
import org.hse.moodactivities.common.proto.requests.stats.TopListRequest;
import org.hse.moodactivities.common.proto.requests.stats.UsersMoodRequest;
import org.hse.moodactivities.common.proto.requests.stats.WeeklyReportRequest;
import org.hse.moodactivities.common.proto.responses.stats.FullReportResponse;
import org.hse.moodactivities.common.proto.responses.stats.TopItem;
import org.hse.moodactivities.common.proto.responses.stats.TopListResponse;
import org.hse.moodactivities.common.proto.responses.stats.UsersMood;
import org.hse.moodactivities.common.proto.responses.stats.UsersMoodResponse;
import org.hse.moodactivities.common.proto.responses.stats.WeeklyReportResponse;
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.utils.MongoDBSingleton;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private static int periodToInt(PeriodType period) {
        int res = 0;
        switch (period) {
            case WEEK -> res = 7;
            case MONTH -> res = 31;
            case YEAR -> res = 365;
            case ALL -> res = Integer.MAX_VALUE;
        }
        return res;
    }

    private static boolean isDateCorrect(LocalDate possibleDate, LocalDate today, int diff) {
        return ChronoUnit.DAYS.between(possibleDate, today) <= diff;
    }

    @Override
    public void getFullReport(FullReportRequest request, StreamObserver<FullReportResponse> responseObserver) {
        int period = periodToInt(request.getPeriod());
        ReportType reportType = request.getReportType();
        String userId = "123";
        User user = getUser(userId);
        List<String> result;
        if (reportType == ReportType.ACTIVITIES) {
            result = user.getMetas().stream()
                    .map((item) -> item.getActivityList().stream()
                            .map(activityItem -> activityItem.getType()).toList())
                    .limit(period)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } else {
            result = user.getMetas().stream()
                    .map((item) -> item.getMoodList().stream()
                            .map(moodItem -> moodItem.getType()).toList())
                    .limit(period)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
        FullReportResponse response = FullReportResponse.newBuilder().addAllReport(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTopList(TopListRequest request, StreamObserver<TopListResponse> responseObserver) {
        int period = periodToInt(request.getPeriod());
        ReportType reportType = request.getReportType();
        String userId = "123";
        User user = getUser(userId);
        List<TopItem> result;
        if (reportType == ReportType.ACTIVITIES) {
            result = user.getMetas().stream()
                    .map((item) -> item.getActivityList().stream()
                            .map(activityItem -> activityItem.getType()).toList())
                    .flatMap(List::stream).
                    collect(
                            Collectors.groupingBy(
                                    s -> s,
                                    Collectors.counting()
                            ))
                    .entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .limit(3).map(entry -> TopItem.newBuilder()
                            .setName(entry.getKey())
                            .setAmount(Math.toIntExact(entry.getValue()))
                            .build())
                    .toList();
        } else {
            result = user.getMetas().stream()
                    .map((item) -> item.getMoodList().stream()
                            .map(moodItem -> moodItem.getType()).toList())
                    .flatMap(List::stream).
                    collect(
                            Collectors.groupingBy(
                                    s -> s,
                                    Collectors.counting()
                            ))
                    .entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .limit(3).map(entry -> TopItem.newBuilder()
                            .setName(entry.getKey())
                            .setAmount(Math.toIntExact(entry.getValue()))
                            .build())
                    .toList();
        }
        TopListResponse response = TopListResponse.newBuilder().addAllTopReport(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsersMood(UsersMoodRequest request, StreamObserver<UsersMoodResponse> responseObserver) {
        int period = periodToInt(request.getPeriod());
        String userId = "123";
        User user = getUser(userId);
        List<UsersMood> result = user.getMetas().stream()
                .map((item) -> UsersMood.newBuilder()
                        .setDate(item.getDate().toString())
                        .setScore(((Double) item.getDailyScore()).intValue()).build())
                .limit(period).toList();
        UsersMoodResponse response = UsersMoodResponse.newBuilder().addAllUsersMoods(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getWeeklyReport(WeeklyReportRequest request, StreamObserver<WeeklyReportResponse> responseObserver) {
        String id = "123";
        List<UserDayMeta> metas = getUser(id).getMetas();
        int size = metas.size();
        if (size >= 7) {
            metas = metas.subList(size - 7, size);
        }
        LocalDate today = LocalDate.now();
        boolean isCorrect = false;
        for (int i = 0; i < metas.size(); i++) {
            if (isDateCorrect(metas.get(i).getDate(), today, periodToInt(PeriodType.WEEK))) {
                metas = metas.subList(i, metas.size());
                isCorrect = true;
                break;
            }
        }
        if (isCorrect) {
            List<DayOfWeek> days = metas.stream().map(item -> DayOfWeek.forNumber(item.getDate().getDayOfWeek().getValue() - 1)).toList();
            WeeklyReportResponse response = WeeklyReportResponse.newBuilder().addAllListOfDays(days).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            WeeklyReportResponse response = WeeklyReportResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
