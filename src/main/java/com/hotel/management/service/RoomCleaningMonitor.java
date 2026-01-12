package com.hotel.management.service;

import com.hotel.management.model.Room;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RoomCleaningMonitor {
    
    private static RoomCleaningMonitor instance;
    private Timer monitorTimer;
    private RoomService roomService;
    private boolean isRunning;
    
    private static final long CHECK_INTERVAL = 10 * 60 * 1000; 
    
    private RoomCleaningMonitor() {
        this.roomService = new RoomService();
        this.isRunning = false;
    }
    
    
    public static synchronized RoomCleaningMonitor getInstance() {
        if (instance == null) {
            instance = new RoomCleaningMonitor();
        }
        return instance;
    }
    
    
    public void start() {
        if (isRunning) {
            System.out.println("⚠ Room cleaning monitor is already running");
            return;
        }
        
        monitorTimer = new Timer("RoomCleaningMonitor", true);
        
        
        System.out.println("✓ Room cleaning monitor started - processing any queued rooms...");
        checkAndUpdateRooms();
        
        
        monitorTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndUpdateRooms();
            }
        }, CHECK_INTERVAL, CHECK_INTERVAL);
        
        isRunning = true;
        System.out.println("✓ Monitoring active (checking every 10 minutes)");
    }
    
    
    public void stop() {
        if (monitorTimer != null) {
            monitorTimer.cancel();
            monitorTimer = null;
        }
        isRunning = false;
        System.out.println("✓ Room cleaning monitor stopped");
    }
    
    
    private void checkAndUpdateRooms() {
        try {
            List<Room> roomsToUpdate = roomService.getRoomsNeedingCleaningOverOneHour();
            
            if (roomsToUpdate.isEmpty()) {
                System.out.println("⏰ Room cleaning check: No rooms need status update");
                
                
                autoAssignQueuedRooms();
                return;
            }
            
            System.out.println("⏰ Found " + roomsToUpdate.size() + " room(s) ready to be marked as Available");
            
            for (Room room : roomsToUpdate) {
                boolean success = roomService.markRoomAvailable(room.getRoomNumber());
                if (success) {
                    System.out.println("  ✓ Room " + room.getRoomNumber() + " is now Available (cleaning completed)");
                    
                    
                    autoAssignQueuedRooms();
                }
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error in room cleaning monitor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    private void autoAssignQueuedRooms() {
        try {
            
            List<Room> queuedRooms = roomService.findRoomsByStatus("Needs Cleaning");
            int queuedCount = 0;
            
            for (Room room : queuedRooms) {
                if (room.getAssignedStaff() == null || 
                    room.getAssignedStaff().equals("Unassigned") || 
                    room.getAssignedStaff().isEmpty()) {
                    
                    queuedCount++;
                    
                    
                    boolean assigned = roomService.autoAssignCleaningStaff(room.getRoomNumber());
                    
                    if (assigned) {
                        System.out.println("  ✓ Queued room " + room.getRoomNumber() + " now has assigned cleaning staff");
                    }
                    
                }
            }
            
            if (queuedCount > 0) {
                System.out.println("📋 Processed queue: " + queuedCount + " room(s) waiting for cleaning staff");
            }
            
        } catch (Exception e) {
            System.err.println("Error auto-assigning queued rooms: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public void runCheckNow() {
        System.out.println("⏰ Manual room cleaning check triggered...");
        checkAndUpdateRooms();
    }
    
    
    public boolean isRunning() {
        return isRunning;
    }
}
