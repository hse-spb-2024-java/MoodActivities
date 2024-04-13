package org.hse.moodactivities.data.entities.mongodb;

import java.util.Date;

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
    private Date dateCreated;

    public Question() {
    }

    public Question(String questionText, Date dateCreated) {
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
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

