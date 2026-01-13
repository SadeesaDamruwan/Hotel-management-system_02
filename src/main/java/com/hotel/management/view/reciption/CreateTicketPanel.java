package com.hotel.management.view.receptionist;

import com.hotel.management.model.Booking;
import com.hotel.management.model.Ticket;
import com.hotel.management.service.GuestBookingService;
import com.hotel.management.service.TicketService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class CreateTicketPanel extends JPanel {
    
    private TicketService ticketService;
    private GuestBookingService bookingService;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> priorityCombo;
    private JTextField roomNumberField;
    private JTextField guestNameField;
    
    private final Color BG_COLOR = new Color(30, 30, 35);
    private final Color CARD_BG = new Color(40, 40, 45, 230);
    private final Color BUTTON_COLOR = new Color(46, 204, 113);
    private final Color URGENT_COLOR = new Color(231, 76, 60);
    
    public CreateTicketPanel() {
        this.ticketService = new TicketService();
        this.bookingService = new GuestBookingService();
        
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Create Support Ticket");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        mainContainer.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = createFormPanel();
        mainContainer.add(formPanel, BorderLayout.CENTER);
        
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        int row = 0;
        
        addFormLabel(formPanel, "Ticket Title *", gbc, 0, row);
        titleField = new JTextField(30);
        styleTextField(titleField);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(titleField, gbc);
        
        addFormLabel(formPanel, "Description *", gbc, 0, row);
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setBackground(new Color(50, 50, 55));
        descriptionArea.setForeground(Color.WHITE);
        descriptionArea.setCaretColor(Color.WHITE);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 75)),
                new EmptyBorder(8, 10, 8, 10)));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.gridheight = 2;
        formPanel.add(scrollPane, gbc);
        gbc.gridheight = 1;
        row++;
        
        addFormLabel(formPanel, "Category *", gbc, 0, row);
        categoryCombo = new JComboBox<>(new String[]{
                "Select Category",
                "Guest Issue",
                "Maintenance Request",
                "Billing Issue",
                "Emergency",
                "Other"
        });
        styleComboBox(categoryCombo);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(categoryCombo, gbc);
        
        addFormLabel(formPanel, "Priority *", gbc, 0, row);
        priorityCombo = new JComboBox<>(new String[]{
                "Select Priority",
                "Low",
                "Medium",
                "High",
                "Urgent"
        });
        styleComboBox(priorityCombo);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(priorityCombo, gbc);
        
        addFormLabel(formPanel, "Room Number", gbc, 0, row);
        roomNumberField = new JTextField(30);
        styleTextField(roomNumberField);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(roomNumberField, gbc);
        
        addFormLabel(formPanel, "Guest Name", gbc, 0, row);
        guestNameField = new JTextField(30);
        guestNameField.setEditable(false);
        guestNameField.setBackground(new Color(40, 40, 45));
        styleTextField(guestNameField);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(guestNameField, gbc);
        
        roomNumberField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fetchGuestNameByRoom();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fetchGuestNameByRoom();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fetchGuestNameByRoom();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setOpaque(false);
        
        JButton submitBtn = createStyledButton("Submit Ticket", BUTTON_COLOR);
        submitBtn.addActionListener(e -> handleSubmit());
        
        JButton clearBtn = createStyledButton("Clear Form", new Color(231, 76, 60));
        clearBtn.addActionListener(e -> clearForm());
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(clearBtn);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private void addFormLabel(JPanel panel, String text, GridBagConstraints gbc, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(label, gbc);
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(50, 50, 55));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 75)),
                new EmptyBorder(8, 10, 8, 10)));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(new Color(50, 50, 55));
        combo.setForeground(Color.WHITE);
        combo.setPreferredSize(new Dimension(300, 35));
    }
    
    private void handleSubmit() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        String priority = (String) priorityCombo.getSelectedItem();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a ticket title!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (category.equals("Select Category")) {
            JOptionPane.showMessageDialog(this, "Please select a category!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (priority.equals("Select Priority")) {
            JOptionPane.showMessageDialog(this, "Please select a priority!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCategory(category);
        ticket.setPriority(priority);
        ticket.setRoomNumber(roomNumberField.getText().trim());
        ticket.setGuestName(guestNameField.getText().trim());
        ticket.setCreatedBy("Receptionist"); 
        
        boolean success = ticketService.createTicket(ticket);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Ticket created successfully!\nTicket ID: " + ticket.getTicketId(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create ticket. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fetchGuestNameByRoom() {
        String roomNumber = roomNumberField.getText().trim();
        
        if (roomNumber.isEmpty()) {
            guestNameField.setText("");
            return;
        }
        
        List<Booking> bookings = bookingService.findBookingsByRoom(roomNumber);
        
        if (bookings != null && !bookings.isEmpty()) {
            Booking currentBooking = null;
            for (Booking booking : bookings) {
                if ("CHECKED_IN".equals(booking.getBookingStatus()) || 
                    "CONFIRMED".equals(booking.getBookingStatus())) {
                    currentBooking = booking;
                    break;
                }
            }
            
            if (currentBooking == null && !bookings.isEmpty()) {
                currentBooking = bookings.get(bookings.size() - 1);
            }
            
            if (currentBooking != null) {
                guestNameField.setText(currentBooking.getGuestName());
                guestNameField.setForeground(new Color(46, 204, 113)); 
            } else {
                guestNameField.setText("");
            }
        } else {
            guestNameField.setText("");
        }
    }
    
    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        categoryCombo.setSelectedIndex(0);
        priorityCombo.setSelectedIndex(0);
        roomNumberField.setText("");
        guestNameField.setText("");
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }
}
