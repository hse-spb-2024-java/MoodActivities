package org.hse.moodactivities.utils;

import org.hse.moodactivities.common.proto.requests.defaults.PeriodType;
import org.hse.moodactivities.data.entities.mongodb.MoodFlowRecord;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.data.promts.PromptsStorage;
import org.hse.moodactivities.services.StatsService;

import java.util.ArrayList;
import java.util.List;

public class PromptGenerator {

    public enum Service {
        metaCreator,
        aiThinker,
        aiAdvice,
        dailyActivity
    }

    public static String generatePrompt(List<UserDayMeta> metas, Service service, String gptMeta, PeriodType period) {
        StringBuilder requestString = new StringBuilder(PromptsStorage.getString(service.toString() + ".defaultRequest"));
        List<UserDayMeta> metaSublist = StatsService.getCorrectDaysSublist(metas, period);
        if (!metaSublist.isEmpty()) {
            int recordedDays = 0;
            int moodSum = 0;
            String activities = null;
            String emotions = null;
            String lastGeneratedActivity = null;
            String lastGeneratedActivityReport = null;
            for (var day : metaSublist) {
                if (day.getDailyScore() != 0) {
                    recordedDays += 1;
                    moodSum += (int) day.getDailyScore();
                }
            }
            for (int metaIndex = metaSublist.size() - 1; metaIndex >= 0; metaIndex--) {
                UserDayMeta meta = metaSublist.get(metaIndex);
                if (!meta.getRecords().isEmpty() && emotions == null) {
                    List<String> unwrappedRecords = unwrapRecords(meta.getRecords());
                    emotions = unwrappedRecords.get(0);
                    activities = unwrappedRecords.get(1);
                }
                if (meta.getActivity().getReport() != null && lastGeneratedActivity == null) {
                    lastGeneratedActivity = meta.getActivity().getActivity();
                    lastGeneratedActivityReport = meta.getActivity().getReport();
                }
            }
            if (recordedDays > 0) {
                String formattedMoodSum = String.format(PromptsStorage.getString("common.addMoodToRequest"), (double) moodSum / recordedDays);
                requestString.append(formattedMoodSum);
            }
            if (emotions != null) {
                String formattedEmotions = String.format(PromptsStorage.getString("common.addDailyRecordRequest"), activities, emotions);
                requestString.append(formattedEmotions);
            }
            if (lastGeneratedActivity != null && service != Service.metaCreator) {
                String formattedActivities = String.format(PromptsStorage.getString("common.addPreviousActivities"), lastGeneratedActivity, lastGeneratedActivityReport);
                requestString.append(formattedActivities);
            }
        }
        if (gptMeta != null && service == Service.dailyActivity) {
            String formattedMeta = String.format(PromptsStorage.getString("metaCreator.promptForMeta"), gptMeta);
            requestString.append(formattedMeta);
        }
        if (service == Service.aiThinker) {
            requestString.append(PromptsStorage.getString("common.personalAdvice"));
        }
        return requestString.toString();
    }

    public static List<String> unwrapRecords(List<MoodFlowRecord> records) {
        ArrayList<String> emotionsAndActivities = new ArrayList<>();
        StringBuilder activities = new StringBuilder();
        StringBuilder emotions = new StringBuilder();
        for (var record : records) {
            for (var activity : record.getActivities()) {
                activities.append(activity.getType()).append(", ");
            }
            for (var emotion : record.getMoods()) {
                emotions.append(emotion.getType()).append(", ");
            }
        }
        emotionsAndActivities.add(emotions.toString());
        emotionsAndActivities.add(activities.toString());
        return emotionsAndActivities;
    }

    public static String addFeedBack(String requestString, User user) {
        StringBuilder requestStringBuilder = new StringBuilder();
        requestStringBuilder.append(requestString);
        if (user.getPositiveFeedback() != null) {
            requestStringBuilder.append(String.format(PromptsStorage.getString("common.positiveFeedback"), user.getPositiveFeedback()));
        }
        if (user.getNegativeFeedback() != null) {
            requestStringBuilder.append(String.format(PromptsStorage.getString("common.negativeFeedback"), user.getNegativeFeedback()));
        }
        return requestStringBuilder.toString();
    }
}
