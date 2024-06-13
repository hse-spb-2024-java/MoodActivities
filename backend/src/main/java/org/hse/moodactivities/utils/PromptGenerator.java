package org.hse.moodactivities.utils;

import org.hse.moodactivities.common.proto.requests.defaults.PeriodType;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.data.promts.PromptsStorage;
import org.hse.moodactivities.services.ActivityService;
import org.hse.moodactivities.services.StatsService;

import java.util.List;

public class PromptGenerator {
    public static String metaPromptCreator(List<UserDayMeta> metas) {
        StringBuilder requestString = new StringBuilder(PromptsStorage.getString("metaCreator.defaultRequest"));
        List<UserDayMeta> weeklySublist = StatsService.getCorrectDaysSublist(metas, PeriodType.WEEK);
        if (!weeklySublist.isEmpty()) {
            int recordedDays = 0;
            int moodSum = 0;
            String activities = null;
            String emotions = null;
            for (var day : weeklySublist) {
                if (day.getDailyScore() != 0) {
                    recordedDays += 1;
                    moodSum += day.getDailyScore();
                }
            }
            for (int metaIndex = weeklySublist.size() - 1; metaIndex >= 0; metaIndex--) {
                UserDayMeta meta = weeklySublist.get(metaIndex);
                if (meta.getRecords().size() > 0 && emotions == null) {
                    List<String> unwrappedRecords = ActivityService.unwrapRecords(meta.getRecords());
                    emotions = unwrappedRecords.get(0);
                    activities = unwrappedRecords.get(1);
                }
            }
            if (recordedDays > 0) {
                String formattedMoodSum = String.format(PromptsStorage.getString("dailyActivity.addMoodToRequest"), (double) moodSum / recordedDays);
                requestString.append(formattedMoodSum);
            }
            if (emotions != null) {
                String formattedEmotions = String.format(PromptsStorage.getString("dailyActivity.addDailyRecordRequest"), activities, emotions);
                requestString.append(formattedEmotions);
            }
        }
        return requestString.toString();
    }
}
