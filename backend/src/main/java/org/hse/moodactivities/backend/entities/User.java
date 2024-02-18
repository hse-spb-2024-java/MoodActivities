package org.hse.moodactivities.backend.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("users")
public class User {
    @Id
    private String id;
    private String username;
    private int age;

    private Gender gender;

    public enum Gender {
        MALE, FEMALE, ETC
    }

    public User(String id, String username, int age, Gender gender) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
