package com.hotel.management.service;

import com.hotel.management.model.Staff;
import com.hotel.management.util.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class StaffService {
    
    private final MongoCollection<Document> staffCollection;
    
    public StaffService() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.staffCollection = database.getCollection("staff");
    }
    
    public boolean addStaff(Staff staff) {
        try {
            if (findStaffById(staff.getId()) != null) {
                System.err.println("Staff with ID " + staff.getId() + " already exists");
                return false;
            }
            
            Document staffDoc = new Document()
                    .append("staffId", staff.getId())
                    .append("name", staff.getName())
                    .append("dob", staff.getDob())
                    .append("nic", staff.getNic())
                    .append("title", staff.getTitle())
                    .append("role", staff.getRole())
                    .append("email", staff.getEmail())
                    .append("phone", staff.getPhone())
                    .append("joinDate", staff.getJoinDate());
            
            staffCollection.insertOne(staffDoc);
            System.out.println("✓ Staff added successfully: " + staff.getName());
            return true;
            
        } catch (Exception e) {
            System.err.println("✗ Error adding staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteStaff(String staffId) {
        try {
            Document query = new Document("staffId", staffId);
            long deletedCount = staffCollection.deleteOne(query).getDeletedCount();
            
            if (deletedCount > 0) {
                System.out.println("✓ Staff deleted successfully: " + staffId);
                return true;
            } else {
                System.err.println("✗ No staff found with ID: " + staffId);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error deleting staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        
        try {
            for (Document doc : staffCollection.find()) {
                Staff staff = documentToStaff(doc);
                staffList.add(staff);
            }
            System.out.println("✓ Retrieved " + staffList.size() + " staff members");
            
        } catch (Exception e) {
            System.err.println("✗ Error retrieving staff: " + e.getMessage());
            e.printStackTrace();
        }
        
        return staffList;
    }
    
    public Staff findStaffById(String staffId) {
        try {
            Document query = new Document("staffId", staffId);
            Document doc = staffCollection.find(query).first();
            
            if (doc != null) {
                return documentToStaff(doc);
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error finding staff: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Staff> findStaffByRole(String role) {
        List<Staff> staffList = new ArrayList<>();
        
        try {
            Document query = new Document("role", role);
            for (Document doc : staffCollection.find(query)) {
                Staff staff = documentToStaff(doc);
                staffList.add(staff);
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error finding staff by role: " + e.getMessage());
            e.printStackTrace();
        }
        
        return staffList;
    }
    
    public List<Staff> getAvailableCleaningStaff() {
        List<Staff> cleaningStaff = findStaffByRole("Cleaning Staff");
        
        RoomService roomService = new RoomService();
        List<String> assignedStaffNames = roomService.getCurrentlyAssignedStaff();
        
        List<Staff> availableStaff = new ArrayList<>();
        for (Staff staff : cleaningStaff) {
            if (!assignedStaffNames.contains(staff.getName())) {
                availableStaff.add(staff);
            }
        }
        
        return availableStaff;
    }
    
    public Staff findStaffByNic(String nic) {
        try {
            Document query = new Document("nic", nic);
            Document doc = staffCollection.find(query).first();
            
            if (doc != null) {
                return documentToStaff(doc);
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error finding staff by NIC: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public Staff findStaffByName(String name) {
        try {
            Document query = new Document("name", name);
            Document doc = staffCollection.find(query).first();
            
            if (doc != null) {
                return documentToStaff(doc);
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error finding staff by name: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateStaff(Staff staff) {
        try {
            Document query = new Document("staffId", staff.getId());
            Document update = new Document("$set", new Document()
                    .append("name", staff.getName())
                    .append("dob", staff.getDob())
                    .append("nic", staff.getNic())
                    .append("title", staff.getTitle())
                    .append("role", staff.getRole())
                    .append("email", staff.getEmail())
                    .append("phone", staff.getPhone())
                    .append("joinDate", staff.getJoinDate()));
            
            long modifiedCount = staffCollection.updateOne(query, update).getModifiedCount();
            
            if (modifiedCount > 0) {
                System.out.println("✓ Staff updated successfully: " + staff.getId());
                return true;
            } else {
                System.err.println("✗ No staff found with ID: " + staff.getId());
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error updating staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public long getStaffCount() {
        try {
            return staffCollection.countDocuments();
        } catch (Exception e) {
            System.err.println("✗ Error counting staff: " + e.getMessage());
            return 0;
        }
    }
    
    private Staff documentToStaff(Document doc) {
        Staff staff = new Staff();
        staff.setId(doc.getString("staffId"));
        staff.setName(doc.getString("name"));
        staff.setDob(doc.getString("dob"));
        staff.setNic(doc.getString("nic"));
        staff.setTitle(doc.getString("title"));
        staff.setRole(doc.getString("role"));
        staff.setEmail(doc.getString("email"));
        staff.setPhone(doc.getString("phone"));
        staff.setJoinDate(doc.getString("joinDate"));
        return staff;
    }
}
