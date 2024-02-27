package org.hse.moodactivities.backend.entities.mongodb;

import dev.morphia.annotations.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity("meta")
final class UserDayMeta implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    private Date date;
    private List<Activities> activitiesList;
    private double daiScore;
    private String dailyConclusion;

    public void setDate(final Date date) {
        this.date = date;
    }
    public void setActivitiesList(final List<Activities> activitiesList) {
        this.activitiesList = activitiesList;
    }

    public void setDaiScore(final double daiScore) {
        this.daiScore = daiScore;
    }

    public void setDailyConclusion(final String dailyConclusion) {
        this.dailyConclusion = dailyConclusion;
    }

    public UserDayMeta(final Date date, final List<Activities> activitiesList, final double daiScore, final String dailyConclusion) {
        this.date = date;
        this.activitiesList = activitiesList;
        this.daiScore = daiScore;
        this.dailyConclusion = dailyConclusion;
    }



    public Date getDate() {
        return date;
    }

    public List<Activities> getActivitiesList() {
        return activitiesList;
    }

    public double getDaiScore() {
        return daiScore;
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
                Objects.equals(this.activitiesList, that.activitiesList) &&
                Double.doubleToLongBits(this.daiScore) == Double.doubleToLongBits(that.daiScore) &&
                Objects.equals(this.dailyConclusion, that.dailyConclusion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, activitiesList, daiScore, dailyConclusion);
    }

    @Override
    public String toString() {
        return "UserDayMeta[" +
                "date=" + date + ", " +
                "activitiesList=" + activitiesList + ", " +
                "daiScore=" + daiScore + ", " +
                "dailyConclusion=" + dailyConclusion + ']';
    }


}
