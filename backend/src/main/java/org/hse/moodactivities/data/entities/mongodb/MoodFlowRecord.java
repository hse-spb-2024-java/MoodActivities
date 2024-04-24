package org.hse.moodactivities.data.entities.mongodb;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import dev.morphia.annotations.Entity;

@Entity
public class MoodFlowRecord implements Serializable {
    @Serial
    private static long serialVersionUID = 0L;
    private ArrayList<Mood> moods;
    private ArrayList<Activity> activities;
    private LocalTime time;
    private MoodFlowQuestion question;

    private double score;

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public static long getSerialVersionUID() {
        return MoodFlowRecord.serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        MoodFlowRecord.serialVersionUID = serialVersionUID;
    }

    public ArrayList<Mood> getMoods() {
        return this.moods;
    }

    public void setMoods(ArrayList<Mood> moods) {
        this.moods = moods;
    }

    public ArrayList<Activity> getActivities() {
        return this.activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public MoodFlowQuestion getQuestion() {
        return this.question;
    }

    public void setQuestion(MoodFlowQuestion question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        MoodFlowRecord that = (MoodFlowRecord) o;
        return Objects.equals(this.moods, that.moods) && Objects.equals(this.activities, that.activities) && Objects.equals(this.time, that.time) && Objects.equals(this.question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.moods, this.activities, this.time, this.question);
    }

    @Override
    public String toString() {
        return "MoodFlowRecord{" +
                "moods=" + moods +
                ", score=" + score +
                ", activities=" + activities +
                ", time=" + time +
                ", question=" + question +
                '}';
    }
}

