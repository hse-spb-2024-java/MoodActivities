package org.hse.moodactivities.utils;

import org.hse.moodactivities.data.utils.MongoDBConnection;

public class MongoDBSingleton {
    private static MongoDBSingleton instance;
    private MongoDBConnection mongoDBConnection;

    private MongoDBSingleton() {
        this.mongoDBConnection = new MongoDBConnection();
    }

    public static synchronized MongoDBSingleton getInstance() {
        if (instance == null) {
            instance = new MongoDBSingleton();
        }
        return instance;
    }

    public MongoDBConnection getConnection() {
        return this.mongoDBConnection;
    }

    public void closeConnection() {
        if (this.mongoDBConnection != null) {
            this.mongoDBConnection.close();
            this.mongoDBConnection = null;
        }
    }
}
