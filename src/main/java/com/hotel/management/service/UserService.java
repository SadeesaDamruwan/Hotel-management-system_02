package com.hotel.management.service;

import com.hotel.management.model.User;
import com.hotel.management.util.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UserService {
    
    private final MongoCollection<Document> usersCollection;
    
    public UserService() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.usersCollection = database.getCollection("users");
        
        try {
            usersCollection.createIndex(new Document("username", 1), 
                new com.mongodb.client.model.IndexOptions().unique(true));
        } catch (Exception e) {

        }
    }
    
    public User authenticate(String username, String password, String role) {
        try {
            Document query = new Document("username", username)
                    .append("password", password)
                    .append("role", role);
            
            Document userDoc = usersCollection.find(query).first();
            
            if (userDoc != null) {
                return User.fromDocument(userDoc);
            }
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean createUser(User user) {
        try {
            Document existingUser = usersCollection.find(new Document("username", user.getUsername())).first();
            if (existingUser != null) {
                System.err.println("✗ Username already exists: " + user.getUsername());
                return false;
            }
            
            usersCollection.insertOne(user.toDocument());
            System.out.println("✓ User created successfully: " + user.getUsername() + " (" + user.getRole() + ")");
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public User findByUsername(String username) {
        try {
            Document userDoc = usersCollection.find(new Document("username", username)).first();
            if (userDoc != null) {
                return User.fromDocument(userDoc);
            }
        } catch (Exception e) {
            System.err.println("Error finding user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updatePassword(String username, String newPassword) {
        try {
            Document query = new Document("username", username);
            Document update = new Document("$set", new Document("password", newPassword));
            
            usersCollection.updateOne(query, update);
            System.out.println("✓ Password updated for user: " + username);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error updating password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteUser(String username) {
        try {
            Document query = new Document("username", username);
            usersCollection.deleteOne(query);
            System.out.println("✓ User deleted: " + username);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean hasUsers() {
        try {
            return usersCollection.countDocuments() > 0;
        } catch (Exception e) {
            System.err.println("Error checking users: " + e.getMessage());
            return false;
        }
    }
    
    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        try {
            for (Document doc : usersCollection.find()) {
                users.add(User.fromDocument(doc));
            }
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
}
