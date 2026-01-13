package com.hotel.management.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class RoomStatusScheduler {
    
    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000; 
    private static RoomStatusScheduler instance;
    private final Timer timer;
    private final Map<String, TimerTask> scheduledTasks;
    private final RoomService roomService;
    
    private RoomStatusScheduler() {
        this.timer = new Timer("RoomStatusScheduler", true);
        this.scheduledTasks = new ConcurrentHashMap<>();
        this.roomService = new RoomService();
    }
    
    public static synchronized RoomStatusScheduler getInstance() {
        if (instance == null) {
            instance = new RoomStatusScheduler();
        }
        return instance;
    }
    
    public void scheduleCleaningToAvailable(String roomNumber) {
        cancelScheduledTask(roomNumber);
        
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    boolean success = roomService.updateRoomStatus(roomNumber, "Available");
                    if (success) {
                        System.out.println("✓ Room " + roomNumber + " is now Available (cleaning completed)");
                    }
                    scheduledTasks.remove(roomNumber);
                } catch (Exception e) {
                    System.err.println("Error updating room status: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        
        timer.schedule(task, ONE_HOUR_IN_MILLIS);
        scheduledTasks.put(roomNumber, task);
        
        System.out.println("⏰ Scheduled room " + roomNumber + " to become Available in 1 hour");
    }
    
    public void cancelScheduledTask(String roomNumber) {
        TimerTask existingTask = scheduledTasks.get(roomNumber);
        if (existingTask != null) {
            existingTask.cancel();
            scheduledTasks.remove(roomNumber);
            System.out.println("⏰ Cancelled scheduled task for room " + roomNumber);
        }
    }
    
    public void markRoomNeedsCleaning(String roomNumber) {
        boolean success = roomService.updateRoomStatus(roomNumber, "Needs Cleaning");
        if (success) {
            scheduleCleaningToAvailable(roomNumber);
        }
    }
    
    public void markRoomAvailable(String roomNumber) {
        cancelScheduledTask(roomNumber);
        roomService.updateRoomStatus(roomNumber, "Available");
    }
    
    public void shutdown() {
        timer.cancel();
        scheduledTasks.clear();
        System.out.println("✓ RoomStatusScheduler shutdown complete");
    }
}
