package com.hotel.management.model;

public class Room {
    private String id; 
    private String roomNumber;
    private String roomType; 
    private String status;
    private double price;
    private int capacity; 
    private String assignedStaff; 
    
    public Room() {
    }
    
    public Room(String roomNumber, String roomType, String status, double price, int capacity) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.assignedStaff = "Unassigned";
    }
    
    public Room(String roomNumber, String roomType, String status, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
        this.price = price;
        this.capacity = 0;
        this.assignedStaff = "Unassigned";
    }
    
    public Room(String id, String roomNumber, String roomType, String status, double price, int capacity, String assignedStaff) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.assignedStaff = assignedStaff;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public String getAssignedStaff() {
        return assignedStaff;
    }
    
    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff = assignedStaff;
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", capacity=" + capacity +
                ", assignedStaff='" + assignedStaff + '\'' +
                '}';
    }
}
