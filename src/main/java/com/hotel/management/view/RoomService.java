package com.hotel.management.service;

import com.hotel.management.model.Room;
import com.hotel.management.util.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomService {
    private final MongoCollection<Document> roomCollection;
    
    public RoomService() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.roomCollection = database.getCollection("rooms");
    }
    
    public boolean addRoom(Room room) {
        try {
            if (findRoomByNumber(room.getRoomNumber()) != null) {
                System.err.println("Room number " + room.getRoomNumber() + " already exists!");
                return false;
            }
            
            Document doc = new Document()
                    .append("roomNumber", room.getRoomNumber())
                    .append("roomType", room.getRoomType())
                    .append("status", room.getStatus())
                    .append("price", room.getPrice())
                    .append("capacity", room.getCapacity())
                    .append("assignedStaff", room.getAssignedStaff());
            
            roomCollection.insertOne(doc);
            System.out.println("✓ Room added successfully: " + room.getRoomNumber());
            return true;
        } catch (Exception e) {
            System.err.println("Error adding room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateRoom(String roomNumber, Room updatedRoom) {
        try {
            Document updateDoc = new Document()
                    .append("roomNumber", updatedRoom.getRoomNumber())
                    .append("roomType", updatedRoom.getRoomType())
                    .append("status", updatedRoom.getStatus())
                    .append("price", updatedRoom.getPrice())
                    .append("capacity", updatedRoom.getCapacity())
                    .append("assignedStaff", updatedRoom.getAssignedStaff());
            
            roomCollection.updateOne(
                    Filters.eq("roomNumber", roomNumber),
                    new Document("$set", updateDoc)
            );
            
            System.out.println("✓ Room updated successfully: " + roomNumber);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateRoomStatus(String roomNumber, String status, String assignedStaff) {
        try {
            Document updateDoc = new Document()
                    .append("status", status)
                    .append("assignedStaff", assignedStaff);
            
            roomCollection.updateOne(
                    Filters.eq("roomNumber", roomNumber),
                    new Document("$set", updateDoc)
            );
            
            System.out.println("✓ Room status updated: " + roomNumber + " -> " + status);
            
            if (assignedStaff != null && !assignedStaff.isEmpty() && 
                (status.equals("Needs Cleaning") || status.equals("Under Maintenance") || status.equals("Maintenance"))) {
                
                System.out.println("📧 Checking staff assignment for email notification...");
                System.out.println("   Staff Name: " + assignedStaff);
                System.out.println("   Task Type: " + status);
                
                Room room = findRoomByNumber(roomNumber);
                if (room != null) {
                    System.out.println("✓ Room found in database: " + roomNumber + " (" + room.getRoomType() + ")");
                    
                    StaffService staffService = new StaffService();
                    com.hotel.management.model.Staff staff = staffService.findStaffByName(assignedStaff);
                    
                    if (staff == null) {
                        System.err.println("✗ ERROR: Staff member '" + assignedStaff + "' NOT FOUND in MongoDB!");
                        System.err.println("   Please verify staff exists in the database.");
                    } else {
                        System.out.println("✓ Staff found in MongoDB:");
                        System.out.println("   - ID: " + staff.getId());
                        System.out.println("   - Name: " + staff.getName());
                        System.out.println("   - Role: " + staff.getRole());
                        System.out.println("   - Email: " + (staff.getEmail() != null ? staff.getEmail() : "NOT SET"));
                        System.out.println("   - Phone: " + staff.getPhone());
                        
                        if (staff.getEmail() == null || staff.getEmail().isEmpty()) {
                            System.err.println("✗ ERROR: Staff member has NO EMAIL ADDRESS!");
                            System.err.println("   Cannot send notification. Please update staff profile with email.");
                        } else {
                            String taskType = status.equals("Needs Cleaning") ? "Room Cleaning" : "Room Maintenance";
                            String priority = "MEDIUM"; 
                            
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a");
                            String assignedTime = sdf.format(new Date());
                            
                            System.out.println("📧 Sending work assignment email to: " + staff.getEmail());
                            
                            final String staffEmail = staff.getEmail();
                            final String staffName = staff.getName();
                            final String roomType = room.getRoomType();
                            
                            new Thread(() -> {
                                boolean emailSent = EmailService.sendWorkAssignmentEmail(
                                    staffEmail,
                                    staffName,
                                    taskType,
                                    roomNumber,
                                    roomType,
                                    priority,
                                    assignedTime
                                );
                                
                                if (emailSent) {
                                    System.out.println("✓ SUCCESS: Email notification sent to " + staffEmail);
                                    System.out.println("   Task: " + taskType + " for Room " + roomNumber);
                                } else {
                                    System.err.println("✗ FAILED: Could not send email to " + staffEmail);
                                    System.err.println("   Check email configuration in .env file");
                                }
                            }).start();
                        }
                    }
                } else {
                    System.err.println("✗ ERROR: Room " + roomNumber + " NOT FOUND in database!");
                }
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error updating room status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateRoomStatus(String roomNumber, String status) {
        try {
            Document updateDoc = new Document("status", status);
            
            roomCollection.updateOne(
                    Filters.eq("roomNumber", roomNumber),
                    new Document("$set", updateDoc)
            );
            
            System.out.println("✓ Room status updated: " + roomNumber + " -> " + status);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating room status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean markRoomNeedsCleaning(String roomNumber) {
        try {
            roomCollection.updateOne(
                    Filters.eq("roomNumber", roomNumber),
                    Updates.combine(
                            Updates.set("status", "Needs Cleaning"),
                            Updates.set("cleaningStartTime", new Date())
                    )
            );
            
            System.out.println("✓ Room " + roomNumber + " marked as 'Needs Cleaning' with timestamp");
            return true;
        } catch (Exception e) {
            System.err.println("Error marking room for cleaning: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Room> getRoomsNeedingCleaningOverOneHour() {
        List<Room> rooms = new ArrayList<>();
        try {
            Date oneHourAgo = new Date(System.currentTimeMillis() - (60 * 60 * 1000));
            
            roomCollection.find(
                    Filters.and(
                            Filters.eq("status", "Needs Cleaning"),
                            Filters.lt("cleaningStartTime", oneHourAgo)
                    )
            ).forEach(doc -> rooms.add(documentToRoom(doc)));
            
        } catch (Exception e) {
            System.err.println("Error finding rooms needing cleaning: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    public boolean markRoomAvailable(String roomNumber) {
        try {
            roomCollection.updateOne(
                    Filters.eq("roomNumber", roomNumber),
                    Updates.combine(
                            Updates.set("status", "Available"),
                            Updates.set("assignedStaff", "Unassigned"),
                            Updates.unset("cleaningStartTime")
                    )
            );
            
            System.out.println("✓ Room " + roomNumber + " is now Available");
            return true;
        } catch (Exception e) {
            System.err.println("Error marking room as available: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteRoom(String roomNumber) {
        try {
            roomCollection.deleteOne(Filters.eq("roomNumber", roomNumber));
            System.out.println("✓ Room deleted: " + roomNumber);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        try {
            for (Document doc : roomCollection.find()) {
                rooms.add(documentToRoom(doc));
            }
        } catch (Exception e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    public Room findRoomByNumber(String roomNumber) {
        try {
            Document doc = roomCollection.find(Filters.eq("roomNumber", roomNumber)).first();
            if (doc != null) {
                return documentToRoom(doc);
            }
        } catch (Exception e) {
            System.err.println("Error finding room: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Room> findRoomsByStatus(String status) {
        List<Room> rooms = new ArrayList<>();
        try {
            for (Document doc : roomCollection.find(Filters.eq("status", status))) {
                rooms.add(documentToRoom(doc));
            }
        } catch (Exception e) {
            System.err.println("Error finding rooms by status: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    public List<Room> findRoomsByType(String roomType) {
        List<Room> rooms = new ArrayList<>();
        try {
            for (Document doc : roomCollection.find(Filters.eq("roomType", roomType))) {
                rooms.add(documentToRoom(doc));
            }
        } catch (Exception e) {
            System.err.println("Error finding rooms by type: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }
    
    public long getRoomCount() {
        try {
            return roomCollection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting rooms: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    public long getAvailableRoomCount() {
        try {
            return roomCollection.countDocuments(Filters.eq("status", "Available"));
        } catch (Exception e) {
            System.err.println("Error counting available rooms: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    public List<String> getCurrentlyAssignedStaff() {
        List<String> assignedStaff = new ArrayList<>();
        try {
            for (Document doc : roomCollection.find(
                    Filters.or(
                        Filters.eq("status", "Needs Cleaning"),
                        Filters.eq("status", "Maintenance")
                    ))) {
                String staffName = doc.getString("assignedStaff");
                if (staffName != null && !staffName.isEmpty() && !staffName.equals("Unassigned")) {
                    assignedStaff.add(staffName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting assigned staff: " + e.getMessage());
            e.printStackTrace();
        }
        return assignedStaff;
    }
    
    public boolean autoAssignCleaningStaff(String roomNumber) {
        try {
            StaffService staffService = new StaffService();
            List<com.hotel.management.model.Staff> availableStaff = staffService.getAvailableCleaningStaff();
            
            if (availableStaff.isEmpty()) {
                System.out.println("⚠ No cleaning staff available for room " + roomNumber + " - room will remain in queue");
                return false;
            }
            
            com.hotel.management.model.Staff assignedStaff = availableStaff.get(0);
            
            roomCollection.updateOne(
                    Filters.eq("roomNumber", roomNumber),
                    Updates.combine(
                            Updates.set("status", "Needs Cleaning"),
                            Updates.set("assignedStaff", assignedStaff.getName()),
                            Updates.set("cleaningStartTime", new Date())
                    )
            );
            
            System.out.println("✓ Cleaning staff " + assignedStaff.getName() + " auto-assigned to room " + roomNumber);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error auto-assigning cleaning staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private Room documentToRoom(Document doc) {
        String id = doc.getObjectId("_id").toString();
        String roomNumber = doc.getString("roomNumber");
        String roomType = doc.getString("roomType");
        String status = doc.getString("status");
        double price = doc.getDouble("price");
        int capacity = doc.getInteger("capacity");
        String assignedStaff = doc.getString("assignedStaff");
        
        return new Room(id, roomNumber, roomType, status, price, capacity, assignedStaff);
    }
}
