package org.hse.moodactivities.data.entities.mongodb;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

import dev.morphia.annotations.Entity;

@Entity
public final class DailyActivity implements Serializable {
    @Serial
    private static long serialVersionUID = 0L;
    private LocalTime time;
    private String activity;
    private String report;

    public DailyActivity(LocalTime time, String activity, String report) {
        this.time = time;
        this.activity = activity;
        this.report = report;
    }

    public DailyActivity() {
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(final LocalTime time) {
        this.time = time;
    }

    public String getActivity() {
        return this.activity;
    }

    public void setActivity(final String activity) {
        this.activity = activity;
    }

    public String getReport() {
        return this.report;
    }

    public void setReport(final String report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DailyActivity) obj;
        return Objects.equals(this.time, that.time) &&
                Objects.equals(this.activity, that.activity) &&
                Objects.equals(this.report, that.report);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, activity, report);
    }

    @Override
    public String toString() {
        return "DailyActivity[" +
                "time=" + time + ", " +
                "activity=" + activity + ", " +
                "report=" + report + ']';
    }
}
