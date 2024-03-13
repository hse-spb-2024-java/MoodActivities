package org.hse.moodactivities.data.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hse.moodactivities.data.entities.mongodb.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBConnection implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBConnection.class);

    private static final String HOST = "mongodb";
    private static final int PORT = 27017;
    private static final String DB_NAME = "user-data";
    private final Datastore datastore;
    private final MongoDatabase database;
    private final MongoClient mongoClient;

    public MongoDBConnection(String host, int port, String dbName) {
        mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        datastore = Morphia.createDatastore(mongoClient, dbName);
        database = mongoClient.getDatabase(dbName);
    }

    public MongoDBConnection() {
        this(HOST, PORT, DB_NAME);
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
        User existingEntity = this.findEntityWithFilters(User.class, queryMap).get(0);
        if (existingEntity != null) {
            datastore.merge(entity);
            return entity;
        } else {
            LOGGER.error("Entity not found for update.");
            return null;
        }
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
