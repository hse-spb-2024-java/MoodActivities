package org.hse.moodactivities.data.entities.mongodb;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import dev.morphia.annotations.Entity;

@Entity
public final class MoodFlowQuestion implements Serializable {
    @Serial
    private static long serialVersionUID = 0L;
    private final String question;
    private final String answer;

    public MoodFlowQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MoodFlowQuestion) obj;
        return Objects.equals(this.question, that.question) &&
                Objects.equals(this.answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, answer);
    }

    @Override
    public String toString() {
        return "MoodFlowQuestion[" +
                "question=" + question + ", " +
                "answer=" + answer + ']';
    }
}
