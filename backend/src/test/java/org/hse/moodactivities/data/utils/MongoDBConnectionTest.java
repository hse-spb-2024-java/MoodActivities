package org.hse.moodactivities.data.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MongoDBConnectionTest {

    private MongoDBConnection testConnection;

    @BeforeEach
    void setUp() {
        testConnection = new MongoDBConnection("test-data");
    }

    @AfterEach
    void tearDown() {
        testConnection.close();
    }

    @Test
    void saveEntity_SingleEntity_Success() {
        User user = new User("John", new ArrayList<>());
        User savedUser = testConnection.saveEntity(user);
        assertNotNull(savedUser);
    }

    @Test
    void saveEntity_ListOfEntities_Success() {
        List<User> userList = List.of(
                new User("Alice", new ArrayList<>()),
                new User("Bob", new ArrayList<>())
        );
        List<User> savedUsers = testConnection.saveEntity(userList);
        assertNotNull(savedUsers);
        assertEquals(userList.size(), savedUsers.size());
    }

    @Test
    void deleteEntity_SingleEntity_Success() {
        User user = new User("Jane", new ArrayList<>());
        User savedUser = testConnection.saveEntity(user);
        assertNotNull(savedUser);
        assertEquals(1, testConnection.deleteEntity(savedUser).getDeletedCount());
    }

    @Test
    void deleteEntity_ListOfEntities_Success() {
        List<User> userList = List.of(
                new User("Michael", new ArrayList<>()),
                new User("Emma", new ArrayList<>())
        );

        int beforeSaving = testConnection.count(new User());
        List<User> savedUsers = testConnection.saveEntity(userList);

        assertNotNull(savedUsers);
        assertEquals(userList.size(), testConnection.deleteEntity(savedUsers).size());
        assertEquals(testConnection.count(new User()), beforeSaving);
    }

    @Test
    void findEntity_ReturnsListOfEntities_Success() {
        User user = new User("Andrew", new ArrayList<>());
        List<User> userList = testConnection.findEntity(User.class);
        assertNotNull(userList);
        assertTrue(userList.size() > 0);
    }

    @Test
    void findEntityWithFilters_ReturnsFilteredEntities_Success() {
        Map<String, Object> filter = new HashMap<>();
        filter.put("_id", "John");

        List<User> filteredUsers = testConnection.findEntityWithFilters(User.class, filter);

        assertNotNull(filteredUsers);
        assertTrue(filteredUsers.size() > 0);
        assertEquals("John", filteredUsers.get(0).getId());
    }

    @Test
    void updateEntity_EntityExists_Success() {
        User user = new User("Test", new ArrayList<>());

        User savedUser = testConnection.saveEntity(user);

        assertNotNull(savedUser);

        ArrayList<UserDayMeta> newMetas = new ArrayList<>();
        newMetas.add(new UserDayMeta());
        savedUser.setMetas(newMetas);
        User updatedUser = testConnection.updateEntity(savedUser);

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getMetas().size(), 1);
    }

    @Test
    void count_ReturnsNumberOfEntities_Success() {
        int userCount = testConnection.count(new User());
        assertTrue(userCount >= 0);
    }
}
