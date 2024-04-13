package org.hse.moodactivities.data.entities.mongodb;

import java.time.LocalDate;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;

@Entity("questions")
public class Question {

    @Id
    private String id;

    @Property
    private String questionText;

    @Property
    private LocalDate dateCreated;

    public Question() {
    }

    public Question(String questionText, LocalDate dateCreated) {
        this.questionText = questionText;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", questionText='" + questionText + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}

