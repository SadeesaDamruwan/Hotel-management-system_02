package com.hotel.management.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Guest {

    private int id;
    private String name;
    private String roomNumber;
    private Date checkIn;
    private Date checkOut;
    private List<String> activities;

    public Guest(int id, String name, String roomNumber, Date checkIn) {
        this.id = id;
        this.name = name;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.activities = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRoomNumber() { return roomNumber; }
    public Date getCheckIn() { return checkIn; }
    public Date getCheckOut() { return checkOut; }
    public List<String> getActivities() { return activities; }

    public void setName(String name) { this.name = name; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }

    public void addActivity(String activity) {
        activities.add(activity);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", activities=" + activities +
                '}';
    }
}
