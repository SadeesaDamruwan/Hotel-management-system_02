package com.hotel.management.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    
    static {
        try {
            String mongoUri = EnvLoader.get("MONGODB_URI", "mongodb://localhost:27017");
            String dbName = EnvLoader.get("MONGODB_DATABASE", "hotel_management_db");
            
            mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase(dbName);
            
            System.out.println("✓ Successfully connected to MongoDB database: " + dbName);
            
        } catch (Exception e) {
            System.err.println("✗ Failed to connect to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static MongoDatabase getDatabase() {
        return database;
    }
    
    public static MongoClient getClient() {
        return mongoClient;
    }
    
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("✓ MongoDB connection closed");
        }
    }
    
    public static boolean isConnected() {
        try {
            if (database != null) {
                database.listCollectionNames().first();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Database connection check failed: " + e.getMessage());
        }
        return false;
    }
}
