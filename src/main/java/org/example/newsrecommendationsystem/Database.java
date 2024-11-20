package org.example.newsrecommendationsystem;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Database {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017/";
    private static final String DATABASE_NAME = "news_recommendation_db";
    private static MongoDatabase database;

    // Initialize the database connection
    public static void initDatabase() {
        try {
            if (database == null) {
                MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DATABASE_NAME);
                System.out.println("Connected to database: " + DATABASE_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
    }

    // Retrieve the database instance
    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("Database is not initialized. Call initDatabase() first.");
        }
        return database;
    }
}
