package org.hse.moodactivities.data.entities.mongodb;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

import dev.morphia.annotations.Entity;

@Entity
public final class DailyQuestion implements Serializable {
    @Serial
    private static long serialVersionUID = 0L;
    private LocalTime time;
    private String question;
    private String answer;

    public DailyQuestion(LocalTime time, String question, String answer) {
        this.time = time;
        this.question = question;
        this.answer = answer;
    }

    public DailyQuestion() {
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(final LocalTime time) {
        this.time = time;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DailyQuestion) obj;
        return Objects.equals(this.time, that.time) &&
                Objects.equals(this.question, that.question) &&
                Objects.equals(this.answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, question, answer);
    }

    @Override
    public String toString() {
        return "DailyQuestion[" +
                "time=" + time + ", " +
                "question=" + question + ", " +
                "answer=" + answer + ']';
    }
}
