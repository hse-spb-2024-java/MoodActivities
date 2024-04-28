package org.hse.moodactivities.data.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.hse.moodactivities.data.entities.mongodb.Question;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import io.github.cdimascio.dotenv.Dotenv;

public class MongoDBConnection implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBConnection.class);

    private static Dotenv dotenv = Dotenv.load();
    private static final String MONGO_HOST = dotenv.get("MONGO_HOST");
    private static final int MONGO_PORT = Integer.valueOf(dotenv.get("MONGO_PORT"));
    private static final String MONGO_USERS_DBNAME = dotenv.get("MONGO_USERS_DBNAME");
    private static final String MONGO_QUESTIONS_DBNAME = dotenv.get("MONGO_QUESTIONS_DBNAME");

    private static final String MONGO_USERNAME = dotenv.get("MONGO_USERNAME");
    private static final String MONGO_PASSWORD = dotenv.get("MONGO_PASSWORD");

    private final Datastore datastore;
    private final MongoDatabase database;
    private final MongoClient mongoClient;

    public enum connectionType {
        USERS,
        QUESTIONS
    }

    ;

    public MongoDBConnection(String host, int port, String dbName) {
        mongoClient = MongoClients.create("mongodb://" /*+ MONGO_USERNAME + ":" + MONGO_PASSWORD  + "@" */ + host + ":" + port);
        datastore = Morphia.createDatastore(mongoClient, dbName);
        database = mongoClient.getDatabase(dbName);
    }

    public MongoDBConnection(connectionType type) {
        this(MONGO_HOST, MONGO_PORT, (type == connectionType.USERS ? MONGO_USERS_DBNAME : MONGO_QUESTIONS_DBNAME));
    }

    public MongoDBConnection(String dbName) {
        this(MONGO_HOST, MONGO_PORT, dbName);
    }


    public <T> T saveEntity(T entity) {
        datastore.save(entity);
        return entity;
    }

    public <T> List<T> saveEntity(List<T> entityList) {
        datastore.save(entityList);
        return entityList;
    }

    public <T> DeleteResult deleteEntity(T entity) {
        return datastore.delete(entity);
    }

    public <T> List<DeleteResult> deleteEntity(List<T> entityList) {
        List<DeleteResult> deleteResultList = new ArrayList<>();
        for (T entity : entityList) {
            deleteResultList.add(datastore.delete(entity));
        }
        return deleteResultList;
    }


    public <T> List<T> findEntity(Class<T> typeClass) {
        try (var find = datastore.find(typeClass).iterator()) {
            return find.toList();
        } catch (Exception e) {
            LOGGER.error("An error occurred:", e);
        }
        return null;
    }

    public <T> List<T> findEntity(Class<T> typeClass, FindOptions findOptions) {
        try (var find = datastore.find(typeClass).iterator(findOptions)) {
            return find.toList();
        } catch (Exception e) {
            LOGGER.error("An error occurred:", e);
        }
        return null;
    }

    public <T> List<T> findEntityWithFilters(Class<T> typeClass, Map<String, Object> queryMap) {
        Document document = new Document();
        for (String key : queryMap.keySet()) {
            document.append(key, queryMap.get(key));
        }
        try (var find = datastore.find(typeClass, document).iterator()) {
            return find.toList();
        } catch (Exception e) {
            LOGGER.error("An error occurred:", e);
        }
        return null;
    }

    public <T> List<T> findEntityWithFilters(Class<T> typeClass, Map<String, Object> queryMap, FindOptions findOptions) {
        Document document = new Document();
        for (String key : queryMap.keySet()) {
            document.append(key, queryMap.get(key));
        }
        try (var find = datastore.find(typeClass, document).iterator(findOptions)) {
            return find.toList();
        } catch (Exception e) {
            LOGGER.error("An error occurred:", e);
        }
        return null;
    }

    public <T> T updateEntity(T entity) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("_id", datastore.getMapper().getId(entity));
        T existingEntity = null;
        if (entity instanceof User) {
            existingEntity = (T) this.findEntityWithFilters(User.class, queryMap).get(0);
        } else {
            existingEntity = (T) this.findEntityWithFilters(Question.class, queryMap).get(0);
        }
        if (existingEntity != null) {
            datastore.merge(entity);
            return entity;
        } else {
            LOGGER.error("Entity not found for update.");
            return null;
        }
    }

    public <T> Integer count(T entity) {
        Map<String, Object> queryMap = new HashMap<>();
        List<?> existingEntities = null;
        if (entity instanceof User) {
            existingEntities = this.findEntityWithFilters(User.class, queryMap);
        } else {
            existingEntities = this.findEntityWithFilters(Question.class, queryMap);
        }
        return existingEntities != null ? existingEntities.size() : 0;
    }

    public void close() {
        mongoClient.close();
        LOGGER.info("MongoDB client closed.");
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
