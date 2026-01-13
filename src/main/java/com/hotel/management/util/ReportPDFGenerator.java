package com.hotel.management.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportPDFGenerator {
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    
    private static final float GOLD_R = 1.0f;
    private static final float GOLD_G = 0.706f;
    private static final float GOLD_B = 0.235f;
    
    private static final float GREEN_R = 0.18f;
    private static final float GREEN_G = 0.8f;
    private static final float GREEN_B = 0.44f;
    
    private static final float BLUE_R = 0.2f;
    private static final float BLUE_G = 0.6f;
    private static final float BLUE_B = 0.86f;
    
    private static final float PURPLE_R = 0.61f;
    private static final float PURPLE_G = 0.35f;
    private static final float PURPLE_B = 0.71f;
    
    
    public static File generateComprehensiveReport(
            long checkInsToday,
            long checkOutsToday,
            double dailyOccupancyRate,
            double dailyRevenue,
            long totalRooms,
            long occupiedRooms,
            long availableRooms,
            long needsCleaning,
            long underMaintenance,
            double monthlyRevenue,
            double cashRevenue,
            double cardRevenue,
            long totalBookings,
            long cashCount,
            long cardCount,
            double occupancyRate,
            double avgStayDuration,
            long guestsThisMonth,
            long currentGuests,
            double guestAvgStayDuration,
            long completedBookings,
            long confirmedBookings,
            long checkedInBookings,
            int totalStaff,
            int cleaningStaff,
            int maintenanceStaff,
            long activeTasks,
            long admins,
            long receptionists,
            long cleaningTasks,
            long maintenanceTasks
    ) throws IOException {
        
        PDDocument document = new PDDocument();
        
        try {
            PDPage page1 = new PDPage(PDRectangle.A4);
            document.addPage(page1);
            PDPageContentStream cs1 = new PDPageContentStream(document, page1);
            generateDailyReportPage(cs1, page1, checkInsToday, checkOutsToday, dailyOccupancyRate, 
                dailyRevenue, totalRooms, occupiedRooms, availableRooms, needsCleaning, underMaintenance);
            cs1.close();
            
            PDPage page2 = new PDPage(PDRectangle.A4);
            document.addPage(page2);
            PDPageContentStream cs2 = new PDPageContentStream(document, page2);
            generateFinancialReportPage(cs2, page2, monthlyRevenue, cashRevenue, cardRevenue, 
                totalBookings, cashCount, cardCount);
            cs2.close();
            
            PDPage page3 = new PDPage(PDRectangle.A4);
            document.addPage(page3);
            PDPageContentStream cs3 = new PDPageContentStream(document, page3);
            generateOccupancyReportPage(cs3, page3, occupancyRate, occupiedRooms, availableRooms, 
                avgStayDuration, totalRooms, needsCleaning, underMaintenance);
            cs3.close();
            
            PDPage page4 = new PDPage(PDRectangle.A4);
            document.addPage(page4);
            PDPageContentStream cs4 = new PDPageContentStream(document, page4);
            generateGuestReportPage(cs4, page4, guestsThisMonth, currentGuests, guestAvgStayDuration, 
                completedBookings, totalBookings, confirmedBookings, checkedInBookings);
            cs4.close();
            
            PDPage page5 = new PDPage(PDRectangle.A4);
            document.addPage(page5);
            PDPageContentStream cs5 = new PDPageContentStream(document, page5);
            generateStaffReportPage(cs5, page5, totalStaff, cleaningStaff, maintenanceStaff, 
                activeTasks, admins, receptionists, cleaningTasks, maintenanceTasks);
            cs5.close();
            
            String fileName = "INNOVA_Comprehensive_Report_" + System.currentTimeMillis() + ".pdf";
            File file = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + fileName);
            document.save(file);
            
            return file;
        } finally {
            document.close();
        }
    }
    
    
    public static File generateDailyReport(
            long checkInsToday,
            long checkOutsToday,
            double occupancyRate,
            double dailyRevenue,
            long totalRooms,
            long occupiedRooms,
            long availableRooms,
            long needsCleaning,
            long underMaintenance
    ) throws IOException {
        
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        try {
            yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "DAILY OPERATIONS REPORT");
            
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
            contentStream.endText();
            
            yPosition -= 35;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Key Metrics");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Check-Ins Today", String.valueOf(checkInsToday), GREEN_R, GREEN_G, GREEN_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Check-Outs Today", String.valueOf(checkOutsToday), BLUE_R, BLUE_G, BLUE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Occupancy Rate", String.format("%.1f%%", occupancyRate), PURPLE_R, PURPLE_G, PURPLE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Daily Revenue", "LKR " + String.format("%,.0f", dailyRevenue), GOLD_R, GOLD_G, GOLD_B);
            
            yPosition -= 20;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Daily Operations Details");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth);
            
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Rooms Needing Cleaning", String.valueOf(needsCleaning), "Pending");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Under Maintenance", String.valueOf(underMaintenance), "In Progress");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Occupied Rooms", String.valueOf(occupiedRooms), "In Use");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Available Rooms", String.valueOf(availableRooms), "Ready");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Total Rooms", String.valueOf(totalRooms), "Active");
            
            drawFooter(contentStream, page, margin);
            
        } finally {
            contentStream.close();
        }
        
        String fileName = "INNOVA_Daily_Report_" + System.currentTimeMillis() + ".pdf";
        File file = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + fileName);
        document.save(file);
        document.close();
        
        return file;
    }
    
    public static File generateFinancialReport(
            double monthlyRevenue,
            double cashRevenue,
            double cardRevenue,
            long totalBookings,
            long cashCount,
            long cardCount
    ) throws IOException {
        
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        try {
            yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "FINANCIAL REPORT");
            
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
            contentStream.endText();
            
            yPosition -= 35;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Financial Summary");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Monthly Revenue", "LKR " + String.format("%,.0f", monthlyRevenue), GREEN_R, GREEN_G, GREEN_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Cash Payments", "LKR " + String.format("%,.0f", cashRevenue), BLUE_R, BLUE_G, BLUE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Card Payments", "LKR " + String.format("%,.0f", cardRevenue), PURPLE_R, PURPLE_G, PURPLE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Total Bookings", String.valueOf(totalBookings), GOLD_R, GOLD_G, GOLD_B);
            
            yPosition -= 20;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Revenue Breakdown");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth, "Payment Type", "Count", "Amount (LKR)");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Cash Payments", String.valueOf(cashCount), String.format("%,.2f", cashRevenue));
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Card Payments", String.valueOf(cardCount), String.format("%,.2f", cardRevenue));
            
            contentStream.setNonStrokingColor(GOLD_R, GOLD_G, GOLD_B);
            contentStream.addRect(margin, yPosition - 5, pageWidth - 2 * margin, 25);
            contentStream.fill();
            contentStream.setNonStrokingColor(0f, 0f, 0f);
            
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Total", String.valueOf(cashCount + cardCount), String.format("%,.2f", cashRevenue + cardRevenue));
            
            drawFooter(contentStream, page, margin);
            
        } finally {
            contentStream.close();
        }
        
        String fileName = "INNOVA_Financial_Report_" + System.currentTimeMillis() + ".pdf";
        File file = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + fileName);
        document.save(file);
        document.close();
        
        return file;
    }
    
    public static File generateOccupancyReport(
            double occupancyRate,
            long occupiedRooms,
            long availableRooms,
            double avgStayDuration,
            long totalRooms,
            long needsCleaning,
            long underMaintenance
    ) throws IOException {
        
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        try {
            yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "OCCUPANCY REPORT");
            
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
            contentStream.endText();
            
            yPosition -= 35;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Occupancy Metrics");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Occupancy Rate", String.format("%.1f%%", occupancyRate), GREEN_R, GREEN_G, GREEN_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Occupied Rooms", String.valueOf(occupiedRooms), BLUE_R, BLUE_G, BLUE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Available Rooms", String.valueOf(availableRooms), PURPLE_R, PURPLE_G, PURPLE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Avg Stay Duration", String.format("%.1f nights", avgStayDuration), GOLD_R, GOLD_G, GOLD_B);
            
            yPosition -= 20;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Room Status Distribution");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth, "Status", "Count", "Percentage");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Occupied", String.valueOf(occupiedRooms), String.format("%.1f%%", (occupiedRooms * 100.0) / totalRooms));
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Available", String.valueOf(availableRooms), String.format("%.1f%%", (availableRooms * 100.0) / totalRooms));
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Needs Cleaning", String.valueOf(needsCleaning), String.format("%.1f%%", (needsCleaning * 100.0) / totalRooms));
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Under Maintenance", String.valueOf(underMaintenance), String.format("%.1f%%", (underMaintenance * 100.0) / totalRooms));
            
            drawFooter(contentStream, page, margin);
            
        } finally {
            contentStream.close();
        }
        
        String fileName = "INNOVA_Occupancy_Report_" + System.currentTimeMillis() + ".pdf";
        File file = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + fileName);
        document.save(file);
        document.close();
        
        return file;
    }
    
    public static File generateGuestReport(
            long guestsThisMonth,
            long currentGuests,
            double avgStayDuration,
            long completedBookings,
            long totalBookings,
            long confirmedBookings,
            long checkedInBookings
    ) throws IOException {
        
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        try {
            yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "GUEST ANALYTICS REPORT");
            
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
            contentStream.endText();
            
            yPosition -= 35;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Guest Metrics");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Guests This Month", String.valueOf(guestsThisMonth), GREEN_R, GREEN_G, GREEN_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Currently Staying", String.valueOf(currentGuests), BLUE_R, BLUE_G, BLUE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Avg Stay", String.format("%.1f nights", avgStayDuration), PURPLE_R, PURPLE_G, PURPLE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Completed Stays", String.valueOf(completedBookings), GOLD_R, GOLD_G, GOLD_B);
            
            yPosition -= 20;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Guest Statistics");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth, "Metric", "Value", "Percentage");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Total Bookings", String.valueOf(totalBookings), "100%");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Confirmed", String.valueOf(confirmedBookings), String.format("%.1f%%", (confirmedBookings * 100.0) / totalBookings));
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Checked In", String.valueOf(checkedInBookings), String.format("%.1f%%", (checkedInBookings * 100.0) / totalBookings));
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Checked Out", String.valueOf(completedBookings), String.format("%.1f%%", (completedBookings * 100.0) / totalBookings));
            
            drawFooter(contentStream, page, margin);
            
        } finally {
            contentStream.close();
        }
        
        String fileName = "INNOVA_Guest_Report_" + System.currentTimeMillis() + ".pdf";
        File file = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + fileName);
        document.save(file);
        document.close();
        
        return file;
    }
    
    public static File generateStaffReport(
            int totalStaff,
            int cleaningStaff,
            int maintenanceStaff,
            long activeTasks,
            long admins,
            long receptionists,
            long cleaningTasks,
            long maintenanceTasks
    ) throws IOException {
        
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        try {
            yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "STAFF PERFORMANCE REPORT");
            
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
            contentStream.endText();
            
            yPosition -= 35;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Staff Metrics");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Total Staff", String.valueOf(totalStaff), GREEN_R, GREEN_G, GREEN_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Cleaning Staff", String.valueOf(cleaningStaff), BLUE_R, BLUE_G, BLUE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Maintenance Staff", String.valueOf(maintenanceStaff), PURPLE_R, PURPLE_G, PURPLE_B);
            yPosition = drawMetricCard(contentStream, margin, yPosition, "Active Tasks", String.valueOf(activeTasks), GOLD_R, GOLD_G, GOLD_B);
            
            yPosition -= 20;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Staff Distribution");
            contentStream.endText();
            
            yPosition -= 25;
            
            yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth, "Role", "Count", "Tasks");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Administrators", String.valueOf(admins), "Management");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Receptionists", String.valueOf(receptionists), "Check-in/out");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Cleaning Staff", String.valueOf(cleaningStaff), cleaningTasks + " Active");
            yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Maintenance Staff", String.valueOf(maintenanceStaff), maintenanceTasks + " Active");
            
            drawFooter(contentStream, page, margin);
            
        } finally {
            contentStream.close();
        }
        
        String fileName = "INNOVA_Staff_Report_" + System.currentTimeMillis() + ".pdf";
        File file = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + fileName);
        document.save(file);
        document.close();
        
        return file;
    }
    
    
    private static float drawReportHeader(PDPageContentStream contentStream, PDPage page, float yPosition, float pageWidth, String reportTitle) throws IOException {
        
        contentStream.setNonStrokingColor(GOLD_R, GOLD_G, GOLD_B);
        contentStream.addRect(0, page.getMediaBox().getHeight() - 8, pageWidth, 8);
        contentStream.fill();
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        
        contentStream.setFont(PDType1Font.TIMES_BOLD, 32);
        String hotelName = "INNOVA";
        float titleWidth = PDType1Font.TIMES_BOLD.getStringWidth(hotelName) / 1000 * 32;
        contentStream.beginText();
        contentStream.newLineAtOffset((pageWidth - titleWidth) / 2, yPosition);
        contentStream.showText(hotelName);
        contentStream.endText();
        
        yPosition -= 16;
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        String tagline = "Luxury & Comfort Redefined";
        float taglineWidth = PDType1Font.HELVETICA.getStringWidth(tagline) / 1000 * 10;
        contentStream.beginText();
        contentStream.newLineAtOffset((pageWidth - taglineWidth) / 2, yPosition);
        contentStream.showText(tagline);
        contentStream.endText();
        
        yPosition -= 25;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        float reportTitleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(reportTitle) / 1000 * 14;
        float boxX = (pageWidth - reportTitleWidth - 20) / 2;
        
        contentStream.setStrokingColor(GOLD_R, GOLD_G, GOLD_B);
        contentStream.setLineWidth(2);
        contentStream.addRect(boxX, yPosition - 5, reportTitleWidth + 20, 22);
        contentStream.stroke();
        contentStream.setStrokingColor(0f, 0f, 0f);
        
        contentStream.beginText();
        contentStream.newLineAtOffset((pageWidth - reportTitleWidth) / 2, yPosition + 2);
        contentStream.showText(reportTitle);
        contentStream.endText();
        
        yPosition -= 25;
        
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(50, yPosition);
        contentStream.lineTo(pageWidth - 50, yPosition);
        contentStream.stroke();
        
        return yPosition;
    }
    
    private static float drawMetricCard(PDPageContentStream contentStream, float margin, float yPosition, String label, String value, float r, float g, float b) throws IOException {
       
        contentStream.setNonStrokingColor(r, g, b);
        contentStream.addRect(margin, yPosition - 5, 5, 25);
        contentStream.fill();
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 15, yPosition + 10);
        contentStream.showText(label + ":");
        contentStream.endText();
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.setNonStrokingColor(r, g, b);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 15, yPosition - 5);
        contentStream.showText(value);
        contentStream.endText();
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        
        return yPosition - 35;
    }
    
    private static float drawTableHeader(PDPageContentStream contentStream, float margin, float yPosition, float pageWidth) throws IOException {
        return drawTableHeader(contentStream, margin, yPosition, pageWidth, "Category", "Value", "Status");
    }
    
    private static float drawTableHeader(PDPageContentStream contentStream, float margin, float yPosition, float pageWidth, String col1, String col2, String col3) throws IOException {
        
        contentStream.setNonStrokingColor(0.12f, 0.12f, 0.14f);
        contentStream.addRect(margin, yPosition - 5, pageWidth - 2 * margin, 25);
        contentStream.fill();
        
        contentStream.setNonStrokingColor(GOLD_R, GOLD_G, GOLD_B);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
        
        float colWidth = (pageWidth - 2 * margin) / 3;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 10, yPosition + 5);
        contentStream.showText(col1);
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + colWidth + 10, yPosition + 5);
        contentStream.showText(col2);
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 2 * colWidth + 10, yPosition + 5);
        contentStream.showText(col3);
        contentStream.endText();
        
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        
        return yPosition - 25;
    }
    
    private static float drawTableRow(PDPageContentStream contentStream, float margin, float yPosition, float pageWidth, String col1, String col2, String col3) throws IOException {
        
        contentStream.setNonStrokingColor(0.96f, 0.96f, 0.96f);
        contentStream.addRect(margin, yPosition - 5, pageWidth - 2 * margin, 25);
        contentStream.fill();
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        
        contentStream.setStrokingColor(0.8f, 0.8f, 0.8f);
        contentStream.setLineWidth(0.5f);
        contentStream.moveTo(margin, yPosition - 5);
        contentStream.lineTo(pageWidth - margin, yPosition - 5);
        contentStream.stroke();
        contentStream.setStrokingColor(0f, 0f, 0f);
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        float colWidth = (pageWidth - 2 * margin) / 3;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 10, yPosition + 5);
        contentStream.showText(col1);
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + colWidth + 10, yPosition + 5);
        contentStream.showText(col2);
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 2 * colWidth + 10, yPosition + 5);
        contentStream.showText(col3);
        contentStream.endText();
        
        return yPosition - 25;
    }
    
    private static void drawFooter(PDPageContentStream contentStream, PDPage page, float margin) throws IOException {
        float pageWidth = page.getMediaBox().getWidth();
        float footerY = 40;
        
        contentStream.setStrokingColor(GOLD_R, GOLD_G, GOLD_B);
        contentStream.setLineWidth(1);
        contentStream.moveTo(margin, footerY);
        contentStream.lineTo(pageWidth - margin, footerY);
        contentStream.stroke();
        
        contentStream.setFont(PDType1Font.HELVETICA, 8);
        contentStream.setNonStrokingColor(0.4f, 0.4f, 0.4f);
        
        String footerText = "INNOVA Hotel Management System | innova@hotel.com | +94 11 234 5678";
        float footerWidth = PDType1Font.HELVETICA.getStringWidth(footerText) / 1000 * 8;
        
        contentStream.beginText();
        contentStream.newLineAtOffset((pageWidth - footerWidth) / 2, footerY - 15);
        contentStream.showText(footerText);
        contentStream.endText();
        
        contentStream.setNonStrokingColor(0f, 0f, 0f);
    }
    
    
    private static void generateDailyReportPage(PDPageContentStream contentStream, PDPage page,
            long checkInsToday, long checkOutsToday, double occupancyRate, double dailyRevenue,
            long totalRooms, long occupiedRooms, long availableRooms, long needsCleaning, long underMaintenance) throws IOException {
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "DAILY OPERATIONS REPORT");
        yPosition -= 30;
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
        contentStream.endText();
        yPosition -= 35;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Key Metrics");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Check-Ins Today", String.valueOf(checkInsToday), GREEN_R, GREEN_G, GREEN_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Check-Outs Today", String.valueOf(checkOutsToday), BLUE_R, BLUE_G, BLUE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Occupancy Rate", String.format("%.1f%%", occupancyRate), PURPLE_R, PURPLE_G, PURPLE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Daily Revenue", "LKR " + String.format("%,.0f", dailyRevenue), GOLD_R, GOLD_G, GOLD_B);
        yPosition -= 20;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Daily Operations Details");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth);
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Rooms Needing Cleaning", String.valueOf(needsCleaning), "Pending");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Under Maintenance", String.valueOf(underMaintenance), "In Progress");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Occupied Rooms", String.valueOf(occupiedRooms), "In Use");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Available Rooms", String.valueOf(availableRooms), "Ready");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Total Rooms", String.valueOf(totalRooms), "Active");
        
        drawFooter(contentStream, page, margin);
    }
    
    private static void generateFinancialReportPage(PDPageContentStream contentStream, PDPage page,
            double monthlyRevenue, double cashRevenue, double cardRevenue, long totalBookings, long cashCount, long cardCount) throws IOException {
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "FINANCIAL REPORT");
        yPosition -= 30;
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
        contentStream.endText();
        yPosition -= 35;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Financial Summary");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Monthly Revenue", "LKR " + String.format("%,.0f", monthlyRevenue), GREEN_R, GREEN_G, GREEN_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Cash Payments", "LKR " + String.format("%,.0f", cashRevenue), BLUE_R, BLUE_G, BLUE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Card Payments", "LKR " + String.format("%,.0f", cardRevenue), PURPLE_R, PURPLE_G, PURPLE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Total Bookings", String.valueOf(totalBookings), GOLD_R, GOLD_G, GOLD_B);
        yPosition -= 20;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Revenue Breakdown");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth, "Payment Type", "Count", "Amount (LKR)");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Cash Payments", String.valueOf(cashCount), String.format("%,.2f", cashRevenue));
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Card Payments", String.valueOf(cardCount), String.format("%,.2f", cardRevenue));
        
        contentStream.setNonStrokingColor(GOLD_R, GOLD_G, GOLD_B);
        contentStream.addRect(margin, yPosition - 5, pageWidth - 2 * margin, 25);
        contentStream.fill();
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Total", String.valueOf(cashCount + cardCount), String.format("%,.2f", cashRevenue + cardRevenue));
        
        drawFooter(contentStream, page, margin);
    }
    
    private static void generateOccupancyReportPage(PDPageContentStream contentStream, PDPage page,
            double occupancyRate, long occupiedRooms, long availableRooms, double avgStayDuration,
            long totalRooms, long needsCleaning, long underMaintenance) throws IOException {
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "OCCUPANCY REPORT");
        yPosition -= 30;
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
        contentStream.endText();
        yPosition -= 35;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Occupancy Metrics");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Occupancy Rate", String.format("%.1f%%", occupancyRate), GREEN_R, GREEN_G, GREEN_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Occupied Rooms", String.valueOf(occupiedRooms), BLUE_R, BLUE_G, BLUE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Available Rooms", String.valueOf(availableRooms), PURPLE_R, PURPLE_G, PURPLE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Avg Stay Duration", String.format("%.1f nights", avgStayDuration), GOLD_R, GOLD_G, GOLD_B);
        yPosition -= 20;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Room Status Distribution");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth, "Status", "Count", "Percentage");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Occupied", String.valueOf(occupiedRooms), String.format("%.1f%%", (occupiedRooms * 100.0) / totalRooms));
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Available", String.valueOf(availableRooms), String.format("%.1f%%", (availableRooms * 100.0) / totalRooms));
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Needs Cleaning", String.valueOf(needsCleaning), String.format("%.1f%%", (needsCleaning * 100.0) / totalRooms));
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Under Maintenance", String.valueOf(underMaintenance), String.format("%.1f%%", (underMaintenance * 100.0) / totalRooms));
        
        drawFooter(contentStream, page, margin);
    }
    
    private static void generateGuestReportPage(PDPageContentStream contentStream, PDPage page,
            long guestsThisMonth, long currentGuests, double avgStayDuration, long completedBookings,
            long totalBookings, long confirmedBookings, long checkedInBookings) throws IOException {
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "GUEST ANALYTICS REPORT");
        yPosition -= 30;
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
        contentStream.endText();
        yPosition -= 35;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Guest Metrics");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Guests This Month", String.valueOf(guestsThisMonth), GREEN_R, GREEN_G, GREEN_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Currently Staying", String.valueOf(currentGuests), BLUE_R, BLUE_G, BLUE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Avg Stay", String.format("%.1f nights", avgStayDuration), PURPLE_R, PURPLE_G, PURPLE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Completed Stays", String.valueOf(completedBookings), GOLD_R, GOLD_G, GOLD_B);
        yPosition -= 20;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Guest Statistics");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth, "Metric", "Value", "Percentage");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Checked In", String.valueOf(checkedInBookings), String.format("%.1f%%", (checkedInBookings * 100.0) / totalBookings));
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Checked Out", String.valueOf(completedBookings), String.format("%.1f%%", (completedBookings * 100.0) / totalBookings));
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Total Bookings", String.valueOf(totalBookings), "100%");
        
        drawFooter(contentStream, page, margin);
    }
    
    private static void generateStaffReportPage(PDPageContentStream contentStream, PDPage page,
            int totalStaff, int cleaningStaff, int maintenanceStaff, long activeTasks,
            long admins, long receptionists, long cleaningTasks, long maintenanceTasks) throws IOException {
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        yPosition = drawReportHeader(contentStream, page, yPosition, pageWidth, "STAFF PERFORMANCE REPORT");
        yPosition -= 30;
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date()));
        contentStream.endText();
        yPosition -= 35;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Staff Metrics");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Total Staff", String.valueOf(totalStaff), GREEN_R, GREEN_G, GREEN_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Cleaning Staff", String.valueOf(cleaningStaff), BLUE_R, BLUE_G, BLUE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Maintenance Staff", String.valueOf(maintenanceStaff), PURPLE_R, PURPLE_G, PURPLE_B);
        yPosition = drawMetricCard(contentStream, margin, yPosition, "Active Tasks", String.valueOf(activeTasks), GOLD_R, GOLD_G, GOLD_B);
        yPosition -= 20;
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Staff Distribution");
        contentStream.endText();
        yPosition -= 25;
        
        yPosition = drawTableHeader(contentStream, margin, yPosition, pageWidth, "Role", "Count", "Tasks");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Administrators", String.valueOf(admins), "Management");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Receptionists", String.valueOf(receptionists), "Check-in/out");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Cleaning Staff", String.valueOf(cleaningStaff), cleaningTasks + " Active");
        yPosition = drawTableRow(contentStream, margin, yPosition, pageWidth, "Maintenance Staff", String.valueOf(maintenanceStaff), maintenanceTasks + " Active");
        
        drawFooter(contentStream, page, margin);
    }
}
