package com.hotel.management.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;
import java.util.Date;

public class EmailService {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String FROM_EMAIL = dotenv.get("MAIL_EMAIL");
    private static final String APP_PASSWORD = dotenv.get("MAIL_PASSWORD");

    public static boolean sendBookingConfirmation(
            String toEmail,
            String guestName,
            String roomType,
            String roomNumber,
            Date checkInDate,
            Date checkOutDate,
            int nights,
            double totalAmount,
            String paymentMethod,
            File pdfReceipt) {

        if (FROM_EMAIL == null || FROM_EMAIL.equals("your-email@gmail.com") ||
            APP_PASSWORD == null || APP_PASSWORD.equals("your-app-password-here")) {
            System.err.println("Email configuration not set in .env file. Skipping email.");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "INNOVA Hotel"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Booking Confirmation - INNOVA Hotel");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = EmailTemplate.bookingConfirmation(
                guestName,
                roomType,
                roomNumber,
                checkInDate,
                checkOutDate,
                nights,
                totalAmount,
                paymentMethod
            );
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            if (pdfReceipt != null && pdfReceipt.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(pdfReceipt);
                attachmentPart.setFileName(pdfReceipt.getName());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            message.setSentDate(new Date());

            Transport.send(message);
            System.out.println("✓ Booking confirmation email sent successfully to " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendBookingConfirmation(
            String toEmail,
            String guestName,
            String roomType,
            String roomNumber,
            Date checkInDate,
            Date checkOutDate,
            int nights,
            double totalAmount,
            String paymentMethod) {
        return sendBookingConfirmation(toEmail, guestName, roomType, roomNumber,
                checkInDate, checkOutDate, nights, totalAmount, paymentMethod, null);
    }

    public static boolean sendStaffWelcomeEmail(
            String toEmail,
            String staffName,
            String staffId,
            String role,
            String phone,
            String joinDate) {

        if (FROM_EMAIL == null || FROM_EMAIL.equals("your-email@gmail.com") ||
            APP_PASSWORD == null || APP_PASSWORD.equals("your-app-password-here")) {
            System.err.println("Email configuration not set in .env file. Skipping email.");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "INNOVA Hotel - HR Department"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Welcome to INNOVA Hotel Team - Staff Onboarding");

            String htmlContent = EmailTemplate.staffWelcomeEmail(
                staffName,
                staffId,
                role,
                toEmail,
                phone,
                joinDate
            );
            message.setContent(htmlContent, "text/html; charset=utf-8");
            message.setSentDate(new Date());

            Transport.send(message);
            System.out.println("✓ Staff welcome email sent successfully to " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to send staff welcome email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendWorkAssignmentEmail(
            String toEmail,
            String staffName,
            String taskType,
            String roomNumber,
            String roomType,
            String priority,
            String assignedTime) {

        if (FROM_EMAIL == null || FROM_EMAIL.equals("your-email@gmail.com") ||
            APP_PASSWORD == null || APP_PASSWORD.equals("your-app-password-here")) {
            System.err.println("Email configuration not set in .env file. Skipping email.");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "INNOVA Hotel - Operations"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("New Task Assignment - Room " + roomNumber);

            String htmlContent = EmailTemplate.workAssignmentEmail(
                staffName,
                taskType,
                roomNumber,
                roomType,
                priority,
                assignedTime
            );
            message.setContent(htmlContent, "text/html; charset=utf-8");
            message.setSentDate(new Date());

            Transport.send(message);
            System.out.println("✓ Work assignment email sent successfully to " + toEmail + " for Room " + roomNumber);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to send work assignment email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
