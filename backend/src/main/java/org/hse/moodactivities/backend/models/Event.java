package org.hse.moodactivities.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name="Event")
public class Event {
    @Id
    private Integer id;

    private String data;

    public Event(int i, String s) {
        id = i;
        data = s;
    }

    public Event() {}
}
