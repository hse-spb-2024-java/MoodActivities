package org.hse.moodactivities.backend.entities.mongodb;

import dev.morphia.annotations.Entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.time.*;

@Entity
final class Mood implements Serializable {
    @Serial
    private static long serialVersionUID = 0L;
    private String type;
    private LocalTime time;

    private double score;

    private String comments;

    public static void setSerialVersionUID(final long serialVersionUID) {
        Mood.serialVersionUID = serialVersionUID;
    }

    public void setTime(final LocalTime time) {
        this.time = time;
    }

    public void setScore(final double score) {
        this.score = score;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    Mood(String type, LocalTime time, double score, String comments) {
        this.type = type;
        this.time = time;
        this.score = score;
        this.comments = comments;
    }

    public double getScore() {
        return this.score;
    }

    public String getType() {
        return this.type;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public String getComments() {
        return this.comments;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Mood) obj;
        return Objects.equals(this.type, that.type) &&
                Objects.equals(this.time, that.time) &&
                Objects.equals(this.comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, time, comments);
    }

    @Override
    public String toString() {
        return "Mood[" +
                "type=" + type + ", " +
                "time=" + time + ", " +
                "score=" + score + ", " +
                "comments=" + comments + ']';
    }
}
