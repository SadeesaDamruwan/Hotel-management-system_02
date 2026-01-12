package com.hotel.management.service;

import com.hotel.management.model.Booking;

import java.util.Date;
import java.util.List;

public class BookingWorkflowExample {
    
    public static void main(String[] args) {
        GuestBookingService bookingService = new GuestBookingService();
        RoomService roomService = new RoomService();
        
        System.out.println("=== BOOKING WORKFLOW DEMONSTRATION ===\n");
        
        System.out.println("1. Viewing all CONFIRMED bookings:");
        List<Booking> confirmedBookings = bookingService.getBookingsByStatus("CONFIRMED");
        for (Booking booking : confirmedBookings) {
            System.out.println("   - " + booking.getGuestName() + " | Room: " + booking.getRoomNumber() + 
                               " | Status: " + booking.getBookingStatus());
        }
        
        if (!confirmedBookings.isEmpty()) {
            System.out.println("\n2. Checking in guest...");
            Booking firstBooking = confirmedBookings.get(0);
            String bookingId = firstBooking.getId();
            
            boolean checkedIn = bookingService.checkInGuest(bookingId);
            if (checkedIn) {
                System.out.println("   ✓ Guest checked in successfully");
                System.out.println("   → Room " + firstBooking.getRoomNumber() + " is now OCCUPIED");
            }
        }
        
        System.out.println("\n3. Viewing all CHECKED_IN bookings:");
        List<Booking> checkedInBookings = bookingService.getBookingsByStatus("CHECKED_IN");
        for (Booking booking : checkedInBookings) {
            System.out.println("   - " + booking.getGuestName() + " | Room: " + booking.getRoomNumber());
        }
        
        if (!checkedInBookings.isEmpty()) {
            System.out.println("\n4. Checking out guest...");
            Booking firstCheckedIn = checkedInBookings.get(0);
            String bookingId = firstCheckedIn.getId();
            
            boolean checkedOut = bookingService.checkOutGuest(bookingId);
            if (checkedOut) {
                System.out.println("   ✓ Guest checked out successfully");
                System.out.println("   → Room " + firstCheckedIn.getRoomNumber() + " status: NEEDS CLEANING");
                System.out.println("   ⏰ Room will automatically become AVAILABLE in 1 hour");
            }
        }
        
        System.out.println("\n=== ROOM STATUS TRANSITIONS ===");
        System.out.println("BOOKING      → Room: Available → Occupied");
        System.out.println("CHECK-IN     → Room: Occupied (confirmed)");
        System.out.println("CHECK-OUT    → Room: Needs Cleaning");
        System.out.println("AFTER 1 HOUR → Room: Available (automatic)");
        
        System.out.println("\n=== END OF DEMONSTRATION ===");
    }
}
