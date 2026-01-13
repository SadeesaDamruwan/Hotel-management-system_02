package com.hotel.management.service;

import com.hotel.management.model.Room;

import java.util.List;

public class RoomStatusFixer {
    
    public static void main(String[] args) {
        System.out.println("=== Room Status Fixer ===\n");
        
        RoomService roomService = new RoomService();
        
        List<Room> allRooms = roomService.getAllRooms();
        int fixedCount = 0;
        
        for (Room room : allRooms) {
            if ("Needs Cleaning".equals(room.getStatus())) {
                System.out.println("Found room in 'Needs Cleaning' status: " + room.getRoomNumber());
                
                boolean success = roomService.markRoomAvailable(room.getRoomNumber());
                if (success) {
                    fixedCount++;
                    System.out.println("  ✓ Room " + room.getRoomNumber() + " marked as Available\n");
                }
            }
        }
        
        if (fixedCount == 0) {
            System.out.println("No rooms found that need fixing.");
        } else {
            System.out.println("\n=== Fixed " + fixedCount + " room(s) ===");
        }
        
        System.out.println("\nYou can now restart the application.");
        System.out.println("The new monitoring system will automatically handle future checkouts.");
    }
}
