package com.hotel.management.view.receptionist;

import com.hotel.management.service.RoomService;
import com.hotel.management.service.PayHereService;
import com.hotel.management.service.EmailService;
import com.hotel.management.service.GuestBookingService;
import com.hotel.management.model.Room;
import com.hotel.management.model.Booking;
import com.hotel.management.util.ReceiptPDFGenerator;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.io.File;

public class AddGuestPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);

    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    
    private JTextField nameField;
    private JDateChooser dobChooser;
    private JTextField nicField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    
    
    private JComboBox<String> roomTypeCombo;
    private JTextField priceField;
    private JComboBox<String> roomNumberCombo;
    private JDateChooser checkInDateChooser;
    private JDateChooser checkOutDateChooser;
    private JTextField nightsField;
    private JTextField totalPriceField;
    
    private final RoomService roomService;
    private final PayHereService payHereService;
    private Map<String, Double> roomTypePrices;
    private Map<String, List<String>> availableRoomsByType;

    public AddGuestPanel() {
        this.roomService = new RoomService();
        this.payHereService = new PayHereService();
        this.roomTypePrices = new HashMap<>();
        this.availableRoomsByType = new HashMap<>();
        
        setOpaque(false);
        setLayout(new GridBagLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        
        cardPanel.add(createStep1GuestDetails(), "Step1");
        cardPanel.add(createStep2RoomSelection(), "Step2");

        add(cardPanel);
    }

    
    private JPanel createStep1GuestDetails() {
        JPanel stepPanel = new JPanel(new GridBagLayout());
        stepPanel.setOpaque(false);

        JPanel formContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FORM_BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel titleLabel = new JLabel("Guest Information");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 20, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.gridwidth = 1;

        
        gbc.gridy++;
        addLabel(formContainer, "Full Name *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        nameField = addTextField(formContainer, "Enter guest full name", gbc, 0, gbc.gridy, 2);

        
        gbc.gridy++;
        addLabel(formContainer, "Date of Birth *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        dobChooser = addDateChooser(formContainer, gbc, 0, gbc.gridy, 2);

        
        gbc.gridy++;
        addLabel(formContainer, "NIC / Passport Number *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        nicField = addTextField(formContainer, "Enter NIC or Passport number", gbc, 0, gbc.gridy, 2);

        
        gbc.gridy++;
        addLabel(formContainer, "Email Address *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        emailField = addTextField(formContainer, "Enter email address", gbc, 0, gbc.gridy, 2);

        
        gbc.gridy++;
        addLabel(formContainer, "Phone Number *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        phoneField = addTextField(formContainer, "Enter phone number", gbc, 0, gbc.gridy, 2);

        
        gbc.gridy++;
        addLabel(formContainer, "Address *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        addressField = addTextField(formContainer, "Enter address", gbc, 0, gbc.gridy, 2);

    
        gbc.gridy++;
        gbc.insets = new Insets(30, 15, 5, 15);
        JButton nextButton = createStyledButton("Next - Select Room");
        nextButton.addActionListener(e -> validateAndProceedToStep2());
        gbc.gridwidth = 2;
        formContainer.add(nextButton, gbc);

        
        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);

        stepPanel.add(formContainer);
        return stepPanel;
    }

    
    private JPanel createStep2RoomSelection() {
        JPanel stepPanel = new JPanel(new GridBagLayout());
        stepPanel.setOpaque(false);

        JPanel formContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FORM_BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        formContainer.setOpaque(false);
        formContainer.setBorder(new EmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 3, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel titleLabel = new JLabel("Room Selection");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 10, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(3, 15, 2, 15);
        gbc.gridwidth = 1;

        
        gbc.gridy++;
        addLabel(formContainer, "Room Type *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        
       
        loadRoomTypesAndPrices();
        
        String[] roomTypes = roomTypePrices.keySet().toArray(new String[0]);
        if (roomTypes.length == 0) {
            roomTypes = new String[]{"No rooms available"};
        }
        
        roomTypeCombo = addComboBox(formContainer, roomTypes, 
            roomTypes.length > 0 ? roomTypes[0] : "No rooms available", gbc, 0, gbc.gridy, 2);
        roomTypeCombo.addActionListener(e -> updatePriceAndRooms());

        
        gbc.gridy++;
        addLabel(formContainer, "Available Room Number *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        roomNumberCombo = addComboBox(formContainer, new String[]{"Select room type first"}, 
            "Select room type first", gbc, 0, gbc.gridy, 2);

        
        gbc.gridy++;
        addLabel(formContainer, "Check-in Date *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        checkInDateChooser = addDateChooser(formContainer, gbc, 0, gbc.gridy, 2);
        checkInDateChooser.setMinSelectableDate(new java.util.Date()); // Cannot select past dates
        checkInDateChooser.addPropertyChangeListener("date", e -> {
            calculateNightsAndTotal();
            updateCheckoutMinDate();
        });

        gbc.gridy++;
        addLabel(formContainer, "Check-out Date *", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        checkOutDateChooser = addDateChooser(formContainer, gbc, 0, gbc.gridy, 2);
        checkOutDateChooser.setMinSelectableDate(new java.util.Date()); // Cannot select past dates
        checkOutDateChooser.addPropertyChangeListener("date", e -> calculateNightsAndTotal());

        
        gbc.gridy++;
        addLabel(formContainer, "Number of Nights", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        nightsField = addTextField(formContainer, "0", gbc, 0, gbc.gridy, 2);
        nightsField.setEditable(false);
        nightsField.setFocusable(false);

        
        gbc.gridy++;
        addLabel(formContainer, "Room Price (per night)", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        priceField = addTextField(formContainer, "Rs. 0.00", gbc, 0, gbc.gridy, 2);
        priceField.setEditable(false);
        priceField.setFocusable(false);

        
        gbc.gridy++;
        addLabel(formContainer, "Total Price", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        totalPriceField = addTextField(formContainer, "Rs. 0.00", gbc, 0, gbc.gridy, 2);
        totalPriceField.setEditable(false);
        totalPriceField.setFocusable(false);
        totalPriceField.setFont(new Font("Segoe UI", Font.BOLD, 16));

        updatePriceAndRooms();

        
        gbc.gridy++;
        gbc.insets = new Insets(15, 15, 5, 15);
        gbc.gridwidth = 2;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton backButton = createStyledButton("← Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Step1"));
        buttonPanel.add(backButton);
        
        JButton confirmButton = createStyledButton("Payment →");
        confirmButton.addActionListener(e -> handleConfirmBooking());
        buttonPanel.add(confirmButton);
        
        formContainer.add(buttonPanel, gbc);

        
        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);

        stepPanel.add(formContainer);
        return stepPanel;
    }
    
    private void updatePriceAndRooms() {
        String selectedType = (String) roomTypeCombo.getSelectedItem();
        
        if (selectedType == null || selectedType.equals("No rooms available")) {
            priceField.setText("Rs. 0.00");
            totalPriceField.setText("Rs. 0.00");
            roomNumberCombo.removeAllItems();
            roomNumberCombo.addItem("No rooms available");
            return;
        }
        
        
        Double price = roomTypePrices.get(selectedType);
        if (price != null) {
            priceField.setText(String.format("Rs. %.2f", price));
        } else {
            priceField.setText("Rs. 0.00");
        }
        
       
        roomNumberCombo.removeAllItems();
        List<String> availableRooms = availableRoomsByType.get(selectedType);
        
        if (availableRooms != null && !availableRooms.isEmpty()) {
            for (String roomNum : availableRooms) {
                roomNumberCombo.addItem(roomNum);
            }
        } else {
            roomNumberCombo.addItem("No rooms available");
        }
        
        
        calculateNightsAndTotal();
    }
    
    private void calculateNightsAndTotal() {
        if (checkInDateChooser.getDate() == null || checkOutDateChooser.getDate() == null) {
            nightsField.setText("0");
            totalPriceField.setText("Rs. 0.00");
            return;
        }
        
        
        long checkInTime = checkInDateChooser.getDate().getTime();
        long checkOutTime = checkOutDateChooser.getDate().getTime();
        long diffInMillis = checkOutTime - checkInTime;
        long nights = diffInMillis / (1000 * 60 * 60 * 24);
        
        if (nights < 0) {
            nightsField.setText("Invalid");
            totalPriceField.setText("Rs. 0.00");
            return;
        }
        
        if (nights == 0) {
            nights = 1; 
        }
        
        nightsField.setText(String.valueOf(nights));
        
        
        String selectedType = (String) roomTypeCombo.getSelectedItem();
        if (selectedType != null && !selectedType.equals("No rooms available")) {
            Double pricePerNight = roomTypePrices.get(selectedType);
            if (pricePerNight != null) {
                double totalPrice = pricePerNight * nights;
                totalPriceField.setText(String.format("Rs. %.2f", totalPrice));
            } else {
                totalPriceField.setText("Rs. 0.00");
            }
        } else {
            totalPriceField.setText("Rs. 0.00");
        }
    }

    private void validateAndProceedToStep2() {
       
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter guest name", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (dobChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select date of birth", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (nicField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter NIC/Passport number", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email address", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        if (!isValidEmail(emailField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter phone number", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        if (!isValidPhoneNumber(phoneField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number (numbers only)", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (addressField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter address", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        loadRoomTypesAndPrices();
        updatePriceAndRooms();
        
        
        cardLayout.show(cardPanel, "Step2");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    private boolean isValidPhoneNumber(String phone) {
        
        String phoneRegex = "^[0-9\\s\\-+()]+$";
        
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        return phone.matches(phoneRegex) && digitsOnly.length() >= 7;
    }
    
    private void updateCheckoutMinDate() {
        if (checkInDateChooser.getDate() != null) {
            
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(checkInDateChooser.getDate());
            cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
            checkOutDateChooser.setMinSelectableDate(cal.getTime());
        }
    }
    
    private void addSummarySection(JPanel parent, String title, String[][] data) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setOpaque(false);
        
       
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sectionTitle.setForeground(GOLD_COLOR);
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sectionPanel.add(sectionTitle);
        
        sectionPanel.add(Box.createVerticalStrut(8));
        
        
        for (String[] row : data) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            rowPanel.setOpaque(false);
            rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
            
            JLabel keyLabel = new JLabel(row[0]);
            keyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            keyLabel.setForeground(new Color(180, 180, 180));
            
            JLabel valueLabel = new JLabel(row[1]);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            valueLabel.setForeground(Color.WHITE);
            
            rowPanel.add(keyLabel);
            rowPanel.add(valueLabel);
            
            sectionPanel.add(rowPanel);
        }
        
        parent.add(sectionPanel);
    }
    
    private void clearForm() {
        nameField.setText("");
        dobChooser.setDate(null);
        nicField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        checkInDateChooser.setDate(null);
        checkOutDateChooser.setDate(null);
        nightsField.setText("0");
        totalPriceField.setText("Rs. 0.00");
    }
    
    private void loadRoomTypesAndPrices() {
        roomTypePrices.clear();
        availableRoomsByType.clear();
        
    
        List<Room> allRooms = roomService.getAllRooms();
        
        for (Room room : allRooms) {
            if ("Available".equals(room.getStatus())) {
                String roomType = room.getRoomType();
                double price = room.getPrice();
                
                roomTypePrices.putIfAbsent(roomType, price);
                
                availableRoomsByType.putIfAbsent(roomType, new java.util.ArrayList<>());
                availableRoomsByType.get(roomType).add(room.getRoomNumber());
            }
        }
    }

    private void handleConfirmBooking() {
        String selectedRoom = (String) roomNumberCombo.getSelectedItem();
        
        if (selectedRoom == null || selectedRoom.equals("No rooms available") || selectedRoom.equals("Select room type first")) {
            JOptionPane.showMessageDialog(this, "Please select a valid room", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (checkInDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select check-in date", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (checkOutDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select check-out date", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (nightsField.getText().equals("Invalid") || nightsField.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "Please select valid dates (check-out must be after check-in)", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        showPaymentMethodDialog();
    }
    
    private void showPaymentMethodDialog() {
        JDialog paymentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Payment Method", true);
        paymentDialog.setLayout(new BorderLayout());
        paymentDialog.setSize(500, 300);
        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setUndecorated(true);
        
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(20, 20, 20),
                    0, getHeight(), new Color(40, 40, 40)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setColor(GOLD_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        
        JLabel titleLabel = new JLabel("Select Payment Method");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        JLabel amountLabel = new JLabel("Total Amount: " + totalPriceField.getText());
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 20, 10);
        mainPanel.add(amountLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JButton cashButton = createPaymentMethodButton("Cash Payment", true);
        cashButton.addActionListener(e -> {
            paymentDialog.dispose();
            handleCashPayment();
        });
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(cashButton, gbc);
        
        JButton cardButton = createPaymentMethodButton("Card Payment", false);
        cardButton.addActionListener(e -> {
            paymentDialog.dispose();
            handleCardPayment();
        });
        gbc.gridx = 1;
        mainPanel.add(cardButton, gbc);
        
        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> paymentDialog.dispose());
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(cancelButton, gbc);
        
        paymentDialog.add(mainPanel);
        paymentDialog.setVisible(true);
    }
    
    private JButton createPaymentMethodButton(String text, boolean isCash) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(200, 150, 50));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 200, 80));
                } else {
                    g2.setColor(GOLD_COLOR);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                int iconSize = 28;
                int iconX = 18;
                int iconY = (getHeight() - iconSize) / 2;
                
                if (isCash) {
                    
                    g2.setColor(new Color(40, 100, 40)); 
                    g2.fillRoundRect(iconX, iconY + 4, iconSize, iconSize - 8, 3, 3);
                    
                    g2.setColor(GOLD_COLOR);
                    g2.setFont(new Font("Arial", Font.BOLD, 14));
                    FontMetrics fm = g2.getFontMetrics();
                    String dollar = "$";
                    int textX = iconX + (iconSize - fm.stringWidth(dollar)) / 2;
                    int textY = iconY + (iconSize + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(dollar, textX, textY);
                    
                    g2.setColor(new Color(80, 150, 80));
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(iconX, iconY + 4, iconSize, iconSize - 8, 3, 3);
                } else {
                    
                    g2.setColor(new Color(50, 50, 80)); 
                    g2.fillRoundRect(iconX, iconY + 4, iconSize, iconSize - 8, 3, 3);
                    
                    g2.setColor(new Color(30, 30, 30));
                    g2.fillRect(iconX + 2, iconY + 8, iconSize - 4, 4);
                    
                    g2.setColor(new Color(200, 180, 100));
                    g2.fillRoundRect(iconX + 4, iconY + 14, 6, 5, 2, 2);
                    
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawLine(iconX + 12, iconY + 16, iconX + 17, iconY + 16);
                    g2.drawLine(iconX + 19, iconY + 16, iconX + 24, iconY + 16);
                    
                    g2.setColor(new Color(100, 100, 150));
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(iconX, iconY + 4, iconSize, iconSize - 8, 3, 3);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(180, 50));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 55, 0, 10));

        return button;
    }
    
    private void handleCashPayment() {
        String selectedRoom = (String) roomNumberCombo.getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        JDialog confirmDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Confirm Cash Payment", true);
        confirmDialog.setLayout(new BorderLayout());
        confirmDialog.setSize(550, 550);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setUndecorated(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(20, 20, 20),
                    0, getHeight(), new Color(40, 40, 40)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setColor(GOLD_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("BOOKING SUMMARY");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel subtitleLabel = new JLabel("Please review the booking details");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(180, 180, 180));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        addSummarySection(contentPanel, "GUEST INFORMATION", new String[][]{
            {"Name:", nameField.getText()},
            {"Email:", emailField.getText()},
            {"Phone:", phoneField.getText()}
        });
        
        contentPanel.add(Box.createVerticalStrut(15));
        
        addSummarySection(contentPanel, "ROOM DETAILS", new String[][]{
            {"Room Number:", selectedRoom},
            {"Room Type:", (String) roomTypeCombo.getSelectedItem()}
        });
        
        contentPanel.add(Box.createVerticalStrut(15));
        
        addSummarySection(contentPanel, "BOOKING PERIOD", new String[][]{
            {"Check-in:", sdf.format(checkInDateChooser.getDate())},
            {"Check-out:", sdf.format(checkOutDateChooser.getDate())},
            {"Number of Nights:", nightsField.getText()}
        });
        
        contentPanel.add(Box.createVerticalStrut(15));
        
        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.setOpaque(false);
        paymentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD_COLOR, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel paymentLabel = new JLabel("TOTAL AMOUNT");
        paymentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        paymentLabel.setForeground(GOLD_COLOR);
        paymentPanel.add(paymentLabel, BorderLayout.WEST);
        
        JLabel amountLabel = new JLabel(totalPriceField.getText());
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        amountLabel.setForeground(GOLD_COLOR);
        paymentPanel.add(amountLabel, BorderLayout.EAST);
        
        contentPanel.add(paymentPanel);
        
        contentPanel.add(Box.createVerticalStrut(10));
        
        JLabel paymentMethodLabel = new JLabel("Payment Method: CASH");
        paymentMethodLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        paymentMethodLabel.setForeground(new Color(200, 200, 200));
        paymentMethodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(paymentMethodLabel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        JButton confirmButton = createStyledButton("Confirm & Receipt");
        confirmButton.setPreferredSize(new Dimension(180, 40));
        confirmButton.addActionListener(e -> {
            confirmDialog.dispose();
            completeCashPayment();
        });
        
        JButton cancelButton = createStyledButton("Cancel");
        cancelButton.addActionListener(e -> confirmDialog.dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        confirmDialog.add(mainPanel);
        confirmDialog.setVisible(true);
    }
    
    private void completeCashPayment() {
        try {
            
            String guestName = nameField.getText();
            String guestEmail = emailField.getText();
            String guestPhone = phoneField.getText();
            String roomType = (String) roomTypeCombo.getSelectedItem();
            String roomNumber = (String) roomNumberCombo.getSelectedItem();
            Date checkIn = checkInDateChooser.getDate();
            Date checkOut = checkOutDateChooser.getDate();
            int nights = Integer.parseInt(nightsField.getText());
            double totalAmount = Double.parseDouble(totalPriceField.getText().replace("Rs. ", "").replace(",", ""));
            
            
            File receiptFile = ReceiptPDFGenerator.generateReceipt(
                guestName,
                guestEmail,
                guestPhone,
                roomType,
                roomNumber,
                checkIn,
                checkOut,
                nights,
                totalAmount,
                "CASH"
            );
            
            String finalReceiptPath = receiptFile.getAbsolutePath();
            String finalGuestEmail = guestEmail;
            String finalGuestName = guestName;
            String finalRoomType = roomType;
            String finalRoomNumber = roomNumber;
            Date finalCheckIn = checkIn;
            Date finalCheckOut = checkOut;
            int finalNights = nights;
            double finalTotalAmount = totalAmount;
            
            new Thread(() -> {
                try {
                    
                    Thread.sleep(500);
                    
                    File pdfFile = new File(finalReceiptPath);
                    
                    if (pdfFile.exists()) {
                        EmailService.sendBookingConfirmation(
                            finalGuestEmail,
                            finalGuestName,
                            finalRoomType,
                            finalRoomNumber,
                            finalCheckIn,
                            finalCheckOut,
                            finalNights,
                            finalTotalAmount,
                            "Cash Payment",
                            pdfFile
                        );
                    } else {
                        System.err.println("PDF file not found: " + finalReceiptPath);
                    }
                } catch (Exception e) {
                    System.err.println("Error sending email: " + e.getMessage());
                    e.printStackTrace();
                }
            }).start();
            
            try {
                Booking booking = new Booking(
                    guestName,
                    guestEmail,
                    guestPhone,
                    addressField.getText(),
                    nicField.getText(),
                    checkIn,
                    checkOut,
                    nights,
                    roomType,
                    roomNumber,
                    totalAmount / nights,
                    totalAmount,
                    "CASH"
                );
                
                GuestBookingService bookingService = new GuestBookingService();
                String bookingId = bookingService.saveBooking(booking);
                
                if (bookingId != null) {
                    System.out.println("✓ Booking saved with ID: " + bookingId);
                    
                    RoomService roomService = new RoomService();
                    boolean roomUpdated = roomService.updateRoomStatus(roomNumber, "Occupied");
                    if (roomUpdated) {
                        System.out.println("✓ Room " + roomNumber + " marked as Occupied");
                    }
                } else {
                    System.err.println("✗ Failed to save booking to database");
                }
            } catch (Exception dbEx) {
                System.err.println("Database error: " + dbEx.getMessage());
                dbEx.printStackTrace();
            }
            
            JOptionPane.showMessageDialog(this,
                "Cash payment confirmed!\n\n" +
                "Booking has been saved successfully.\n" +
                "Receipt saved to: " + receiptFile.getAbsolutePath() + "\n\n" +
                "✓ Confirmation email is being sent to: " + guestEmail,
                "Payment Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            cardLayout.show(cardPanel, "Step1");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error generating receipt: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleCardPayment() {
        try {
            
            String orderId = PayHereService.generateOrderId();
            
            String selectedRoom = (String) roomNumberCombo.getSelectedItem();
            String roomType = (String) roomTypeCombo.getSelectedItem();
            int nights = Integer.parseInt(nightsField.getText());
            
            String totalStr = totalPriceField.getText().replace("Rs. ", "").replace(",", "");
            double totalAmount = Double.parseDouble(totalStr);
            
            Map<String, String> paymentData = payHereService.createPaymentData(
                orderId,
                nameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                addressField.getText(),
                roomType,
                selectedRoom,
                nights,
                totalAmount
            );
            
            String paymentHtml = payHereService.generatePaymentForm(paymentData);
            
            System.gc();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            PaymentWebViewDialog paymentDialog = new PaymentWebViewDialog(parentFrame, paymentHtml);
            
            System.out.println("=== Opening Payment Dialog ===");
            System.out.println("Order ID: " + orderId);
            System.out.println("Payment HTML length: " + paymentHtml.length());
            System.out.println("============================");
            
            paymentDialog.setVisible(true);
            
            if (paymentDialog.isPaymentSuccessful()) {
                String paymentId = paymentDialog.getPaymentId();
                
                try {
                    String guestName = nameField.getText();
                    String guestEmail = emailField.getText();
                    Date checkIn = checkInDateChooser.getDate();
                    Date checkOut = checkOutDateChooser.getDate();
                    String paymentMethodStr = "CARD - PayHere Payment\nOrder ID: " + orderId + "\nPayment ID: #" + paymentId;
                    
                    File receiptFile = ReceiptPDFGenerator.generateReceipt(
                        guestName,
                        guestEmail,
                        phoneField.getText(),
                        roomType,
                        selectedRoom,
                        checkIn,
                        checkOut,
                        nights,
                        totalAmount,
                        paymentMethodStr
                    );
                    
                    String finalReceiptPath = receiptFile.getAbsolutePath();
                    String finalGuestEmail = guestEmail;
                    String finalGuestName = guestName;
                    String finalRoomType = roomType;
                    String finalSelectedRoom = selectedRoom;
                    Date finalCheckIn = checkIn;
                    Date finalCheckOut = checkOut;
                    int finalNights = nights;
                    double finalTotalAmount = totalAmount;
                    String finalOrderId = orderId;
                    String finalPaymentId = paymentId;
                    
                    new Thread(() -> {
                        try {
                            
                            Thread.sleep(500);
                            
                            File pdfFile = new File(finalReceiptPath);
                            
                            if (pdfFile.exists()) {
                                EmailService.sendBookingConfirmation(
                                    finalGuestEmail,
                                    finalGuestName,
                                    finalRoomType,
                                    finalSelectedRoom,
                                    finalCheckIn,
                                    finalCheckOut,
                                    finalNights,
                                    finalTotalAmount,
                                    "Card Payment (PayHere)\nOrder ID: " + finalOrderId + "\nPayment ID: #" + finalPaymentId,
                                    pdfFile
                                );
                            } else {
                                System.err.println("PDF file not found: " + finalReceiptPath);
                            }
                        } catch (Exception e) {
                            System.err.println("Error sending email: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }).start();
                    
                    JOptionPane.showMessageDialog(this,
                        "═══════════════════════════════\n" +
                        "         PAYMENT SUCCESSFUL\n" +
                        "═══════════════════════════════\n\n" +
                        "Payment ID: #" + paymentId + "\n\n" +
                        "Your booking has been confirmed!\n\n" +
                        "Receipt saved to:\n" +
                        receiptFile.getName() + "\n\n" +
                        "✓ Confirmation email is being sent to: " + guestEmail,
                        "Payment Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                } catch (Exception e) {
                    System.err.println("Error generating receipt: " + e.getMessage());
                    e.printStackTrace();
                    
                    JOptionPane.showMessageDialog(this,
                        "═══════════════════════════════\n" +
                        "         PAYMENT SUCCESSFUL\n" +
                        "═══════════════════════════════\n\n" +
                        "Payment ID: #" + paymentId + "\n\n" +
                        "Your booking has been confirmed!",
                        "Payment Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
                completeCardPayment(orderId, paymentId);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Payment was not completed.\n\n" +
                    "You can try again or choose cash payment.",
                    "Payment Cancelled",
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error processing payment: " + e.getMessage(),
                "Payment Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void completeCardPayment(String orderId, String paymentId) {

        try {
            String guestName = nameField.getText();
            String guestEmail = emailField.getText();
            String guestPhone = phoneField.getText();
            String roomType = (String) roomTypeCombo.getSelectedItem();
            String roomNumber = (String) roomNumberCombo.getSelectedItem();
            Date checkIn = checkInDateChooser.getDate();
            Date checkOut = checkOutDateChooser.getDate();
            int nights = Integer.parseInt(nightsField.getText());
            double totalAmount = Double.parseDouble(totalPriceField.getText().replace("Rs. ", "").replace(",", ""));
            
            Booking booking = new Booking(
                guestName,
                guestEmail,
                guestPhone,
                addressField.getText(),
                nicField.getText(),
                checkIn,
                checkOut,
                nights,
                roomType,
                roomNumber,
                totalAmount / nights,
                totalAmount,
                "CARD"
            );
            
            booking.setOrderId(orderId);
            booking.setPaymentId(paymentId);
            
            GuestBookingService bookingService = new GuestBookingService();
            String bookingId = bookingService.saveBooking(booking);
            
            if (bookingId != null) {
                System.out.println("✓ Booking saved with ID: " + bookingId);
                
                RoomService roomService = new RoomService();
                boolean roomUpdated = roomService.updateRoomStatus(roomNumber, "Occupied");
                if (roomUpdated) {
                    System.out.println("✓ Room " + roomNumber + " marked as Occupied");
                }
            } else {
                System.err.println("✗ Failed to save booking to database");
            }
        } catch (Exception dbEx) {
            System.err.println("Database error: " + dbEx.getMessage());
            dbEx.printStackTrace();
        }
        
        clearForm();
        cardLayout.show(cardPanel, "Step1");
    }


    private void addLabel(JPanel parent, String text, GridBagConstraints gbc, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(LABEL_COLOR);

        GridBagConstraints labelGbc = (GridBagConstraints) gbc.clone();
        labelGbc.gridx = x;
        labelGbc.gridy = y;
        labelGbc.gridwidth = width;
        labelGbc.anchor = GridBagConstraints.WEST;
        parent.add(label, labelGbc);
    }

    private JTextField addTextField(JPanel parent, String placeholder, GridBagConstraints gbc, int x, int y, int width) {
        JTextField textField = new JTextField();
        textField.setOpaque(false);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(GOLD_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(textField, fieldGbc);
        return textField;
    }

    private JDateChooser addDateChooser(JPanel parent, GridBagConstraints gbc, int x, int y, int width) {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.getJCalendar().setWeekOfYearVisible(false);
        dateChooser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(dateChooser, fieldGbc);
        return dateChooser;
    }

    private JComboBox<String> addComboBox(JPanel parent, String[] items, String defaultItem, GridBagConstraints gbc, int x, int y, int width) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setSelectedItem(defaultItem);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(Color.BLACK);
        comboBox.setOpaque(false);
        comboBox.setBackground(new Color(0, 0, 0, 0));

        styleComboBox(comboBox);

        GridBagConstraints comboGbc = (GridBagConstraints) gbc.clone();
        comboGbc.gridx = x;
        comboGbc.gridy = y;
        comboGbc.gridwidth = width;
        parent.add(comboBox, comboGbc);
        return comboBox;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton();
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setContentAreaFilled(false);
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                btn.setIcon(new Icon() {
                    @Override
                    public void paintIcon(Component c, Graphics g, int x, int y) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(GOLD_COLOR);
                        int w = 8;
                        int h = 5;
                        int mx = c.getWidth() / 2 - w / 2;
                        int my = c.getHeight() / 2 - h / 2;
                        int[] xPoints = {mx, mx + w, mx + w / 2};
                        int[] yPoints = {my, my, my + h};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }

                    @Override
                    public int getIconWidth() {
                        return 10;
                    }

                    @Override
                    public int getIconHeight() {
                        return 10;
                    }
                });
                return btn;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = super.createScroller();
                        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                        return scroller;
                    }
                };
                popup.setBorder(BorderFactory.createLineBorder(GOLD_COLOR, 1));
                return popup;
            }
        });

        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (index == -1) {
                    setOpaque(true);
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                } else {
                    setOpaque(true);
                    if (isSelected) {
                        setBackground(GOLD_COLOR);
                        setForeground(Color.BLACK);
                    } else {
                        setBackground(new Color(40, 40, 40));
                        setForeground(Color.WHITE);
                    }
                }
                setBorder(new EmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(200, 150, 50));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 200, 80));
                } else {
                    g2.setColor(GOLD_COLOR);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(200, 40));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }
}
