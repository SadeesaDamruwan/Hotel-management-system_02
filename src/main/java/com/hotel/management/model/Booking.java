package com.hotel.management.model;

import java.util.Date;

public class Booking {
    
    private String id;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String guestAddress;
    private String guestNIC;

    private Date checkInDate;
    private Date checkOutDate;

    private int numberOfNights;

    private String roomType;
    private String roomNumber;

    private double pricePerNight;
    private double totalAmount;

    private String paymentType; 
    private String paymentId; 
    private String orderId;
    
    private Date bookingDate;
    private String bookingStatus; 
    private Date actualCheckOutDate; 
    
    public Booking() {
        this.bookingDate = new Date();
        this.bookingStatus = "CONFIRMED";
    }
    
    public Booking(String guestName, String guestEmail, String guestPhone, String guestAddress,
                   String guestNIC, Date checkInDate, Date checkOutDate, int numberOfNights,
                   String roomType, String roomNumber, double pricePerNight, double totalAmount,
                   String paymentType) {
        this();
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestPhone = guestPhone;
        this.guestAddress = guestAddress;
        this.guestNIC = guestNIC;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfNights = numberOfNights;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
    }
    
    public String getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getGuestEmail() { return guestEmail; }
    public String getGuestPhone() { return guestPhone; }
    public String getGuestAddress() { return guestAddress; }
    public String getGuestNIC() { return guestNIC; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public int getNumberOfNights() { return numberOfNights; }
    public String getRoomType() { return roomType; }
    public String getRoomNumber() { return roomNumber; }
    public double getPricePerNight() { return pricePerNight; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentType() { return paymentType; }
    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public Date getBookingDate() { return bookingDate; }
    public String getBookingStatus() { return bookingStatus; }
    public Date getActualCheckOutDate() { return actualCheckOutDate; }
    
    public void setId(String id) { this.id = id; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }
    public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }
    public void setGuestAddress(String guestAddress) { this.guestAddress = guestAddress; }
    public void setGuestNIC(String guestNIC) { this.guestNIC = guestNIC; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setNumberOfNights(int numberOfNights) { this.numberOfNights = numberOfNights; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public void setActualCheckOutDate(Date actualCheckOutDate) { this.actualCheckOutDate = actualCheckOutDate; }
    
    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", guestName='" + guestName + '\'' +
                ", guestEmail='" + guestEmail + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", totalAmount=" + totalAmount +
                ", paymentType='" + paymentType + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
