package org.hse.moodactivities.services;

import org.hse.moodactivities.common.proto.requests.defaults.ActivityRecord;
import org.hse.moodactivities.common.proto.requests.defaults.DayOfWeek;
import org.hse.moodactivities.common.proto.requests.defaults.MoodRecord;
import org.hse.moodactivities.common.proto.requests.defaults.PeriodType;
import org.hse.moodactivities.common.proto.requests.defaults.QuestionRecord;
import org.hse.moodactivities.common.proto.requests.stats.AllDayRequest;
import org.hse.moodactivities.common.proto.requests.stats.DaysMoodRequest;
import org.hse.moodactivities.common.proto.requests.stats.FullReportRequest;
import org.hse.moodactivities.common.proto.requests.stats.MoodForTheMonthRequest;
import org.hse.moodactivities.common.proto.requests.stats.ReportType;
import org.hse.moodactivities.common.proto.requests.stats.TopListRequest;
import org.hse.moodactivities.common.proto.requests.stats.UsersMoodRequest;
import org.hse.moodactivities.common.proto.requests.stats.WeeklyReportRequest;
import org.hse.moodactivities.common.proto.responses.stats.AllDayResponse;
import org.hse.moodactivities.common.proto.responses.stats.DaysMoodResponse;
import org.hse.moodactivities.common.proto.responses.stats.FullReportResponse;
import org.hse.moodactivities.common.proto.responses.stats.MoodForTheMonthResponse;
import org.hse.moodactivities.common.proto.responses.stats.TopItem;
import org.hse.moodactivities.common.proto.responses.stats.TopListResponse;
import org.hse.moodactivities.common.proto.responses.stats.UsersMood;
import org.hse.moodactivities.common.proto.responses.stats.UsersMoodResponse;
import org.hse.moodactivities.common.proto.responses.stats.WeeklyReportResponse;
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.MongoDBSingleton;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.grpc.stub.StreamObserver;

public class StatsService extends StatsServiceGrpc.StatsServiceImplBase {

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

    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_IN_MONTH = 31;
    private static final int DAYS_IN_YEAR = 366;
    private static final int DAYS_IN_UNIVERSE = Integer.MAX_VALUE;

    private static int periodToInt(PeriodType period) {
        int res = 0;
        switch (period) {
            case WEEK -> res = DAYS_IN_WEEK;
            case MONTH -> res = DAYS_IN_MONTH;
            case YEAR -> res = DAYS_IN_YEAR;
            case ALL -> res = DAYS_IN_UNIVERSE;
        }
        return res;
    }

    private static boolean isDateCorrect(LocalDate possibleDate, LocalDate today, int diff) {
        return today.toEpochDay() - possibleDate.toEpochDay() < diff;
    }

    private static List<UserDayMeta> getCorrectDaysSublist(List<UserDayMeta> metas, PeriodType period) {
        if (metas == null) {
            return new ArrayList<>();
        }
        int size = metas.size();
        if (size >= periodToInt(period)) {
            metas = metas.subList(size - periodToInt(period), size);
        }
        LocalDate today = LocalDate.now();
        boolean isCorrect = false;
        for (int i = 0; i < metas.size(); i++) {
            if (isDateCorrect(metas.get(i).getDate(), today, periodToInt(period))) {
                metas = metas.subList(i, metas.size());
                isCorrect = true;
                break;
            }
        }
        return isCorrect ? metas : new ArrayList<>();
    }

    private static UserDayMeta getMetaByDate(User user, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateString, formatter);

        } catch (DateTimeParseException e) {
            System.out.println("Error parsing the date: " + e.getMessage());
            return null;
        }
        List<UserDayMeta> metas = user.getMetas();
        if (metas == null || metas.isEmpty()) {
            return null;
        }
        LocalDate finalDate = date;
        List<UserDayMeta> acceptedMetas = metas.stream()
                .filter(meta -> meta.getDate().equals(finalDate))
                .limit(1)
                .toList();
        return acceptedMetas.isEmpty() ? null : acceptedMetas.getLast();
    }


    private static int mapAmount(int amount) {
        return amount == 0 ? Integer.MAX_VALUE : amount;
    }

    private static String removeMS(String time) {
        int lastDotIndex = time.lastIndexOf('.');
        if (lastDotIndex != -1) {
            String result = time.substring(0, lastDotIndex);
            return result;
        } else {
            throw new RuntimeException("bad time format in db");
        }
    }

    private static String removeSeconds(String time) {
        int endIndex = 5;
        if (endIndex < time.length()) {
            String result = time.substring(0, endIndex);
            return result;
        } else {
            throw new RuntimeException("bad time format in db");
        }
    }

    @Override
    public void allDayReport(AllDayRequest request, StreamObserver<AllDayResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        User user = getUser(userId);
        List<MoodRecord> records = new ArrayList<>();
        UserDayMeta meta = getMetaByDate(user, request.getDate());
        AllDayResponse response = null;
        if (meta == null) {
            response = AllDayResponse.newBuilder().setDate(request.getDate()).build();
        } else {
            for (var record : meta.getRecords()) {
                MoodRecord newRecord = MoodRecord.newBuilder()
                        .addAllMoods(record.getMoods().stream().map(item -> item.getType()).toList())
                        .addAllActivities(record.getActivities().stream().map(item -> item.getType()).toList())
                        .setQuestion(record.getQuestion().getQuestion())
                        .setAnswer(record.getQuestion().getAnswer())
                        .setScore(record.getScore())
                        .setTime(removeSeconds(record.getTime().toString()))
                        .setShortSummary(record.getShortSummary())
                        .build();
                records.add(newRecord);
            }

            QuestionRecord question = null;
            if (meta.getQuestion().getQuestion() != null) {
                question = QuestionRecord.newBuilder()
                        .setQuestion(meta.getQuestion().getQuestion())
                        .setAnswer(meta.getQuestion().getAnswer()).build();
            }

            ActivityRecord activity = null;
            if (meta.getActivity().getActivity() != null) {
                activity = ActivityRecord.newBuilder()
                        .setActivity(meta.getActivity().getActivity())
                        .setReport(meta.getActivity().getReport())
                        .build();
            }

            response = AllDayResponse.newBuilder()
                    .addAllRecords(records)
                    .setDate(request.getDate())
                    .setScore(meta.getDailyScore())
                    .setQuestion(question == null ? QuestionRecord.getDefaultInstance() : question)
                    .setActivity(activity == null ? ActivityRecord.getDefaultInstance() : activity)
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getDaysMood(DaysMoodRequest request, StreamObserver<DaysMoodResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        User user = getUser(userId);
        UserDayMeta meta = getMetaByDate(user, request.getDate());
        DaysMoodResponse response = DaysMoodResponse.newBuilder()
                .setScore(meta == null ? 0 : meta.getDailyScore())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getFullReport(FullReportRequest request, StreamObserver<FullReportResponse> responseObserver) {
        ReportType reportType = request.getReportType();
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        User user = getUser(userId);
        List<String> result = null;
        if (reportType == ReportType.ACTIVITIES) {
            result = getCorrectDaysSublist(user.getMetas(), request.getPeriod())
                    .stream()
                    .map((item) -> item.getRecords().stream().collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .map((recordItem) -> recordItem.getActivities())
                    .flatMap(List::stream)
                    .map(activityItem -> activityItem.getType())
                    .toList();

        } else {
            result = getCorrectDaysSublist(user.getMetas(), request.getPeriod())
                    .stream()
                    .map((item) -> item.getRecords().stream().collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .map((recordItem) -> recordItem.getMoods())
                    .flatMap(List::stream)
                    .map(moodItem -> moodItem.getType())
                    .toList();
        }
        FullReportResponse response = FullReportResponse.newBuilder().addAllReport(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTopList(TopListRequest request, StreamObserver<TopListResponse> responseObserver) {
        ReportType reportType = request.getReportType();
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        User user = getUser(userId);
        Stream<String> stream;
        if (reportType == ReportType.ACTIVITIES) {
            stream = getCorrectDaysSublist(user.getMetas(), request.getPeriod()).stream()
                    .map((item) -> item.getRecords().stream().collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .map((recordItem) -> recordItem.getActivities())
                    .flatMap(List::stream)
                    .map(activityItem -> activityItem.getType());
        } else {
            stream = getCorrectDaysSublist(user.getMetas(), request.getPeriod()).stream()
                    .map((item) -> item.getRecords().stream().collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .map((recordItem) -> recordItem.getMoods())
                    .flatMap(List::stream)
                    .map(moodItem -> moodItem.getType());
        }
        List<TopItem> result = stream
                .collect(
                        Collectors.groupingBy(
                                s -> s,
                                Collectors.counting()
                        ))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(mapAmount(request.getAmount()))
                .map(entry -> TopItem.newBuilder()
                        .setName(entry.getKey())
                        .setAmount(Math.toIntExact(entry.getValue()))
                        .build())
                .toList();
        TopListResponse response = TopListResponse.newBuilder().addAllTopReport(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsersMood(UsersMoodRequest request, StreamObserver<UsersMoodResponse> responseObserver) {
        int period = periodToInt(request.getPeriod());
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        User user = getUser(userId);
        List<UsersMood> result = getCorrectDaysSublist(user.getMetas(), request.getPeriod()).stream()
                .map((item) -> UsersMood.newBuilder()
                        .setDate(item.getDate().toString())
                        .setScore(((Double) item.getDailyScore()).intValue()).build())
                .limit(period)
                .sorted((lhs, rhs) -> LocalDate.parse(lhs.getDate()).compareTo(LocalDate.parse(rhs.getDate()))).toList();
        UsersMoodResponse response = UsersMoodResponse.newBuilder().addAllUsersMoods(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getWeeklyReport(WeeklyReportRequest request, StreamObserver<WeeklyReportResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        List<UserDayMeta> metas = getCorrectDaysSublist(getUser(userId).getMetas(), PeriodType.WEEK);
        List<DayOfWeek> days = metas.stream()
                .map(item -> DayOfWeek.forNumber(item.getDate().getDayOfWeek().getValue() - 1))
                .toList();
        WeeklyReportResponse response = WeeklyReportResponse.newBuilder().addAllListOfDays(days).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMoodForTheMonth(MoodForTheMonthRequest request, StreamObserver<MoodForTheMonthResponse> responseObserver) {
        String userId = JWTUtils.CLIENT_ID_CONTEXT_KEY.get();
        List<UserDayMeta> metas = getUser(userId).getMetas();
        List<DaysMoodResponse> responses = metas.stream()
                .filter((item) ->
                        (item.getDate().getMonth().equals(Month.of(request.getMonth()))) &&
                                item.getDate().getYear() == request.getYear())
                .map((item) -> DaysMoodResponse.newBuilder()
                        .setDate(item.getDate().toString())
                        .setScore(item.getDailyScore()).build()
                ).collect(Collectors.toList());
        MoodForTheMonthResponse response = MoodForTheMonthResponse.newBuilder()
                .addAllRecordedDays(responses)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
