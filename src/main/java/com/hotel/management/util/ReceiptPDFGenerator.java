package com.hotel.management.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptPDFGenerator {
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    
    private static final float GOLD_R = 1.0f;
    private static final float GOLD_G = 0.706f;
    private static final float GOLD_B = 0.235f;
    
    public static File generateReceipt(
            String guestName,
            String email,
            String phone,
            String roomType,
            String roomNumber,
            Date checkInDate,
            Date checkOutDate,
            int numberOfNights,
            double totalAmount,
            String paymentMethod
    ) throws IOException {
        
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();
        
        try {
           
            contentStream.setNonStrokingColor(GOLD_R, GOLD_G, GOLD_B);
            contentStream.addRect(0, page.getMediaBox().getHeight() - 8, pageWidth, 8);
            contentStream.fill();
            contentStream.setNonStrokingColor(0f, 0f, 0f);
            
            contentStream.setFont(PDType1Font.TIMES_BOLD, 36);
            String hotelName = "INNOVA";
            float titleWidth = PDType1Font.TIMES_BOLD.getStringWidth(hotelName) / 1000 * 36;
            contentStream.beginText();
            contentStream.newLineAtOffset((pageWidth - titleWidth) / 2, yPosition);
            contentStream.showText(hotelName);
            contentStream.endText();
            
            yPosition -= 18;
            
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            String tagline = "Luxury & Comfort Redefined";
            float taglineWidth = PDType1Font.HELVETICA.getStringWidth(tagline) / 1000 * 11;
            contentStream.beginText();
            contentStream.newLineAtOffset((pageWidth - taglineWidth) / 2, yPosition);
            contentStream.showText(tagline);
            contentStream.endText();
            
            yPosition -= 25;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            String receiptTitle = "BOOKING RECEIPT";
            float receiptTitleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(receiptTitle) / 1000 * 16;
            float boxX = (pageWidth - receiptTitleWidth - 20) / 2;
            
            contentStream.setStrokingColor(GOLD_R, GOLD_G, GOLD_B);
            contentStream.setLineWidth(2);
            contentStream.addRect(boxX, yPosition - 5, receiptTitleWidth + 20, 25);
            contentStream.stroke();
            contentStream.setStrokingColor(0f, 0f, 0f); 
            
            contentStream.beginText();
            contentStream.newLineAtOffset((pageWidth - receiptTitleWidth) / 2, yPosition + 2);
            contentStream.showText(receiptTitle);
            contentStream.endText();
            
            yPosition -= 35;
            
            contentStream.setLineWidth(1.5f);
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(pageWidth - margin, yPosition);
            contentStream.stroke();
            yPosition -= 3;
            contentStream.setLineWidth(0.5f);
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(pageWidth - margin, yPosition);
            contentStream.stroke();
            
            yPosition -= 25;
            
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            String receiptDate = "Generated: " + dateFormat.format(new Date()) + " at " + timeFormat.format(new Date());
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(receiptDate);
            contentStream.endText();
            
            String receiptNumber = "Receipt #: INNOVA-" + System.currentTimeMillis();
            float receiptNumWidth = PDType1Font.HELVETICA.getStringWidth(receiptNumber) / 1000 * 10;
            contentStream.beginText();
            contentStream.newLineAtOffset(pageWidth - margin - receiptNumWidth, yPosition);
            contentStream.showText(receiptNumber);
            contentStream.endText();
            
            yPosition -= 35;
            
            drawSectionHeader(contentStream, margin, yPosition, pageWidth, "GUEST INFORMATION");
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            addFieldStyled(contentStream, margin + 15, yPosition, "Name:", guestName);
            yPosition -= 20;
            addFieldStyled(contentStream, margin + 15, yPosition, "Email:", email);
            yPosition -= 20;
            addFieldStyled(contentStream, margin + 15, yPosition, "Phone:", phone);
            
            yPosition -= 30;
            
            drawSectionHeader(contentStream, margin, yPosition, pageWidth, "ROOM DETAILS");
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            addFieldStyled(contentStream, margin + 15, yPosition, "Room Type:", roomType);
            yPosition -= 20;
            addFieldStyled(contentStream, margin + 15, yPosition, "Room Number:", roomNumber);
            
            yPosition -= 30;
            
            drawSectionHeader(contentStream, margin, yPosition, pageWidth, "BOOKING PERIOD");
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            addFieldStyled(contentStream, margin + 15, yPosition, "Check-in Date:", dateFormat.format(checkInDate));
            yPosition -= 20;
            addFieldStyled(contentStream, margin + 15, yPosition, "Check-out Date:", dateFormat.format(checkOutDate));
            yPosition -= 20;
            addFieldStyled(contentStream, margin + 15, yPosition, "Number of Nights:", String.valueOf(numberOfNights) + " night(s)");
            
            yPosition -= 30;
            
            drawSectionHeader(contentStream, margin, yPosition, pageWidth, "PAYMENT DETAILS");
            yPosition -= 30;
            
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            float extraOffset = addFieldStyled(contentStream, margin + 15, yPosition, "Payment Method:", paymentMethod);
            yPosition -= (25 + extraOffset); 
            
            float boxHeight = 35;
            float boxY = yPosition - boxHeight + 8;
            
            contentStream.setStrokingColor(GOLD_R, GOLD_G, GOLD_B);
            contentStream.setLineWidth(2);
            contentStream.addRect(margin, boxY, pageWidth - 2 * margin, boxHeight);
            contentStream.stroke();
            contentStream.setStrokingColor(0f, 0f, 0f);
            
            contentStream.setNonStrokingColor(1.0f, 0.95f, 0.8f);
            contentStream.addRect(margin + 2, boxY + 2, pageWidth - 2 * margin - 4, boxHeight - 4);
            contentStream.fill();
            contentStream.setNonStrokingColor(0f, 0f, 0f);
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + 15, yPosition - 10);
            contentStream.showText("TOTAL AMOUNT:");
            contentStream.endText();
            
            String totalText = String.format("LKR %.2f", totalAmount);
            float totalWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(totalText) / 1000 * 14;
            contentStream.beginText();
            contentStream.newLineAtOffset(pageWidth - margin - totalWidth - 15, yPosition - 10);
            contentStream.showText(totalText);
            contentStream.endText();
            
            yPosition -= 50;
            
            contentStream.setLineWidth(1.5f);
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(pageWidth - margin, yPosition);
            contentStream.stroke();
            yPosition -= 3;
            contentStream.setLineWidth(0.5f);
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(pageWidth - margin, yPosition);
            contentStream.stroke();
            
            yPosition -= 25;
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
            String footer1 = "Thank you for choosing INNOVA!";
            float footer1Width = PDType1Font.HELVETICA_BOLD.getStringWidth(footer1) / 1000 * 11;
            contentStream.beginText();
            contentStream.newLineAtOffset((pageWidth - footer1Width) / 2, yPosition);
            contentStream.showText(footer1);
            contentStream.endText();
            
            yPosition -= 18;
            
            contentStream.setFont(PDType1Font.HELVETICA, 9);
            String footer2 = "We wish you a luxurious and memorable stay";
            float footer2Width = PDType1Font.HELVETICA.getStringWidth(footer2) / 1000 * 9;
            contentStream.beginText();
            contentStream.newLineAtOffset((pageWidth - footer2Width) / 2, yPosition);
            contentStream.showText(footer2);
            contentStream.endText();
            
            yPosition -= 25;
            
            contentStream.setFont(PDType1Font.HELVETICA, 8);
            String contact = "For inquiries: info@innovahotel.com | +94 11 234 5678 | www.innovahotel.com";
            float contactWidth = PDType1Font.HELVETICA.getStringWidth(contact) / 1000 * 8;
            contentStream.beginText();
            contentStream.newLineAtOffset((pageWidth - contactWidth) / 2, yPosition);
            contentStream.showText(contact);
            contentStream.endText();
            
            contentStream.setNonStrokingColor(GOLD_R, GOLD_G, GOLD_B);
            contentStream.addRect(0, 0, pageWidth, 8);
            contentStream.fill();
            
            contentStream.close();
            
            String userHome = System.getProperty("user.home");
            String downloadsPath = userHome + File.separator + "Downloads";
            String fileName = "INNOVA_Receipt_" + guestName.replaceAll("\\s+", "_") + "_" + System.currentTimeMillis() + ".pdf";
            File outputFile = new File(downloadsPath, fileName);
            
            document.save(outputFile);
            document.close();
            
            return outputFile;
            
        } catch (Exception e) {
            contentStream.close();
            document.close();
            throw e;
        }
    }
    
    private static void drawSectionHeader(PDPageContentStream contentStream, float margin, float y, float pageWidth, String title) throws IOException {
        
        contentStream.setStrokingColor(GOLD_R, GOLD_G, GOLD_B);
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(margin, y + 2);
        contentStream.lineTo(pageWidth - margin, y + 2);
        contentStream.stroke();
        contentStream.setStrokingColor(0f, 0f, 0f);
        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 13);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, y - 15);
        contentStream.showText(title);
        contentStream.endText();
    }
    
    private static float addFieldStyled(PDPageContentStream contentStream, float x, float y, String label, String value) throws IOException {
        
        contentStream.setNonStrokingColor(GOLD_R, GOLD_G, GOLD_B);
        contentStream.addRect(x - 8, y + 2, 3, 3);
        contentStream.fill();
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.setFont(PDType1Font.HELVETICA, 11);
        contentStream.showText(label);
        contentStream.endText();
        
        float labelWidth = PDType1Font.HELVETICA.getStringWidth(label) / 1000 * 11;
        String[] lines = value.split("\\n");
        float currentY = y;
        
        for (int i = 0; i < lines.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + labelWidth + 8, currentY);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
            contentStream.showText(lines[i]);
            contentStream.endText();
            
            if (i < lines.length - 1) {
                currentY -= 12; 
            }
        }
        
        return (lines.length - 1) * 12;
    }
    
    private static void addField(PDPageContentStream contentStream, float x, float y, String label, String value) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.showText(label);
        contentStream.endText();
        
        float labelWidth = PDType1Font.HELVETICA.getStringWidth(label) / 1000 * 10;
        contentStream.beginText();
        contentStream.newLineAtOffset(x + labelWidth + 10, y);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
        contentStream.showText(value);
        contentStream.endText();
    }
}
