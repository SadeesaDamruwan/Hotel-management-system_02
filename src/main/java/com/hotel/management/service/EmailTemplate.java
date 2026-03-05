package com.hotel.management.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailTemplate {

    private static final String GOLD_COLOR = "#DAA520";
    private static final String DARK_COLOR = "#2C3E50";

    public static String bookingConfirmation(
            String guestName,
            String roomType,
            String roomNumber,
            Date checkInDate,
            Date checkOutDate,
            int nights,
            double totalAmount,
            String paymentMethod) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

        return "<!DOCTYPE html>" +
            "<html lang='en'>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <title>Booking Confirmation</title>" +
            "</head>" +
            "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
            "    <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f4f4f4; padding: 20px;'>" +
            "        <tr>" +
            "            <td align='center'>" +
            "                <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
            "                    " +
            "                    <!-- Header with Gold Accent -->" +
            "                    <tr>" +
            "                        <td style='background: linear-gradient(135deg, " + DARK_COLOR + " 0%, #34495e 100%); padding: 40px 30px; text-align: center;'>" +
            "                            <h1 style='margin: 0; color: " + GOLD_COLOR + "; font-size: 36px; font-weight: bold; letter-spacing: 3px;'>INNOVA</h1>" +
            "                            <p style='margin: 10px 0 0 0; color: #ffffff; font-size: 14px; letter-spacing: 1px;'>LUXURY HOTEL</p>" +
            "                            <div style='width: 60px; height: 3px; background-color: " + GOLD_COLOR + "; margin: 20px auto 0;'></div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Success Message -->" +
            "                    <tr>" +
            "                        <td style='padding: 40px 30px 20px; text-align: center;'>" +
            "                            <table cellpadding='0' cellspacing='0' style='width: 60px; height: 60px; background-color: #27ae60; border-radius: 50%; margin: 0 auto 20px;'>" +
            "                                <tr>" +
            "                                    <td style='text-align: center; vertical-align: middle;'>" +
            "                                        <span style='color: white; font-size: 30px; line-height: 1;'>✓</span>" +
            "                                    </td>" +
            "                                </tr>" +
            "                            </table>" +
            "                            <h2 style='margin: 0 0 10px 0; color: " + DARK_COLOR + "; font-size: 28px;'>Booking Confirmed!</h2>" +
            "                            <p style='margin: 0; color: #7f8c8d; font-size: 16px;'>Thank you for choosing INNOVA Hotel</p>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Guest Details -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px;'>" +
            "                            <div style='background-color: #f8f9fa; border-left: 4px solid " + GOLD_COLOR + "; padding: 20px; border-radius: 4px;'>" +
            "                                <p style='margin: 0 0 5px 0; color: #7f8c8d; font-size: 12px; text-transform: uppercase; letter-spacing: 1px;'>Guest Name</p>" +
            "                                <p style='margin: 0; color: " + DARK_COLOR + "; font-size: 18px; font-weight: bold;'>" + guestName + "</p>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Booking Details -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 20px;'>" +
            "                            <h3 style='margin: 0 0 15px 0; color: " + DARK_COLOR + "; font-size: 20px; padding-bottom: 10px; border-bottom: 2px solid " + GOLD_COLOR + "; text-align: center;'>Reservation Details</h3>" +
            "                            " +
            "                            <table width='100%' cellpadding='12' cellspacing='0' style='border-collapse: collapse;'>" +
            "                                <tr style='border-bottom: 1px solid #ecf0f1;'>" +
            "                                    <td style='color: #7f8c8d; font-size: 14px; padding: 12px 0;'>Room Type</td>" +
            "                                    <td style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 14px; text-align: right; padding: 12px 0;'>" + roomType + "</td>" +
            "                                </tr>" +
            "                                <tr style='border-bottom: 1px solid #ecf0f1;'>" +
            "                                    <td style='color: #7f8c8d; font-size: 14px; padding: 12px 0;'>Room Number</td>" +
            "                                    <td style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 14px; text-align: right; padding: 12px 0;'>" + roomNumber + "</td>" +
            "                                </tr>" +
            "                                <tr style='border-bottom: 1px solid #ecf0f1;'>" +
            "                                    <td style='color: #7f8c8d; font-size: 14px; padding: 12px 0;'>Check-in Date</td>" +
            "                                    <td style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 14px; text-align: right; padding: 12px 0;'>" + dateFormat.format(checkInDate) + "</td>" +
            "                                </tr>" +
            "                                <tr style='border-bottom: 1px solid #ecf0f1;'>" +
            "                                    <td style='color: #7f8c8d; font-size: 14px; padding: 12px 0;'>Check-out Date</td>" +
            "                                    <td style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 14px; text-align: right; padding: 12px 0;'>" + dateFormat.format(checkOutDate) + "</td>" +
            "                                </tr>" +
            "                                <tr style='border-bottom: 1px solid #ecf0f1;'>" +
            "                                    <td style='color: #7f8c8d; font-size: 14px; padding: 12px 0;'>Number of Nights</td>" +
            "                                    <td style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 14px; text-align: right; padding: 12px 0;'>" + nights + " night(s)</td>" +
            "                                </tr>" +
            "                            </table>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Payment Details -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px;'>" +
            "                            <h3 style='margin: 0 0 15px 0; color: " + DARK_COLOR + "; font-size: 20px; padding-bottom: 10px; border-bottom: 2px solid " + GOLD_COLOR + "; text-align: center;'>Payment Information</h3>" +
            "                            " +
            "                            <table width='100%' cellpadding='12' cellspacing='0' style='border-collapse: collapse;'>" +
            "                                <tr style='border-bottom: 1px solid #ecf0f1;'>" +
            "                                    <td style='color: #7f8c8d; font-size: 14px; padding: 12px 0;'>Payment Method</td>" +
            "                                    <td style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 14px; text-align: right; padding: 12px 0;'>" + paymentMethod.replace("\n", "<br>") + "</td>" +
            "                                </tr>" +
            "                                <tr style='background-color: " + GOLD_COLOR + ";'>" +
            "                                    <td style='color: white; font-size: 16px; font-weight: bold; padding: 15px 0;'>Total Amount</td>" +
            "                                    <td style='color: white; font-weight: bold; font-size: 20px; text-align: right; padding: 15px 0;'>Rs. " + priceFormat.format(totalAmount) + "</td>" +
            "                                </tr>" +
            "                            </table>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Important Information -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px;'>" +
            "                            <div style='background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; border-radius: 4px;'>" +
            "                                <p style='margin: 0 0 8px 0; color: #856404; font-weight: bold; font-size: 14px;'>⚠ Important Information</p>" +
            "                                <p style='margin: 0; color: #856404; font-size: 13px; line-height: 1.5;'>" +
            "                                    • Please bring a valid photo ID at check-in<br>" +
            "                                    • Check-in time: 2:00 PM | Check-out time: 12:00 PM<br>" +
            "                                    • For any changes or cancellations, please contact us 24 hours in advance" +
            "                                </p>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Footer -->" +
            "                    <tr>" +
            "                        <td style='background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 4px solid " + GOLD_COLOR + ";'>" +
            "                            <p style='margin: 0 0 15px 0; color: " + DARK_COLOR + "; font-size: 16px; font-weight: bold;'>We look forward to welcoming you!</p>" +
            "                            <p style='margin: 0 0 5px 0; color: #7f8c8d; font-size: 13px;'>INNOVA</p>" +
            "                            <p style='margin: 0 0 5px 0; color: #7f8c8d; font-size: 13px;'>📞 Contact: +94 11 234 5678</p>" +
            "                            <p style='margin: 0 0 15px 0; color: #7f8c8d; font-size: 13px;'>✉️ Email: info@innovahotel.lk</p>" +
            "                            <div style='margin-top: 20px; padding-top: 20px; border-top: 1px solid #dee2e6;'>" +
            "                                <p style='margin: 0; color: #95a5a6; font-size: 11px;'>© 2025 INNOVA. All rights reserved.</p>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                </table>" +
            "            </td>" +
            "        </tr>" +
            "    </table>" +
            "</body>" +
            "</html>";
    }

    public static String staffWelcomeEmail(
            String staffName,
            String staffId,
            String role,
            String email,
            String phone,
            String joinDate) {

        return "<!DOCTYPE html>" +
            "<html lang='en'>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <title>Welcome to INNOVA Hotel</title>" +
            "</head>" +
            "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
            "    <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f4f4f4; padding: 20px;'>" +
            "        <tr>" +
            "            <td align='center'>" +
            "                <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
            "                    " +
            "                    <!-- Header with Gold Accent -->" +
            "                    <tr>" +
            "                        <td style='background: linear-gradient(135deg, " + DARK_COLOR + " 0%, #34495e 100%); padding: 50px 30px; text-align: center;'>" +
            "                            <h1 style='margin: 0; color: " + GOLD_COLOR + "; font-size: 42px; font-weight: bold; letter-spacing: 4px;'>INNOVA</h1>" +
            "                            <p style='margin: 10px 0 0 0; color: #ffffff; font-size: 15px; letter-spacing: 2px;'>LUXURY HOTEL</p>" +
            "                            <div style='width: 80px; height: 4px; background-color: " + GOLD_COLOR + "; margin: 25px auto 0;'></div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Welcome Banner -->" +
            "                    <tr>" +
            "                        <td style='background: linear-gradient(90deg, #27ae60 0%, #2ecc71 100%); padding: 30px; text-align: center;'>" +
            "                            <h2 style='margin: 0; color: white; font-size: 32px; font-weight: bold; text-shadow: 0 2px 4px rgba(0,0,0,0.2);'>🎉 Welcome to Our Team!</h2>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Welcome Message -->" +
            "                    <tr>" +
            "                        <td style='padding: 40px 30px 30px; text-align: center;'>" +
            "                            <p style='margin: 0 0 20px 0; color: #7f8c8d; font-size: 16px; line-height: 1.6;'>" +
            "                                Dear <strong style='color: " + DARK_COLOR + ";'>" + staffName + "</strong>," +
            "                            </p>" +
            "                            <p style='margin: 0; color: #7f8c8d; font-size: 16px; line-height: 1.8;'>" +
            "                                We are thrilled to welcome you to the <strong>INNOVA Hotel</strong> family! Your skills, dedication, and passion " +
            "                                make you a perfect fit for our team. We look forward to working together to deliver exceptional " +
            "                                experiences to our guests." +
            "                            </p>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Staff Details Card -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px;'>" +
            "                            <div style='background: linear-gradient(135deg, " + GOLD_COLOR + " 0%, #e8b84d 100%); padding: 25px; border-radius: 10px; box-shadow: 0 4px 15px rgba(218, 165, 32, 0.3);'>" +
            "                                <h3 style='margin: 0 0 20px 0; color: white; font-size: 22px; text-align: center; font-weight: bold;'>Your Staff Information</h3>" +
            "                                " +
            "                                <table width='100%' cellpadding='0' cellspacing='0' style='background-color: rgba(255,255,255,0.95); border-radius: 8px; border-collapse: collapse;'>" +
            "                                    <tr>" +
            "                                        <td width='40%' style='color: #7f8c8d; font-size: 14px; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'><strong>👤 Full Name</strong></td>" +
            "                                        <td width='60%' style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 15px; text-align: right; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'>" + staffName + "</td>" +
            "                                    </tr>" +
            "                                    <tr>" +
            "                                        <td width='40%' style='color: #7f8c8d; font-size: 14px; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'><strong>🆔 Staff ID</strong></td>" +
            "                                        <td width='60%' style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 16px; text-align: right; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'>" + staffId + "</td>" +
            "                                    </tr>" +
            "                                    <tr>" +
            "                                        <td width='40%' style='color: #7f8c8d; font-size: 14px; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'><strong>💼 Position</strong></td>" +
            "                                        <td width='60%' style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 15px; text-align: right; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'>" + role + "</td>" +
            "                                    </tr>" +
            "                                    <tr>" +
            "                                        <td width='40%' style='color: #7f8c8d; font-size: 14px; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'><strong>📧 Email</strong></td>" +
            "                                        <td width='60%' style='color: " + DARK_COLOR + "; font-size: 14px; text-align: right; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'>" + email + "</td>" +
            "                                    </tr>" +
            "                                    <tr>" +
            "                                        <td width='40%' style='color: #7f8c8d; font-size: 14px; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'><strong>📱 Phone</strong></td>" +
            "                                        <td width='60%' style='color: " + DARK_COLOR + "; font-size: 14px; text-align: right; padding: 15px 20px; border-bottom: 1px solid #ecf0f1;'>" + phone + "</td>" +
            "                                    </tr>" +
            "                                    <tr style='background-color: #2ecc71;'>" +
            "                                        <td width='40%' style='color: white; font-size: 15px; font-weight: bold; padding: 15px 20px;'><strong>📅 Join Date</strong></td>" +
            "                                        <td width='60%' style='color: white; font-weight: bold; font-size: 16px; text-align: right; padding: 15px 20px;'>" + joinDate + "</td>" +
            "                                    </tr>" +
            "                                </table>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Next Steps Section -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px;'>" +
            "                            <div style='background-color: #e8f8f5; border-left: 4px solid #27ae60; padding: 20px; border-radius: 4px;'>" +
            "                                <h3 style='margin: 0 0 15px 0; color: #27ae60; font-size: 18px;'>✅ What's Next?</h3>" +
            "                                <ul style='margin: 0; padding-left: 20px; color: #2c3e50; font-size: 14px; line-height: 1.8;'>" +
            "                                    <li>Please keep your <strong>Staff ID (" + staffId + ")</strong> secure - you'll need it for system access</li>" +
            "                                    <li>Report to the HR department on your first day to complete onboarding</li>" +
            "                                    <li>Your login credentials will be provided during orientation</li>" +
            "                                    <li>Review our employee handbook and hotel policies</li>" +
            "                                </ul>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Contact Information -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px;'>" +
            "                            <div style='background-color: #fff9e6; border-left: 4px solid " + GOLD_COLOR + "; padding: 20px; border-radius: 4px;'>" +
            "                                <h3 style='margin: 0 0 10px 0; color: " + GOLD_COLOR + "; font-size: 18px;'>📞 Need Help?</h3>" +
            "                                <p style='margin: 0; color: #856404; font-size: 14px; line-height: 1.6;'>" +
            "                                    If you have any questions or need assistance, please don't hesitate to contact our HR department:<br>" +
            "                                    <strong>HR Hotline:</strong> +94 11 234 5678<br>" +
            "                                    <strong>Email:</strong> hr@innovahotel.lk" +
            "                                </p>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Motivational Quote -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px; text-align: center;'>" +
            "                            <div style='border-top: 2px solid #ecf0f1; border-bottom: 2px solid #ecf0f1; padding: 25px 0;'>" +
            "                                <p style='margin: 0; color: " + GOLD_COLOR + "; font-size: 18px; font-style: italic; line-height: 1.6;'>" +
            "                                    \"Excellence is not a skill, it's an attitude.\"" +
            "                                </p>" +
            "                                <p style='margin: 10px 0 0 0; color: #95a5a6; font-size: 14px;'>— INNOVA Hotel Team</p>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Footer -->" +
            "                    <tr>" +
            "                        <td style='background-color: #f8f9fa; padding: 35px 30px; text-align: center; border-top: 4px solid " + GOLD_COLOR + ";'>" +
            "                            <p style='margin: 0 0 15px 0; color: " + DARK_COLOR + "; font-size: 17px; font-weight: bold;'>Welcome aboard! We're excited to have you!</p>" +
            "                            <div style='margin: 20px 0; padding: 20px 0; border-top: 1px solid #dee2e6; border-bottom: 1px solid #dee2e6;'>" +
            "                                <p style='margin: 0 0 8px 0; color: " + GOLD_COLOR + "; font-size: 20px; font-weight: bold; letter-spacing: 2px;'>INNOVA</p>" +
            "                                <p style='margin: 0 0 5px 0; color: #7f8c8d; font-size: 13px;'>123 Luxury Avenue, Colombo 03, Sri Lanka</p>" +
            "                                <p style='margin: 0 0 5px 0; color: #7f8c8d; font-size: 13px;'>📞 +94 11 234 5678 | ✉️ info@innovahotel.lk</p>" +
            "                                <p style='margin: 0; color: #7f8c8d; font-size: 13px;'>🌐 www.innovahotel.lk</p>" +
            "                            </div>" +
            "                            <p style='margin: 15px 0 0 0; color: #95a5a6; font-size: 11px;'>© 2025 INNOVA Luxury Hotel. All rights reserved.</p>" +
            "                            <p style='margin: 5px 0 0 0; color: #bdc3c7; font-size: 10px;'>This is an automated message. Please do not reply to this email.</p>" +
            "                        </td>" +
            "                    </tr>" +
            "                </table>" +
            "            </td>" +
            "        </tr>" +
            "    </table>" +
            "</body>" +
            "</html>";
    }

    public static String workAssignmentEmail(
            String staffName,
            String taskType,
            String roomNumber,
            String roomType,
            String priority,
            String assignedTime) {

        String taskIcon = taskType.toLowerCase().contains("clean") ? "🧹" : "🔧";
        String taskColor = taskType.toLowerCase().contains("clean") ? "#3498db" : "#e67e22";
        String priorityBadgeColor = priority.equalsIgnoreCase("HIGH") ? "#e74c3c" : 
                                     priority.equalsIgnoreCase("MEDIUM") ? "#f39c12" : "#27ae60";

        return "<!DOCTYPE html>" +
            "<html lang='en'>" +
            "<head>" +
            "    <meta charset='UTF-8'>" +
            "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "    <title>Work Assignment Notification</title>" +
            "</head>" +
            "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
            "    <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f4f4f4; padding: 20px;'>" +
            "        <tr>" +
            "            <td align='center'>" +
            "                <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
            "                    " +
            "                    <!-- Header with Gold Accent -->" +
            "                    <tr>" +
            "                        <td style='background: linear-gradient(135deg, " + DARK_COLOR + " 0%, #34495e 100%); padding: 40px 30px; text-align: center;'>" +
            "                            <h1 style='margin: 0; color: " + GOLD_COLOR + "; font-size: 36px; font-weight: bold; letter-spacing: 3px;'>INNOVA</h1>" +
            "                            <p style='margin: 10px 0 0 0; color: #ffffff; font-size: 14px; letter-spacing: 1px;'>LUXURY HOTEL</p>" +
            "                            <div style='width: 60px; height: 3px; background-color: " + GOLD_COLOR + "; margin: 20px auto 0;'></div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Task Alert Banner -->" +
            "                    <tr>" +
            "                        <td style='background: linear-gradient(90deg, " + taskColor + " 0%, " + taskColor + "ee 100%); padding: 25px 30px; text-align: center;'>" +
            "                            <table cellpadding='0' cellspacing='0' style='margin: 0 auto;'>" +
            "                                <tr>" +
            "                                    <td style='vertical-align: middle;'>" +
            "                                        <span style='font-size: 36px; margin-right: 15px;'>" + taskIcon + "</span>" +
            "                                    </td>" +
            "                                    <td style='vertical-align: middle;'>" +
            "                                        <h2 style='margin: 0; color: white; font-size: 28px; font-weight: bold; text-shadow: 0 2px 4px rgba(0,0,0,0.2);'>New Task Assignment</h2>" +
            "                                    </td>" +
            "                                </tr>" +
            "                            </table>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Greeting -->" +
            "                    <tr>" +
            "                        <td style='padding: 35px 30px 20px; text-align: center;'>" +
            "                            <p style='margin: 0; color: #7f8c8d; font-size: 16px; line-height: 1.6;'>" +
            "                                Hello <strong style='color: " + DARK_COLOR + "; font-size: 18px;'>" + staffName + "</strong>," +
            "                            </p>" +
            "                            <p style='margin: 15px 0 0 0; color: #7f8c8d; font-size: 15px; line-height: 1.7;'>" +
            "                                A new <strong>" + taskType + "</strong> task has been assigned to you. Please review the details below and proceed accordingly." +
            "                            </p>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Priority Badge -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 20px; text-align: center;'>" +
            "                            <div style='display: inline-block; background-color: " + priorityBadgeColor + "; color: white; padding: 10px 25px; border-radius: 20px; font-weight: bold; font-size: 14px; letter-spacing: 1px; box-shadow: 0 2px 8px rgba(0,0,0,0.2);'>" +
            "                                ⚡ PRIORITY: " + priority.toUpperCase() + "" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Task Details Card -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px;'>" +
            "                            <div style='background: linear-gradient(135deg, " + taskColor + " 0%, " + taskColor + "dd 100%); padding: 25px; border-radius: 10px; box-shadow: 0 4px 15px rgba(0,0,0,0.15);'>" +
            "                                <h3 style='margin: 0 0 20px 0; color: white; font-size: 20px; text-align: center; font-weight: bold; text-transform: uppercase; letter-spacing: 1px;'>📋 Task Details</h3>" +
            "                                " +
            "                                <table width='100%' cellpadding='0' cellspacing='0' style='background-color: rgba(255,255,255,0.98); border-radius: 8px; border-collapse: collapse;'>" +
            "                                    <tr>" +
            "                                        <td width='45%' style='color: #7f8c8d; font-size: 14px; padding: 18px 20px; border-bottom: 1px solid #ecf0f1;'><strong>🏨 Room Number</strong></td>" +
            "                                        <td width='55%' style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 20px; text-align: right; padding: 18px 20px; border-bottom: 1px solid #ecf0f1;'>" + roomNumber + "</td>" +
            "                                    </tr>" +
            "                                    <tr>" +
            "                                        <td width='45%' style='color: #7f8c8d; font-size: 14px; padding: 18px 20px; border-bottom: 1px solid #ecf0f1;'><strong>🛏️ Room Type</strong></td>" +
            "                                        <td width='55%' style='color: " + DARK_COLOR + "; font-weight: bold; font-size: 15px; text-align: right; padding: 18px 20px; border-bottom: 1px solid #ecf0f1;'>" + roomType + "</td>" +
            "                                    </tr>" +
            "                                    <tr>" +
            "                                        <td width='45%' style='color: #7f8c8d; font-size: 14px; padding: 18px 20px; border-bottom: 1px solid #ecf0f1;'><strong>" + taskIcon + " Task Type</strong></td>" +
            "                                        <td width='55%' style='color: " + taskColor + "; font-weight: bold; font-size: 15px; text-align: right; padding: 18px 20px; border-bottom: 1px solid #ecf0f1;'>" + taskType + "</td>" +
            "                                    </tr>" +
            "                                    <tr style='background-color: " + taskColor + ";'>" +
            "                                        <td width='45%' style='color: white; font-size: 15px; font-weight: bold; padding: 18px 20px;'><strong>⏰ Assigned Time</strong></td>" +
            "                                        <td width='55%' style='color: white; font-weight: bold; font-size: 16px; text-align: right; padding: 18px 20px;'>" + assignedTime + "</td>" +
            "                                    </tr>" +
            "                                </table>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Instructions Section -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 25px;'>" +
            "                            <div style='background-color: #e8f8f5; border-left: 4px solid #27ae60; padding: 20px; border-radius: 4px;'>" +
            "                                <h3 style='margin: 0 0 12px 0; color: #27ae60; font-size: 17px;'>✅ Action Required</h3>" +
            "                                <ul style='margin: 0; padding-left: 20px; color: #2c3e50; font-size: 14px; line-height: 2;'>" +
            "                                    <li>Please proceed to <strong>Room " + roomNumber + "</strong> at your earliest convenience</li>" +
            "                                    <li>Ensure you have all necessary equipment and supplies</li>" +
            "                                    <li>Follow standard operating procedures for " + taskType.toLowerCase() + "</li>" +
            "                                    <li>Update the task status in the system upon completion</li>" +
            "                                    <li>Report any issues or damages to your supervisor immediately</li>" +
            "                                </ul>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Quality Standards -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 25px;'>" +
            "                            <div style='background-color: #fff3cd; border-left: 4px solid " + GOLD_COLOR + "; padding: 20px; border-radius: 4px;'>" +
            "                                <h3 style='margin: 0 0 10px 0; color: #856404; font-size: 17px;'>⭐ Quality Standards</h3>" +
            "                                <p style='margin: 0; color: #856404; font-size: 14px; line-height: 1.7;'>" +
            "                                    Please maintain INNOVA's high standards of excellence. Pay attention to detail and ensure " +
            "                                    the room meets our quality checklist requirements. Your dedication to quality service " +
            "                                    is what makes INNOVA exceptional." +
            "                                </p>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Contact Information -->" +
            "                    <tr>" +
            "                        <td style='padding: 0 30px 30px;'>" +
            "                            <div style='background-color: #e3f2fd; border-left: 4px solid #2196f3; padding: 18px; border-radius: 4px;'>" +
            "                                <p style='margin: 0; color: #1565c0; font-size: 14px; line-height: 1.6;'>" +
            "                                    <strong>📞 Need Assistance?</strong><br>" +
            "                                    Contact your supervisor or call the operations desk:<br>" +
            "                                    <strong>Operations Hotline:</strong> +94 11 234 5678" +
            "                                </p>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    " +
            "                    <!-- Footer -->" +
            "                    <tr>" +
            "                        <td style='background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 4px solid " + GOLD_COLOR + ";'>" +
            "                            <p style='margin: 0 0 15px 0; color: " + DARK_COLOR + "; font-size: 16px; font-weight: bold;'>Thank you for your dedication and hard work!</p>" +
            "                            <div style='margin: 20px 0; padding: 15px 0; border-top: 1px solid #dee2e6; border-bottom: 1px solid #dee2e6;'>" +
            "                                <p style='margin: 0 0 5px 0; color: " + GOLD_COLOR + "; font-size: 18px; font-weight: bold; letter-spacing: 2px;'>INNOVA</p>" +
            "                                <p style='margin: 0 0 5px 0; color: #7f8c8d; font-size: 12px;'>Operations Department</p>" +
            "                                <p style='margin: 0; color: #7f8c8d; font-size: 12px;'>📞 +94 11 234 5678 | ✉️ operations@innovahotel.lk</p>" +
            "                            </div>" +
            "                            <p style='margin: 10px 0 0 0; color: #95a5a6; font-size: 11px;'>© 2026 INNOVA Luxury Hotel. All rights reserved.</p>" +
            "                            <p style='margin: 5px 0 0 0; color: #bdc3c7; font-size: 10px;'>This is an automated notification from the INNOVA Management System.</p>" +
            "                        </td>" +
            "                    </tr>" +
            "                </table>" +
            "            </td>" +
            "        </tr>" +
            "    </table>" +
            "</body>" +
            "</html>";
    }
}
