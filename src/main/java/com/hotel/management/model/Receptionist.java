package com.hotel.management.model;

public class Receptionist {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String shift;
    private boolean active;

    public Receptionist(int id, String name, String email, String phone, String shift) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.shift = shift;
        this.active = true;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getShift() { return shift; }
    public boolean isActive() { return active; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setShift(String shift) { this.shift = shift; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Receptionist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", shift='" + shift + '\'' +
                ", active=" + active +
                '}';
    }
}
