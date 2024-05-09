package org.hse.moodactivities.data.entities.mongodb;

import org.hse.moodactivities.common.proto.requests.survey.LongSurveyRequest;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.morphia.annotations.Entity;

@Entity("meta")
public class UserDayMeta implements Serializable {
    @Serial
    private static long serialVersionUID = 0L;

    private LocalDate date;
    private List<MoodFlowRecord> records;

    private DailyQuestion question;
    private double dailyScore = 0;
    private DailyActivity activity;
    private String dailyConclusion;

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDailyScore(double dailyScore) {
        this.dailyScore = dailyScore;
    }

    public void setDailyConclusion(String dailyConclusion) {
        this.dailyConclusion = dailyConclusion;
    }

    public List<MoodFlowRecord> getRecords() {
        if (this.records == null) {
            this.records = new ArrayList<>();
        }
        return Collections.unmodifiableList(this.records);
    }

    public void setRecords(List<MoodFlowRecord> records) {
        this.records = records;
        calculateDailyScore();
    }

    public void addRecords(MoodFlowRecord record) {
        this.records.add(record);
        calculateDailyScore();
    }

    public UserDayMeta(LocalDate date, List<MoodFlowRecord> records, double dailyScore, String dailyConclusion) {
        this.date = date;
        this.records = records;
        this.dailyScore = dailyScore;
        this.dailyConclusion = dailyConclusion;
    }

    public UserDayMeta(LongSurveyRequest longSurveyRequest) {
        LocalTime time = LocalTime.ofSecondOfDay(longSurveyRequest.getTimeModuloDayInSeconds());
        ArrayList<Activity> activityList = new ArrayList<>();
        ArrayList<Mood> moodList = new ArrayList<>();
        for (var activity : longSurveyRequest.getActivitiesList()) {
            activityList.add(new Activity(activity, time, 0.5, ""));
        }
        for (var emotion : longSurveyRequest.getEmotionsList()) {
            moodList.add(new Mood(emotion, time, 0.5, ""));
        }
        if (this.records == null) {
            this.records = new ArrayList<>();
        }
        RecordQuestion question = new RecordQuestion(longSurveyRequest.getQuestion(), longSurveyRequest.getAnswer());
        MoodFlowRecord newRecord = new MoodFlowRecord();
        newRecord.setActivities(activityList);
        newRecord.setMoods(moodList);
        newRecord.setQuestion(question);
        newRecord.setTime(LocalTime.now());
        newRecord.setScore(longSurveyRequest.getMoodRating());
        this.records.add(newRecord);
        calculateDailyScore();
        if (date == null) {
            date = LocalDate.now();
        }
    }

    public UserDayMeta(LocalDate date) {
        this.date = date;
    }

    public UserDayMeta() {
    }

    public LocalDate getDate() {
        return date;
    }

    public double getDailyScore() {
        return dailyScore;
    }

    public String getDailyConclusion() {
        return dailyConclusion;
    }

    public DailyQuestion getQuestion() {
        return this.question == null ? new DailyQuestion() : this.question;
    }

    public void setQuestion(DailyQuestion question) {
        this.question = question;
    }

    public DailyActivity getActivity() {
        return this.activity == null ? new DailyActivity() : this.activity;
    }

    public void setActivity(DailyActivity activity) {
        this.activity = activity;
    }

    private void calculateDailyScore() {
        if (records == null) {
            return;
        }
        double sum = 0;
        int recordsWithScore = 0;
        for (var record : records) {
            if (record.getScore() != 0) {
                recordsWithScore += 1;
            }
            sum += record.getScore();
        }
        dailyScore = sum / recordsWithScore;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDayMeta) obj;
        return Objects.equals(this.date, that.date) &&
                Double.doubleToLongBits(this.dailyScore) == Double.doubleToLongBits(that.dailyScore) &&
                Objects.equals(this.dailyConclusion, that.dailyConclusion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, records, dailyScore, dailyConclusion);
    }

    @Override
    public String toString() {
        return "UserDayMeta[" +
                "date=" + date + ", " +
                "records=" + records + ", " +
                "daiScore=" + dailyScore + ", " +
                "dailyConclusion=" + dailyConclusion + ']';
    }
}
