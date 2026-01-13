package com.hotel.management.util;

import com.hotel.management.model.User;
import com.hotel.management.service.UserService;

import java.util.Scanner;


public class UserCreationScript {
    
    public static void main(String[] args) {
        System.out.println("║      Hotel Management System - User Creation Script        ║");
        
        if (!DatabaseConnection.isConnected()) {
            System.err.println("✗ Failed to connect to MongoDB. Please check your .env configuration.");
            System.err.println("  Make sure MongoDB is running and the connection string is correct.");
            return;
        }
        
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);
        
        boolean continueCreating = true;
        
        while (continueCreating) {
            System.out.println("Create New User");
            
            System.out.println("\nSelect Role:");
            System.out.println("1. Admin");
            System.out.println("2. Receptionist");
            System.out.print("Enter choice (1 or 2): ");
            
            String roleChoice = scanner.nextLine().trim();
            String role;
            
            if (roleChoice.equals("1")) {
                role = "admin";
            } else if (roleChoice.equals("2")) {
                role = "receptionist";
            } else {
                System.err.println("✗ Invalid choice. Please enter 1 or 2.");
                continue;
            }
            
            System.out.print("\nEnter username: ");
            String username = scanner.nextLine().trim();
            
            if (username.isEmpty()) {
                System.err.println("✗ Username cannot be empty.");
                continue;
            }
            
            if (userService.findByUsername(username) != null) {
                System.err.println("✗ Username '" + username + "' already exists. Please choose a different username.");
                continue;
            }
            
            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();
            
            if (password.isEmpty()) {
                System.err.println("✗ Password cannot be empty.");
                continue;
            }
            
            System.out.print("Enter full name (optional, press Enter to skip): ");
            String fullName = scanner.nextLine().trim();
            if (fullName.isEmpty()) {
                fullName = null;
            }
            
            System.out.print("Enter email (optional, press Enter to skip): ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                email = null;
            }
            
            User newUser = new User(username, password, role, fullName, email);
            
            System.out.println("Creating user with following details:");
            System.out.println("  Username : " + username);
            System.out.println("  Role     : " + role);
            System.out.println("  Full Name: " + (fullName != null ? fullName : "(not provided)"));
            System.out.println("  Email    : " + (email != null ? email : "(not provided)"));
            
            boolean success = userService.createUser(newUser);
            
            if (success) {
                System.out.println("\n✓ User created successfully!");
                System.out.println("  You can now login with:");
                System.out.println("    Username: " + username);
                System.out.println("    Role    : " + role);
            } else {
                System.err.println("\n✗ Failed to create user. Please try again.");
            }
            
            System.out.print("\nDo you want to create another user? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            
            if (!response.equals("y") && !response.equals("yes")) {
                continueCreating = false;
            }
        }

        System.out.println("User creation script completed.");

        
        scanner.close();
        DatabaseConnection.close();
    }
    
    public static void createDefaultUsers() {
        System.out.println("Creating default users...\n");
        
        if (!DatabaseConnection.isConnected()) {
            System.err.println("✗ Failed to connect to MongoDB.");
            return;
        }
        
        UserService userService = new UserService();
        
        User admin = new User("admin", "admin123", "admin", "System Administrator", "admin@hotel.com");
        userService.createUser(admin);
        
        User receptionist = new User("receptionist", "recept123", "receptionist", "Front Desk", "reception@hotel.com");
        userService.createUser(receptionist);
        
        System.out.println("\n✓ Default users created successfully!");
        System.out.println("  Admin        - username: admin, password: admin123");
        System.out.println("  Receptionist - username: receptionist, password: recept123");
        
        DatabaseConnection.close();
    }
}
