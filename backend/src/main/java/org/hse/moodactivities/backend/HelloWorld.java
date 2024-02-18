package org.hse.moodactivities.backend;

import org.hse.moodactivities.backend.entities.User;

public class HelloWorld {
    public static void main(String[] argc) {

        System.out.println("Hello, world!");
        MongoDBConnection connection = new MongoDBConnection();

        User user = new User("1", "abc", 12, User.Gender.MALE);

        connection.getDatastore().save(user);

        User retrievedUser = connection.getDatastore().find(User.class).first();

        if (retrievedUser != null) {
            System.out.println("Retrieved user: " + retrievedUser.getUsername() + ", Age: " + retrievedUser.getAge());
        }

        connection.close();
    }
}
