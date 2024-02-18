package org.hse.moodactivities.backend;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MongoDBConnection {
    private static final String DATABASE_NAME = "user-data";
    private static final String MONGO_HOST = "localhost";
    private static final int MONGO_PORT = 27017;

    private static MongoClient mongoClient;
    private static Datastore datastore;

    static {
        mongoClient = MongoClients.create("mongodb://" + MONGO_HOST + ":" + MONGO_PORT);
        datastore = Morphia.createDatastore(mongoClient, DATABASE_NAME);
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void close() {
        mongoClient.close();
    }
}
