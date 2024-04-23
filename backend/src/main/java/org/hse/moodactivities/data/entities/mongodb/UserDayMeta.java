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
public final class UserDayMeta implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    private LocalDate date;
    private List<Activity> activityList = new ArrayList<>();
    private List<Mood> moodList = new ArrayList<>();
    private String answerToQuestion;
    private double dailyScore;
    private String dailyConclusion;

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public void setActivityList(final List<Activity> activityList) {
        this.activityList = activityList;
    }

    public void setMoodList(final List<Mood> moodList) {
        this.moodList = moodList;
    }

    public void addActivity(Activity activity) {
        this.activityList.add(activity);
    }

    public void addMood(Mood mood) {
        this.moodList.add(mood);
    }

    public void setDailyScore(final double dailyScore) {
        this.dailyScore = dailyScore;
    }

    public void setDailyConclusion(final String dailyConclusion) {
        this.dailyConclusion = dailyConclusion;
    }

    public void setAnswerToQuestion(String answer) {
        answerToQuestion = answer;
        if (date == null) {
            date = LocalDate.now();
        }
    }

    public String getAnswerToQuestion() {
        return answerToQuestion;
    }

    public UserDayMeta(final LocalDate date, final List<Activity> activityList, final double dailyScore, final String dailyConclusion) {
        this.date = date;
        this.activityList = activityList;
        this.dailyScore = dailyScore;
        this.dailyConclusion = dailyConclusion;
    }

    public UserDayMeta(LongSurveyRequest longSurveyRequest) {
        LocalTime time = LocalTime.ofSecondOfDay(longSurveyRequest.getTimeModuloDayInSeconds());
        for (var activity : longSurveyRequest.getActivitiesList()) {
            activityList.add(new Activity(activity, time, 0.5, ""));
        }
        for (var emotion : longSurveyRequest.getEmotionsList()) {
            moodList.add(new Mood(emotion, time, 0.5, ""));
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

    public List<Activity> getActivityList() {
        return activityList;
    }

    public List<Mood> getMoodList() {
        return Collections.unmodifiableList(moodList);
    }

    public double getDailyScore() {
        return dailyScore;
    }

    public String getDailyConclusion() {
        return dailyConclusion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDayMeta) obj;
        return Objects.equals(this.date, that.date) &&
                Objects.equals(this.activityList, that.activityList) &&
                Objects.equals(this.moodList, that.moodList) &&
                Double.doubleToLongBits(this.dailyScore) == Double.doubleToLongBits(that.dailyScore) &&
                Objects.equals(this.dailyConclusion, that.dailyConclusion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, activityList, moodList, dailyScore, dailyConclusion);
    }

    @Override
    public String toString() {
        return "UserDayMeta[" +
                "date=" + date + ", " +
                "activityList=" + activityList + ", " +
                "moodList=" + moodList + ", " +
                "daiScore=" + dailyScore + ", " +
                "dailyConclusion=" + dailyConclusion + ']';
    }
}
