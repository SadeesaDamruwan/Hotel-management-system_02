# 🏨 Hotel Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-007396?style=flat-square&logo=java&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4.11.1-47A248?style=flat-square&logo=mongodb&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-17.0.2-orange?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=flat-square&logo=apache-maven&logoColor=white)

**A professional desktop application for managing hotel operations with role-based access control, payment integration, and automated workflows.**

</div>


## 🌟 Overview

The **Hotel Management System** is a comprehensive desktop application built with Java Swing and MongoDB, designed to streamline hotel operations including guest management, room booking, payment processing, staff management, and financial reporting. The system features separate dashboards for administrators and receptionists with secure role-based authentication.

### Key Highlights

- **Role-Based Access Control** - Separate dashboards for Admin and Receptionist roles
- **Payment Integration** - PayHere payment gateway for secure online transactions
- **Automated Workflows** - Email confirmations, PDF receipts, and room status automation
- **Real-Time Monitoring** - Live room status tracking and booking analytics
- **Professional UI/UX** - Modern Swing interface with custom components

---


## 📁 Project Structure

```
hotel-management-system/
├── src/main/java/com/hotel/management/
│   ├── Main.java                    # Application entry point
│   ├── model/                       # Data models (User, Guest, Room, Booking, etc.)
│   ├── service/                     # Business logic services
│   │   ├── UserService.java
│   │   ├── RoomService.java
│   │   ├── PayHereService.java
│   │   ├── EmailService.java
│   │   └── ...
│   ├── view/                        # UI components
│   │   ├── admin/                   # Admin dashboard panels
│   │   ├── receptionist/            # Receptionist dashboard panels
│   │   └── components/              # Reusable UI components
│   └── util/                        # Utility classes
│       ├── DatabaseConnection.java
│       ├── ReceiptPDFGenerator.java
│       └── UserCreationScript.java
├── src/main/resources/
│   └── images/                      # Application images
├── pom.xml                          # Maven configuration
└── .env                             # Environment variables
```


## ✨ Features

### Admin Dashboard
- **Staff Management** - Add, view, edit, and remove staff members with role assignments
- **Room Management** - Manage rooms with dynamic pricing, capacity, and real-time status tracking
- **Financial Reports** - Comprehensive revenue tracking with daily/monthly reports and PDF export
- **Guest Activity** - Monitor all check-ins/check-outs with detailed activity logs
- **Support Tickets** - View and manage guest support requests with status tracking

### Receptionist Dashboard
- **Guest Check-In/Out** - Quick booking process with guest profile management
- **Payment Processing** - PayHere payment gateway integration with multiple payment methods
- **Room Status** - Real-time room availability and status updates
- **PDF Invoices** - Automated receipt and invoice generation
- **Email Notifications** - Automated booking confirmations with PDF attachments

### Technical Features
- **Security** - Role-based authentication with encrypted passwords
- **Database** - MongoDB for flexible data storage
- **Email Service** - Gmail SMTP integration for automated communications
- **Modern UI** - Professional Swing interface with custom components and animations

---

## 🛠 Technology Stack

- **Java 17+** - Core programming language
- **Maven** - Dependency management and build automation
- **Java Swing** - Desktop UI framework
- **JavaFX 17.0.2** - WebView for payment integration
- **MongoDB 4.11.1** - NoSQL database
- **PayHere API** - Payment gateway integration
- **Apache PDFBox 2.0.29** - PDF generation
- **Jakarta Mail** - Email service (Gmail SMTP)
- **JCalendar 1.4** - Date picker component
- **Dotenv Java 3.0.0** - Environment configuration

---

## 📦 Installation

### Prerequisites

- **Java JDK 17+** - [Download here](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** - [Download here](https://maven.apache.org/download.cgi)
- **MongoDB** - [Download here](https://www.mongodb.com/try/download/community)


### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/hotel-management-system.git
   cd hotel-management-system
   ```

2. **Configure environment variables**  
   Create a `.env` file in the project root

3. **Install dependencies**
   ```bash
   mvn clean install
   ```

4. **Create initial users**
   ```bash
   mvn exec:java -Dexec.mainClass="com.hotel.management.util.UserCreationScript"
   ```

5. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.hotel.management.Main"
   ```
---

## ⚙️ Configuration

### Database Collections

The application automatically creates these MongoDB collections:
- `users` - User authentication and roles
- `rooms` - Room inventory and status
- `guests` - Guest profiles and information
- `bookings` - Booking records and history
- `staff` - Staff member details
- `financialRecords` - Revenue and payment tracking
- `tickets` - Support ticket system

### PayHere Setup

1. Sign up at [PayHere](https://www.payhere.lk/)
2. Get your Merchant ID and Secret from the dashboard
3. Set `PAYHERE_SANDBOX_MODE=true` for testing
4. Set `PAYHERE_SANDBOX_MODE=false` for production

### Email Setup

1. Enable 2-Step Verification in your Google Account
2. Generate an [App Password](https://support.google.com/accounts/answer/185833) for Mail
3. Use the app password in your `.env` file

---

## 📖 Usage

### Login

1. Run the application and select your role (Admin or Receptionist)
2. Enter your credentials created during the setup

### Admin Functions

- **Manage Rooms**: Add rooms, adjust pricing, monitor status
- **Manage Staff**: Add/remove staff, assign cleaning tasks
- **View Reports**: Generate financial reports and export as PDF
- **Monitor Activity**: Track guest check-ins/check-outs

### Receptionist Functions

- **Check-In**: Select room, enter guest details, process payment
- **Check-Out**: Review billing, complete checkout, email invoice
- **Room Status**: View availability, update room status
- **Support Tickets**: Create tickets for guest issues

---

## 🙏 Acknowledgments

- MongoDB for the flexible database solution
- PayHere for payment gateway integration
- Apache PDFBox for PDF generation
- Open Source Community for continuous support

## 📮 Support

**📧 Email:** [k.b.ravindusankalpaac@gmail.com](mailto:k.b.ravindusankalpaac@gmail.com)  
**🐞 Bug Reports:** [GitHub Issues](https://github.com/K-B-R-S-W/Hotel_Management_System/issues)   
**💭 Discussions:** [GitHub Discussions](https://github.com/K-B-R-S-W/Hotel_Management_System/discussions)  

## ⭐ Support This Project

If you find this project helpful, please give it a **⭐ star** on GitHub — it motivates me to keep improving! 🚀