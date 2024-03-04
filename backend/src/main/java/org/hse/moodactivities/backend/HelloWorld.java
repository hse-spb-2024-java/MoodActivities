package org.hse.moodactivities.backend;

import org.hse.moodactivities.backend.entities.mongodb.User;
import org.hse.moodactivities.utils.MongoDBConnection;

public class HelloWorld {
    public static void main(String[] argc) {

        System.out.println("Hello, world!");
        try (MongoDBConnection connection = new MongoDBConnection("localhost", 27017, "user-data")) {

            User user = new User("123", null);

            connection.saveEntity(user);
            var result = connection.findEntity(User.class);
            System.out.println(result.get(0).getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
