package com.hotel.management.model;

import java.util.Date;

public class GuestActivity {
    private final int id;
    private final String guestId;
    private final String type;      
    private final String description;
    private final Date timestamp;

    public GuestActivity(int id, String guestId, String type, String description, Date timestamp) {
        this.id = id;
        this.guestId = guestId;
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getGuestId() { return guestId; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public Date getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "GuestActivity{id=" + id + ", guestId=" + guestId + ", type=" + type +
                ", timestamp=" + timestamp + ", description=" + description + "}";
    }
}
