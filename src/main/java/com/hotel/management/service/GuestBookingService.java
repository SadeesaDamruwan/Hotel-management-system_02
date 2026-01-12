package com.hotel.management.service;

import com.hotel.management.model.Booking;
import com.hotel.management.util.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuestBookingService {
    
    private final MongoCollection<Document> bookingCollection;
    
    public GuestBookingService() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.bookingCollection = database.getCollection("guest_bookings");
    }
    
    public String saveBooking(Booking booking) {
        try {
            Document doc = new Document()
                    .append("guestName", booking.getGuestName())
                    .append("guestEmail", booking.getGuestEmail())
                    .append("guestPhone", booking.getGuestPhone())
                    .append("guestAddress", booking.getGuestAddress())
                    .append("guestNIC", booking.getGuestNIC())
                    .append("checkInDate", booking.getCheckInDate())
                    .append("checkOutDate", booking.getCheckOutDate())
                    .append("numberOfNights", booking.getNumberOfNights())
                    .append("roomType", booking.getRoomType())
                    .append("roomNumber", booking.getRoomNumber())
                    .append("pricePerNight", booking.getPricePerNight())
                    .append("totalAmount", booking.getTotalAmount())
                    .append("paymentType", booking.getPaymentType())
                    .append("bookingDate", booking.getBookingDate())
                    .append("bookingStatus", booking.getBookingStatus());
            
            if (booking.getPaymentId() != null && !booking.getPaymentId().isEmpty()) {
                doc.append("paymentId", booking.getPaymentId());
            }
            if (booking.getOrderId() != null && !booking.getOrderId().isEmpty()) {
                doc.append("orderId", booking.getOrderId());
            }
            
            bookingCollection.insertOne(doc);
            String insertedId = doc.getObjectId("_id").toString();
            
            System.out.println("✓ Booking saved successfully with ID: " + insertedId);
            return insertedId;
            
        } catch (Exception e) {
            System.err.println("✗ Error saving booking: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public Booking findBookingById(String bookingId) {
        try {
            Document doc = bookingCollection.find(Filters.eq("_id", new ObjectId(bookingId))).first();
            if (doc != null) {
                return documentToBooking(doc);
            }
        } catch (Exception e) {
            System.err.println("Error finding booking: " + e.getMessage());
        }
        return null;
    }
    
    public List<Booking> findBookingsByEmail(String guestEmail) {
        List<Booking> bookings = new ArrayList<>();
        try {
            bookingCollection.find(Filters.eq("guestEmail", guestEmail))
                    .forEach(doc -> bookings.add(documentToBooking(doc)));
        } catch (Exception e) {
            System.err.println("Error finding bookings: " + e.getMessage());
        }
        return bookings;
    }

    public List<Booking> findBookingsByRoom(String roomNumber) {
        List<Booking> bookings = new ArrayList<>();
        try {
            bookingCollection.find(Filters.eq("roomNumber", roomNumber))
                    .forEach(doc -> bookings.add(documentToBooking(doc)));
        } catch (Exception e) {
            System.err.println("Error finding bookings: " + e.getMessage());
        }
        return bookings;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        try {
            bookingCollection.find().forEach(doc -> bookings.add(documentToBooking(doc)));
        } catch (Exception e) {
            System.err.println("Error retrieving bookings: " + e.getMessage());
        }
        return bookings;
    }

    public boolean updateBookingStatus(String bookingId, String newStatus) {
        try {
            bookingCollection.updateOne(
                    Filters.eq("_id", new ObjectId(bookingId)),
                    Updates.set("bookingStatus", newStatus)
            );
            System.out.println("✓ Booking status updated to: " + newStatus);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error updating booking status: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteBooking(String bookingId) {
        try {
            bookingCollection.deleteOne(Filters.eq("_id", new ObjectId(bookingId)));
            System.out.println("✓ Booking deleted successfully");
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error deleting booking: " + e.getMessage());
            return false;
        }
    }

    private Booking documentToBooking(Document doc) {
        Booking booking = new Booking();
        booking.setId(doc.getObjectId("_id").toString());
        booking.setGuestName(doc.getString("guestName"));
        booking.setGuestEmail(doc.getString("guestEmail"));
        booking.setGuestPhone(doc.getString("guestPhone"));
        booking.setGuestAddress(doc.getString("guestAddress"));
        booking.setGuestNIC(doc.getString("guestNIC"));
        booking.setCheckInDate(doc.getDate("checkInDate"));
        booking.setCheckOutDate(doc.getDate("checkOutDate"));
        booking.setNumberOfNights(doc.getInteger("numberOfNights"));
        booking.setRoomType(doc.getString("roomType"));
        booking.setRoomNumber(doc.getString("roomNumber"));
        booking.setPricePerNight(doc.getDouble("pricePerNight"));
        booking.setTotalAmount(doc.getDouble("totalAmount"));
        booking.setPaymentType(doc.getString("paymentType"));
        booking.setPaymentId(doc.getString("paymentId"));
        booking.setOrderId(doc.getString("orderId"));
        booking.setBookingDate(doc.getDate("bookingDate"));
        booking.setBookingStatus(doc.getString("bookingStatus"));
        booking.setActualCheckOutDate(doc.getDate("actualCheckOutDate"));
        return booking;
    }
    
    public List<Booking> getBookingsByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        try {
            bookingCollection.find(Filters.eq("bookingStatus", status))
                    .forEach(doc -> bookings.add(documentToBooking(doc)));
        } catch (Exception e) {
            System.err.println("Error finding bookings by status: " + e.getMessage());
        }
        return bookings;
    }
    
    public boolean checkInGuest(String bookingId) {
        try {
            Booking booking = findBookingById(bookingId);
            if (booking == null) {
                System.err.println("✗ Booking not found: " + bookingId);
                return false;
            }
            
            boolean bookingUpdated = updateBookingStatus(bookingId, "CHECKED_IN");
        
            RoomService roomService = new RoomService();
            boolean roomUpdated = roomService.updateRoomStatus(booking.getRoomNumber(), "Occupied");
            
            if (bookingUpdated && roomUpdated) {
                System.out.println("✓ Guest checked in - Room " + booking.getRoomNumber() + " is now Occupied");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("✗ Error checking in guest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean checkOutGuest(String bookingId) {
        try {
            Booking booking = findBookingById(bookingId);
            if (booking == null) {
                System.err.println("✗ Booking not found: " + bookingId);
                return false;
            }
            
            Date now = new Date();
            bookingCollection.updateOne(
                    Filters.eq("_id", new ObjectId(bookingId)),
                    Updates.combine(
                            Updates.set("bookingStatus", "CHECKED_OUT"),
                            Updates.set("actualCheckOutDate", now)
                    )
            );
            
            RoomService roomService = new RoomService();
            boolean roomUpdated = roomService.markRoomNeedsCleaning(booking.getRoomNumber());
            
            if (roomUpdated) {
                System.out.println("✓ Guest checked out - Room " + booking.getRoomNumber() + " needs cleaning");
                
                boolean staffAssigned = roomService.autoAssignCleaningStaff(booking.getRoomNumber());
                
                if (staffAssigned) {
                    System.out.println("✓ Cleaning staff automatically assigned");
                    System.out.println("⏰ Room will be Available after staff completes cleaning (1 hour)");
                } else {
                    System.out.println("⚠ No cleaning staff available - room added to queue");
                    System.out.println("⏰ Staff will be auto-assigned when available");
                }
                
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("✗ Error checking out guest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
