package com.hotel.management.model;
import java.util.Date;


public class Staff {
    private String id;
    private String name;
    private String dob; 
    private String nic; 
    private String title; 
    private String role; 
    private String email;
    private String phone;
    private String joinDate; 

    public Staff() {
    }

    public Staff(String id, String name, String dob, String nic, String title, 
                 String role, String email, String phone, String joinDate) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.nic = nic;
        this.title = title;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.joinDate = joinDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getNic() {
        return nic;
    }

    public String getTitle() {
        return title;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", nic='" + nic + '\'' +
                ", title='" + title + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", joinDate='" + joinDate + '\'' +
                '}';
    }
}
