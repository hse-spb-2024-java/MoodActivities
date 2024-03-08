package org.hse.moodactivities.data.entities.mongodb;

import dev.morphia.annotations.Entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
final class Activities implements Serializable {
    @Serial
    private static long serialVersionUID = 0L;
    private String type;
    private double score;
    private String comments;

    public static void setSerialVersionUID(final long serialVersionUID) {
        Activities.serialVersionUID = serialVersionUID;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setScore(final double score) {
        this.score = score;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    Activities(String type, double score, String comments) {
        this.type = type;
        this.score = score;
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public double getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Activities) obj;
        return Objects.equals(this.type, that.type) &&
                Double.doubleToLongBits(this.score) == Double.doubleToLongBits(that.score) &&
                Objects.equals(this.comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, score, comments);
    }

    @Override
    public String toString() {
        return "Activities[" +
                "type=" + type + ", " +
                "score=" + score + ", " +
                "comments=" + comments + ']';
    }
}
