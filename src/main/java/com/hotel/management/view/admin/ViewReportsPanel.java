package com.hotel.management.view.admin;

import com.hotel.management.model.Booking;
import com.hotel.management.model.Room;
import com.hotel.management.model.Staff;
import com.hotel.management.service.GuestBookingService;
import com.hotel.management.service.RoomService;
import com.hotel.management.service.StaffService;
import com.hotel.management.service.UserService;
import com.hotel.management.util.ReportPDFGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class ViewReportsPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color DARK_BG = new Color(25, 25, 28, 240);
    private final Color TEXT_COLOR = Color.WHITE;
    
    private JComboBox<String> reportTypeCombo;
    private JComboBox<String> dateRangeCombo;
    private JPanel reportContentPanel;
    private CardLayout reportCardLayout;
    
    private GuestBookingService bookingService;
    private RoomService roomService;
    private StaffService staffService;
    private UserService userService;

    public ViewReportsPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        bookingService = new GuestBookingService();
        roomService = new RoomService();
        staffService = new StaffService();
        userService = new UserService();

        add(createControlsPanel(), BorderLayout.NORTH);
        
        reportCardLayout = new CardLayout();
        reportContentPanel = new JPanel(reportCardLayout);
        reportContentPanel.setOpaque(false);
        
        reportContentPanel.add(createDailyReportPanel(), "Daily");
        reportContentPanel.add(createFinancialReportPanel(), "Financial");
        reportContentPanel.add(createOccupancyReportPanel(), "Occupancy");
        reportContentPanel.add(createGuestReportPanel(), "Guest");
        reportContentPanel.add(createStaffReportPanel(), "Staff");
        
        add(reportContentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Reports & Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(GOLD_COLOR);
        panel.add(titleLabel, BorderLayout.WEST);
        
        JPanel controlsRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        controlsRight.setOpaque(false);
        
        JLabel reportLabel = new JLabel("Report Type:");
        reportLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportLabel.setForeground(TEXT_COLOR);
        controlsRight.add(reportLabel);
        
        String[] reportTypes = {"Daily Operations", "Financial Report", "Occupancy Report", "Guest Analytics", "Staff Performance"};
        reportTypeCombo = new JComboBox<>(reportTypes);
        reportTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportTypeCombo.setPreferredSize(new Dimension(180, 35));
        styleComboBox(reportTypeCombo);
        reportTypeCombo.addActionListener(e -> {
            String selected = (String) reportTypeCombo.getSelectedItem();
            if (selected != null) {
                if (selected.contains("Daily")) reportCardLayout.show(reportContentPanel, "Daily");
                else if (selected.contains("Financial")) reportCardLayout.show(reportContentPanel, "Financial");
                else if (selected.contains("Occupancy")) reportCardLayout.show(reportContentPanel, "Occupancy");
                else if (selected.contains("Guest")) reportCardLayout.show(reportContentPanel, "Guest");
                else if (selected.contains("Staff")) reportCardLayout.show(reportContentPanel, "Staff");
            }
        });
        controlsRight.add(reportTypeCombo);
        
        JLabel dateLabel = new JLabel("Period:");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(TEXT_COLOR);
        controlsRight.add(dateLabel);
        
        String[] dateRanges = {"Today", "This Week", "This Month", "Last 30 Days", "This Year"};
        dateRangeCombo = new JComboBox<>(dateRanges);
        dateRangeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateRangeCombo.setPreferredSize(new Dimension(140, 35));
        styleComboBox(dateRangeCombo);
        dateRangeCombo.addActionListener(e -> refreshCurrentReport());
        controlsRight.add(dateRangeCombo);
        
        JButton exportBtn = createStyledButton("Export PDF");
        exportBtn.addActionListener(e -> exportCurrentReport());
        controlsRight.add(exportBtn);
        
        panel.add(controlsRight, BorderLayout.EAST);
        
        return panel;
    }
    
    private void refreshCurrentReport() {
        reportContentPanel.removeAll();
        reportContentPanel.add(createDailyReportPanel(), "Daily");
        reportContentPanel.add(createFinancialReportPanel(), "Financial");
        reportContentPanel.add(createOccupancyReportPanel(), "Occupancy");
        reportContentPanel.add(createGuestReportPanel(), "Guest");
        reportContentPanel.add(createStaffReportPanel(), "Staff");
        
        String selected = (String) reportTypeCombo.getSelectedItem();
        if (selected != null) {
            if (selected.contains("Daily")) reportCardLayout.show(reportContentPanel, "Daily");
            else if (selected.contains("Financial")) reportCardLayout.show(reportContentPanel, "Financial");
            else if (selected.contains("Occupancy")) reportCardLayout.show(reportContentPanel, "Occupancy");
            else if (selected.contains("Guest")) reportCardLayout.show(reportContentPanel, "Guest");
            else if (selected.contains("Staff")) reportCardLayout.show(reportContentPanel, "Staff");
        }
        
        reportContentPanel.revalidate();
        reportContentPanel.repaint();
    }
    
    private void exportCurrentReport() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate now = LocalDate.now();
            LocalDate monthStart = now.withDayOfMonth(1);
            
            List<Booking> allBookings = bookingService.getAllBookings();
            List<Room> allRooms = roomService.getAllRooms();
            List<Staff> allStaff = staffService.getAllStaff();
            List<com.hotel.management.model.User> allUsers = userService.getAllUsers();
            
            long checkInsToday = allBookings.stream()
                .filter(b -> b.getCheckInDate() != null &&
                    b.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(today) &&
                    "CONFIRMED".equals(b.getBookingStatus()))
                .count();
                
            long checkOutsToday = allBookings.stream()
                .filter(b -> b.getActualCheckOutDate() != null &&
                    b.getActualCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(today))
                .count();
                
            long occupiedRooms = allRooms.stream().filter(r -> "Occupied".equals(r.getStatus())).count();
            double dailyOccupancyRate = (occupiedRooms * 100.0) / allRooms.size();
            
            double dailyRevenue = allBookings.stream()
                .filter(b -> b.getBookingDate() != null && b.getTotalAmount() > 0)
                .filter(b -> {
                    LocalDate bookingDate = b.getBookingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return bookingDate.equals(today);
                })
                .mapToDouble(Booking::getTotalAmount)
                .sum();
                
            long needsCleaning = allRooms.stream().filter(r -> "Needs Cleaning".equals(r.getStatus())).count();
            long underMaintenance = allRooms.stream().filter(r -> "Under Maintenance".equals(r.getStatus())).count();
            
            double monthlyRevenue = allBookings.stream()
                .filter(b -> b.getBookingDate() != null &&
                    !b.getBookingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(monthStart) &&
                    b.getTotalAmount() > 0)
                .mapToDouble(Booking::getTotalAmount)
                .sum();
                
            double cashRevenue = allBookings.stream()
                .filter(b -> "CASH".equals(b.getPaymentType()) && b.getTotalAmount() > 0)
                .mapToDouble(Booking::getTotalAmount)
                .sum();
                
            double cardRevenue = allBookings.stream()
                .filter(b -> "CARD".equals(b.getPaymentType()) && b.getTotalAmount() > 0)
                .mapToDouble(Booking::getTotalAmount)
                .sum();
                
            long totalBookings = allBookings.stream()
                .filter(b -> !b.getBookingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(monthStart))
                .count();
                
            long cashCount = allBookings.stream().filter(b -> "CASH".equals(b.getPaymentType())).count();
            long cardCount = allBookings.stream().filter(b -> "CARD".equals(b.getPaymentType())).count();
            
            long totalRooms = allRooms.size();
            long availableRooms = allRooms.stream().filter(r -> "Available".equals(r.getStatus())).count();
            double occupancyRate = (occupiedRooms * 100.0) / totalRooms;
            
            double avgStayDuration = allBookings.stream()
                .filter(b -> "CHECKED_OUT".equals(b.getBookingStatus()))
                .mapToInt(Booking::getNumberOfNights)
                .average()
                .orElse(0.0);
            
            long totalGuestsThisMonth = allBookings.stream()
                .filter(b -> b.getBookingDate() != null &&
                    !b.getBookingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(monthStart))
                .count();
                
            long currentGuests = allBookings.stream()
                .filter(b -> {
                    if ("CHECKED_IN".equals(b.getBookingStatus())) {
                        return true;
                    }
                    if ("CONFIRMED".equals(b.getBookingStatus()) && b.getCheckInDate() != null) {
                        LocalDate checkInDate = b.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return !checkInDate.isAfter(today);
                    }
                    return false;
                })
                .count();
                
            long completedBookings = allBookings.stream()
                .filter(b -> "CHECKED_OUT".equals(b.getBookingStatus()))
                .count();
                
            long confirmedBookings = allBookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getBookingStatus()))
                .count();
            
            int totalStaff = allStaff.size() + allUsers.size();
            int cleaningStaff = (int) allStaff.stream().filter(s -> "Cleaning Staff".equals(s.getRole())).count();
            int maintenanceStaff = (int) allStaff.stream().filter(s -> "Maintenance Staff".equals(s.getRole())).count();
            
            long roomsBeingCleaned = allRooms.stream()
                .filter(r -> "Needs Cleaning".equals(r.getStatus()) && r.getAssignedStaff() != null && !r.getAssignedStaff().isEmpty())
                .count();
                
            long roomsUnderMaintenance = allRooms.stream()
                .filter(r -> "Under Maintenance".equals(r.getStatus()))
                .count();
                
            long admins = allUsers.stream().filter(u -> "admin".equalsIgnoreCase(u.getRole())).count();
            long receptionists = allUsers.stream().filter(u -> "receptionist".equalsIgnoreCase(u.getRole())).count();
            
            File pdfFile = ReportPDFGenerator.generateComprehensiveReport(
                checkInsToday, checkOutsToday, dailyOccupancyRate, dailyRevenue,
                totalRooms, occupiedRooms, availableRooms, needsCleaning, underMaintenance,
                monthlyRevenue, cashRevenue, cardRevenue, allBookings.size(), cashCount, cardCount,
                occupancyRate, avgStayDuration,
                totalGuestsThisMonth, currentGuests, avgStayDuration, completedBookings,
                confirmedBookings, currentGuests,
                totalStaff, cleaningStaff, maintenanceStaff, roomsBeingCleaned + roomsUnderMaintenance,
                admins, receptionists, roomsBeingCleaned, roomsUnderMaintenance
            );
            
            if (pdfFile != null && pdfFile.exists()) {
                int response = JOptionPane.showConfirmDialog(
                    this,
                    "Comprehensive report exported successfully to:\n" + pdfFile.getAbsolutePath() + "\n\nWould you like to open it?",
                    "Export Successful",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                if (response == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(pdfFile);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error exporting report: " + ex.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
    
    private LocalDate[] getDateRange() {
        String period = (String) dateRangeCombo.getSelectedItem();
        LocalDate now = LocalDate.now();
        LocalDate startDate;
        
        switch (period) {
            case "Today":
                startDate = now;
                break;
            case "This Week":
                startDate = now.minusDays(now.getDayOfWeek().getValue() - 1); // Start of week (Monday)
                break;
            case "This Month":
                startDate = now.withDayOfMonth(1);
                break;
            case "Last 30 Days":
                startDate = now.minusDays(30);
                break;
            case "This Year":
                startDate = now.withDayOfYear(1);
                break;
            default:
                startDate = now;
        }
        
        return new LocalDate[]{startDate, now};
    }
    
    private void styleComboBox(JComboBox<?> combo) {
        combo.setBackground(new Color(40, 40, 45));
        combo.setForeground(TEXT_COLOR);
        combo.setFocusable(false);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(GOLD_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 200, 100));
                } else {
                    g2.setColor(GOLD_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(110, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private JPanel createDailyReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        LocalDate[] dateRange = getDateRange();
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];
        
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Room> allRooms = roomService.getAllRooms();
        
        long checkInsToday = allBookings.stream()
            .filter(b -> b.getCheckInDate() != null)
            .filter(b -> {
                LocalDate checkInDate = b.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return !checkInDate.isBefore(startDate) && !checkInDate.isAfter(endDate);
            })
            .count();
            
        long checkOutsToday = allBookings.stream()
            .filter(b -> b.getActualCheckOutDate() != null)
            .filter(b -> {
                LocalDate checkOutDate = b.getActualCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return !checkOutDate.isBefore(startDate) && !checkOutDate.isAfter(endDate);
            })
            .count();
            
        long occupiedRooms = allRooms.stream().filter(r -> "Occupied".equals(r.getStatus())).count();
        double occupancyRate = (occupiedRooms * 100.0) / allRooms.size();
        
        double dailyRevenue = allBookings.stream()
            .filter(b -> b.getBookingDate() != null && b.getTotalAmount() > 0)
            .filter(b -> {
                LocalDate bookingDate = b.getBookingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return !bookingDate.isBefore(startDate) && !bookingDate.isAfter(endDate);
            })
            .mapToDouble(Booking::getTotalAmount)
            .sum();
        
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        summaryPanel.add(createMetricCard("Check-Ins Today", String.valueOf(checkInsToday), "👥", new Color(46, 204, 113)));
        summaryPanel.add(createMetricCard("Check-Outs Today", String.valueOf(checkOutsToday), "📤", new Color(52, 152, 219)));
        summaryPanel.add(createMetricCard("Occupancy Rate", String.format("%.1f%%", occupancyRate), "🏨", new Color(155, 89, 182)));
        summaryPanel.add(createMetricCard("Daily Revenue", "LKR " + String.format("%,.0f", dailyRevenue), "💰", GOLD_COLOR));
        
        panel.add(summaryPanel);
        panel.add(Box.createVerticalStrut(20));
        
        JPanel detailsPanel = createReportDetailsPanel("Daily Operations Details",
            new String[]{"Category", "Value", "Status"},
            new String[][]{
                {"Rooms Needing Cleaning", String.valueOf(allRooms.stream().filter(r -> "Needs Cleaning".equals(r.getStatus())).count()), "Pending"},
                {"Under Maintenance", String.valueOf(allRooms.stream().filter(r -> "Under Maintenance".equals(r.getStatus())).count()), "In Progress"},
                {"Occupied Rooms", String.valueOf(occupiedRooms), "In Use"},
                {"Available Rooms", String.valueOf(allRooms.size() - occupiedRooms), "Ready"},
                {"Total Rooms", String.valueOf(allRooms.size()), "Active"}
            });
        panel.add(detailsPanel);
        
        panel.add(Box.createVerticalGlue());
        return panel;
    }
    
    private JPanel createFinancialReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        LocalDate[] dateRange = getDateRange();
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];
        
        List<Booking> allBookings = bookingService.getAllBookings();
        
        List<Booking> periodBookings = allBookings.stream()
            .filter(b -> b.getBookingDate() != null)
            .filter(b -> {
                LocalDate bookingDate = b.getBookingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return !bookingDate.isBefore(startDate) && !bookingDate.isAfter(endDate);
            })
            .toList();
        
        double monthlyRevenue = periodBookings.stream()
            .filter(b -> b.getTotalAmount() > 0)
            .mapToDouble(Booking::getTotalAmount)
            .sum();
            
        double cashRevenue = periodBookings.stream()
            .filter(b -> "CASH".equals(b.getPaymentType()) && b.getTotalAmount() > 0)
            .mapToDouble(Booking::getTotalAmount)
            .sum();
            
        double cardRevenue = periodBookings.stream()
            .filter(b -> "CARD".equals(b.getPaymentType()) && b.getTotalAmount() > 0)
            .mapToDouble(Booking::getTotalAmount)
            .sum();
            
        long totalBookings = periodBookings.size();
        
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        summaryPanel.add(createMetricCard("Monthly Revenue", "LKR " + String.format("%,.0f", monthlyRevenue), "💵", new Color(46, 204, 113)));
        summaryPanel.add(createMetricCard("Cash Payments", "LKR " + String.format("%,.0f", cashRevenue), "💵", new Color(52, 152, 219)));
        summaryPanel.add(createMetricCard("Card Payments", "LKR " + String.format("%,.0f", cardRevenue), "💳", new Color(155, 89, 182)));
        summaryPanel.add(createMetricCard("Total Bookings", String.valueOf(totalBookings), "📊", GOLD_COLOR));
        
        panel.add(summaryPanel);
        panel.add(Box.createVerticalStrut(20));
        
        JPanel detailsPanel = createReportDetailsPanel("Revenue Breakdown",
            new String[]{"Payment Type", "Count", "Amount (LKR)"},
            new String[][]{
                {"Cash Payments", String.valueOf(allBookings.stream().filter(b -> "CASH".equals(b.getPaymentType())).count()), 
                    String.format("%,.2f", cashRevenue)},
                {"Card Payments", String.valueOf(allBookings.stream().filter(b -> "CARD".equals(b.getPaymentType())).count()), 
                    String.format("%,.2f", cardRevenue)},
                {"Total", String.valueOf(allBookings.size()), String.format("%,.2f", cashRevenue + cardRevenue)}
            });
        panel.add(detailsPanel);
        
        panel.add(Box.createVerticalGlue());
        return panel;
    }
    
    private JPanel createOccupancyReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        List<Room> allRooms = roomService.getAllRooms();
        List<Booking> allBookings = bookingService.getAllBookings();
        
        long totalRooms = allRooms.size();
        long occupiedRooms = allRooms.stream().filter(r -> "Occupied".equals(r.getStatus())).count();
        long availableRooms = allRooms.stream().filter(r -> "Available".equals(r.getStatus())).count();
        double occupancyRate = (occupiedRooms * 100.0) / totalRooms;
        
        double avgStayDuration = allBookings.stream()
            .filter(b -> "CHECKED_OUT".equals(b.getBookingStatus()))
            .mapToInt(Booking::getNumberOfNights)
            .average()
            .orElse(0.0);
        
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        summaryPanel.add(createMetricCard("Occupancy Rate", String.format("%.1f%%", occupancyRate), "📊", new Color(46, 204, 113)));
        summaryPanel.add(createMetricCard("Occupied Rooms", String.valueOf(occupiedRooms), "🏨", new Color(52, 152, 219)));
        summaryPanel.add(createMetricCard("Available Rooms", String.valueOf(availableRooms), "✅", new Color(155, 89, 182)));
        summaryPanel.add(createMetricCard("Avg Stay Duration", String.format("%.1f nights", avgStayDuration), "⏱️", GOLD_COLOR));
        
        panel.add(summaryPanel);
        panel.add(Box.createVerticalStrut(20));
        
        JPanel detailsPanel = createReportDetailsPanel("Room Status Distribution",
            new String[]{"Status", "Count", "Percentage"},
            new String[][]{
                {"Occupied", String.valueOf(occupiedRooms), String.format("%.1f%%", (occupiedRooms * 100.0) / totalRooms)},
                {"Available", String.valueOf(availableRooms), String.format("%.1f%%", (availableRooms * 100.0) / totalRooms)},
                {"Needs Cleaning", String.valueOf(allRooms.stream().filter(r -> "Needs Cleaning".equals(r.getStatus())).count()), 
                    String.format("%.1f%%", (allRooms.stream().filter(r -> "Needs Cleaning".equals(r.getStatus())).count() * 100.0) / totalRooms)},
                {"Under Maintenance", String.valueOf(allRooms.stream().filter(r -> "Under Maintenance".equals(r.getStatus())).count()), 
                    String.format("%.1f%%", (allRooms.stream().filter(r -> "Under Maintenance".equals(r.getStatus())).count() * 100.0) / totalRooms)}
            });
        panel.add(detailsPanel);
        
        panel.add(Box.createVerticalGlue());
        return panel;
    }
    
    private JPanel createGuestReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        LocalDate[] dateRange = getDateRange();
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];
        
        List<Booking> allBookings = bookingService.getAllBookings();
        
        long totalGuestsThisMonth = allBookings.stream()
            .filter(b -> b.getBookingDate() != null)
            .filter(b -> {
                LocalDate bookingDate = b.getBookingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return !bookingDate.isBefore(startDate) && !bookingDate.isAfter(endDate);
            })
            .count();
            
        long currentGuests = allBookings.stream()
            .filter(b -> {
                if ("CHECKED_IN".equals(b.getBookingStatus())) {
                    return true;
                }
                if ("CONFIRMED".equals(b.getBookingStatus()) && b.getCheckInDate() != null) {
                    LocalDate checkInDate = b.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate now = LocalDate.now();
                    return !checkInDate.isAfter(now);
                }
                return false;
            })
            .count();
            
        double avgStayDuration = allBookings.stream()
            .filter(b -> "CHECKED_OUT".equals(b.getBookingStatus()))
            .mapToInt(Booking::getNumberOfNights)
            .average()
            .orElse(0.0);
            
        long completedBookings = allBookings.stream()
            .filter(b -> "CHECKED_OUT".equals(b.getBookingStatus()))
            .count();
        
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        summaryPanel.add(createMetricCard("Guests This Month", String.valueOf(totalGuestsThisMonth), "👥", new Color(46, 204, 113)));
        summaryPanel.add(createMetricCard("Currently Staying", String.valueOf(currentGuests), "🔑", new Color(52, 152, 219)));
        summaryPanel.add(createMetricCard("Avg Stay", String.format("%.1f nights", avgStayDuration), "⏱️", new Color(155, 89, 182)));
        summaryPanel.add(createMetricCard("Completed Stays", String.valueOf(completedBookings), "✅", GOLD_COLOR));
        
        panel.add(summaryPanel);
        panel.add(Box.createVerticalStrut(20));
        
        JPanel detailsPanel = createReportDetailsPanel("Guest Statistics",
            new String[]{"Metric", "Value", "Percentage"},
            new String[][]{
                
                {"Checked In", String.valueOf(currentGuests), 
                    String.format("%.1f%%", (currentGuests * 100.0) / allBookings.size())},
                {"Checked Out", String.valueOf(completedBookings), 
                    String.format("%.1f%%", (completedBookings * 100.0) / allBookings.size())},
                {"Total Bookings", String.valueOf(allBookings.size()), "100%"}
            });
        panel.add(detailsPanel);
        
        panel.add(Box.createVerticalGlue());
        return panel;
    }
    
    private JPanel createStaffReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        List<Staff> allStaff = staffService.getAllStaff();
        List<com.hotel.management.model.User> allUsers = userService.getAllUsers();
        List<Room> allRooms = roomService.getAllRooms();
        
        int totalStaff = allStaff.size() + allUsers.size();
        int cleaningStaff = (int) allStaff.stream().filter(s -> "Cleaning Staff".equals(s.getRole())).count();
        int maintenanceStaff = (int) allStaff.stream().filter(s -> "Maintenance Staff".equals(s.getRole())).count();
        
        long roomsBeingCleaned = allRooms.stream()
            .filter(r -> "Needs Cleaning".equals(r.getStatus()) && r.getAssignedStaff() != null && !r.getAssignedStaff().isEmpty())
            .count();
            
        long roomsUnderMaintenance = allRooms.stream()
            .filter(r -> "Under Maintenance".equals(r.getStatus()))
            .count();
        
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        summaryPanel.add(createMetricCard("Total Staff", String.valueOf(totalStaff), "👥", new Color(46, 204, 113)));
        summaryPanel.add(createMetricCard("Cleaning Staff", String.valueOf(cleaningStaff), "🧹", new Color(52, 152, 219)));
        summaryPanel.add(createMetricCard("Maintenance Staff", String.valueOf(maintenanceStaff), "🔧", new Color(155, 89, 182)));
        summaryPanel.add(createMetricCard("Active Tasks", String.valueOf(roomsBeingCleaned + roomsUnderMaintenance), "📋", GOLD_COLOR));
        
        panel.add(summaryPanel);
        panel.add(Box.createVerticalStrut(20));
        
        JPanel detailsPanel = createReportDetailsPanel("Staff Distribution",
            new String[]{"Role", "Count", "Tasks"},
            new String[][]{
                {"Administrators", String.valueOf(allUsers.stream().filter(u -> "admin".equalsIgnoreCase(u.getRole())).count()), "Management"},
                {"Receptionists", String.valueOf(allUsers.stream().filter(u -> "receptionist".equalsIgnoreCase(u.getRole())).count()), "Check-in/out"},
                {"Cleaning Staff", String.valueOf(cleaningStaff), String.valueOf(roomsBeingCleaned) + " Active"},
                {"Maintenance Staff", String.valueOf(maintenanceStaff), String.valueOf(roomsUnderMaintenance) + " Active"}
            });
        panel.add(detailsPanel);
        
        panel.add(Box.createVerticalGlue());
        return panel;
    }
    
    
    private JPanel createMetricCard(String title, String value, String emoji, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(DARK_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 80));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout(10, 5));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        card.add(emojiLabel, BorderLayout.WEST);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(new Color(180, 180, 180));
        textPanel.add(titleLabel);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);
        textPanel.add(valueLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createReportDetailsPanel(String title, String[] columns, String[][] data) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(DARK_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setForeground(TEXT_COLOR);
        table.setBackground(new Color(35, 35, 40));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(60, 60, 65));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setForeground(GOLD_COLOR);
        table.getTableHeader().setBackground(new Color(30, 30, 35));
        
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
}
