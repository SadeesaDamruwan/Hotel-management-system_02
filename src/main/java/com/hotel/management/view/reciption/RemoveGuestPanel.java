package com.hotel.management.view.receptionist;

import com.hotel.management.model.Booking;
import com.hotel.management.model.Room;
import com.hotel.management.service.GuestBookingService;
import com.hotel.management.service.RoomStatusScheduler;
import com.hotel.management.service.RoomService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveGuestPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);
    private final Color BUTTON_BG_COLOR = GOLD_COLOR;
    private final Color BUTTON_HOVER_COLOR = new Color(255, 200, 100);

    private JTextField guestNameField;
    private JTextField nicField;
    private JTextField phoneField;
    private JTextField roomNumberField;
    private JTextField nightCountField;
    private JTextField checkOutDateField;
    private JButton removeGuestButton;
    private JButton searchButton;
    private JButton clearButton;
    
    private JComboBox<String> roomTypeCombo;
    private JComboBox<String> roomNumberCombo;
    private List<Room> allRooms;
    private DefaultComboBoxModel<String> roomNumberModel;
    
    private Booking currentBooking; 
    private GuestBookingService bookingService;
    private RoomService roomService;

    public RemoveGuestPanel() {
        bookingService = new GuestBookingService();
        roomService = new RoomService();
        
        
        allRooms = roomService.getAllRooms();
        
        setLayout(new GridBagLayout());
        setOpaque(false);

        JPanel formPanel = createFormPanel();
        add(formPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FORM_BACKGROUND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));
        panel.setPreferredSize(new Dimension(700, 750));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel titleLabel = new JLabel("Remove Guest (Check-Out)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(titleLabel, gbc);

        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        
        addLabel(panel, "Room Type:", gbc, 0, 1, 1);
        roomTypeCombo = createStyledComboBox(new String[]{"All Types", "Standard Rooms", "Deluxe suite", "Family Room", "Penthouse"});
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(roomTypeCombo, gbc);
        
        
        addLabel(panel, "Room Number:", gbc, 0, 2, 1);
        roomNumberModel = new DefaultComboBoxModel<>();
        roomNumberCombo = new JComboBox<>(roomNumberModel);
        styleComboBox(roomNumberCombo);
        roomNumberCombo.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(roomNumberCombo, gbc);
        
        
        searchButton = createStyledButton("Search Guest");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(searchButton, gbc);

        
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(GOLD_COLOR.getRed(), GOLD_COLOR.getGreen(), GOLD_COLOR.getBlue(), 100));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 0, 20, 0);
        panel.add(separator, gbc);

        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        
        addLabel(panel, "Guest Name:", gbc, 0, 5, 1);
        guestNameField = addTextField(panel, "", gbc, 1, 5, 2);
        guestNameField.setEditable(false);

        
        addLabel(panel, "NIC:", gbc, 0, 6, 1);
        nicField = addTextField(panel, "", gbc, 1, 6, 2);
        nicField.setEditable(false);

        addLabel(panel, "Phone Number:", gbc, 0, 7, 1);
        phoneField = addTextField(panel, "", gbc, 1, 7, 2);
        phoneField.setEditable(false);

        addLabel(panel, "Room Number:", gbc, 0, 8, 1);
        roomNumberField = addTextField(panel, "", gbc, 1, 8, 2);
        roomNumberField.setEditable(false);

        
        addLabel(panel, "Number of Nights:", gbc, 0, 9, 1);
        nightCountField = addTextField(panel, "", gbc, 1, 9, 2);
        nightCountField.setEditable(false);

        
        addLabel(panel, "Check-Out Date:", gbc, 0, 10, 1);
        checkOutDateField = addTextField(panel, "", gbc, 1, 10, 2);
        checkOutDateField.setEditable(false);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        removeGuestButton = createStyledButton("Remove Guest");
        removeGuestButton.setEnabled(false);
        
        clearButton = createStyledButton("Clear");
        clearButton.setBackground(new Color(100, 100, 100));
        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (clearButton.isEnabled()) {
                    clearButton.setBackground(new Color(130, 130, 130));
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (clearButton.isEnabled()) {
                    clearButton.setBackground(new Color(100, 100, 100));
                }
            }
        });

        buttonPanel.add(removeGuestButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(30, 10, 10, 10);
        panel.add(buttonPanel, gbc);

    
        roomTypeCombo.addActionListener(e -> updateRoomNumberList());
        
        
        JTextField roomNumberEditor = (JTextField) roomNumberCombo.getEditor().getEditorComponent();
        roomNumberEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    filterRoomNumbers(roomNumberEditor.getText());
                });
            }
        });
        
        searchButton.addActionListener(e -> searchGuestByRoom());
        clearButton.addActionListener(e -> clearForm());
        removeGuestButton.addActionListener(e -> removeGuest());
        
        
        updateRoomNumberList();

        return panel;
    }
    
    private void updateRoomNumberList() {
        String selectedType = (String) roomTypeCombo.getSelectedItem();
        roomNumberModel.removeAllElements();
        
        List<String> roomNumbers;
        if (selectedType.equals("All Types")) {
            roomNumbers = allRooms.stream()
                    .map(Room::getRoomNumber)
                    .sorted()
                    .collect(Collectors.toList());
        } else {
            roomNumbers = allRooms.stream()
                    .filter(room -> room.getRoomType().equals(selectedType))
                    .map(Room::getRoomNumber)
                    .sorted()
                    .collect(Collectors.toList());
        }
        
        for (String roomNumber : roomNumbers) {
            roomNumberModel.addElement(roomNumber);
        }
        
        if (roomNumberModel.getSize() > 0) {
            roomNumberCombo.setSelectedIndex(-1);
        }
    }
    
    private void filterRoomNumbers(String filter) {
        String selectedType = (String) roomTypeCombo.getSelectedItem();
        String currentText = filter;
        
        roomNumberModel.removeAllElements();
        
        List<String> filteredRooms;
        if (selectedType.equals("All Types")) {
            filteredRooms = allRooms.stream()
                    .map(Room::getRoomNumber)
                    .filter(roomNum -> roomNum.contains(filter))
                    .sorted()
                    .collect(Collectors.toList());
        } else {
            filteredRooms = allRooms.stream()
                    .filter(room -> room.getRoomType().equals(selectedType))
                    .map(Room::getRoomNumber)
                    .filter(roomNum -> roomNum.contains(filter))
                    .sorted()
                    .collect(Collectors.toList());
        }
        
        for (String roomNumber : filteredRooms) {
            roomNumberModel.addElement(roomNumber);
        }
        
        
        JTextField editor = (JTextField) roomNumberCombo.getEditor().getEditorComponent();
        editor.setText(currentText);
        editor.setCaretPosition(currentText.length());
        
        
        if (filteredRooms.size() > 0 && !filter.isEmpty()) {
            roomNumberCombo.showPopup();
        }
    }

    private void searchGuestByRoom() {
        Object selectedRoom = roomNumberCombo.getSelectedItem();
        String roomNumber = null;
        
        if (selectedRoom != null) {
            roomNumber = selectedRoom.toString().trim();
        }
        
        if (roomNumber == null || roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select or enter a room number to search.",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            
            List<Booking> allBookings = bookingService.findBookingsByRoom(roomNumber);
            Booking foundBooking = null;

            
            for (Booking booking : allBookings) {
                if ((booking.getBookingStatus().equals("CONFIRMED") || booking.getBookingStatus().equals("CHECKED_IN"))) {
                    foundBooking = booking;
                    break;
                }
            }

            if (foundBooking != null) {
                displayGuestInformation(foundBooking);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No active booking found for room: " + roomNumber + "\n\n" +
                        "Note: Only CONFIRMED or CHECKED IN bookings can be checked out.",
                        "Guest Not Found",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error searching for guest: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void displayGuestInformation(Booking booking) {
        currentBooking = booking;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

        guestNameField.setText(booking.getGuestName());
        nicField.setText(booking.getGuestNIC());
        phoneField.setText(booking.getGuestPhone());
        roomNumberField.setText(booking.getRoomNumber());
        nightCountField.setText(String.valueOf(booking.getNumberOfNights()));
        checkOutDateField.setText(dateFormat.format(booking.getCheckOutDate()));

        removeGuestButton.setEnabled(true);

        JOptionPane.showMessageDialog(this,
                "Guest found!\n\n" +
                "Name: " + booking.getGuestName() + "\n" +
                "Room: " + booking.getRoomNumber() + "\n" +
                "Status: " + booking.getBookingStatus(),
                "Guest Found",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeGuest() {
        if (currentBooking == null) {
            JOptionPane.showMessageDialog(this,
                    "No guest selected. Please search for a guest first.",
                    "No Guest Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to check out this guest?\n\n" +
                "Guest: " + currentBooking.getGuestName() + "\n" +
                "Room: " + currentBooking.getRoomNumber() + "\n\n" +
                "This will:\n" +
                "• Mark the booking as CHECKED OUT\n" +
                "• Set room status to 'Needs Cleaning'\n" +
                "• Room will be available in 1 hour",
                "Confirm Check Out",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                
                boolean success = bookingService.checkOutGuest(currentBooking.getId());

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "═══════════════════════════════\n" +
                            "      CHECK OUT SUCCESSFUL\n" +
                            "═══════════════════════════════\n\n" +
                            "Guest: " + currentBooking.getGuestName() + "\n" +
                            "Room: " + currentBooking.getRoomNumber() + "\n\n" +
                            "✓ Booking marked as CHECKED OUT\n" +
                            "✓ Room status: Needs Cleaning\n" +
                            "✓ Cleaning staff assigned (if available)\n" +
                            "⏰ Room will be Available after cleaning\n\n" +
                            "Thank you for staying with INNOVA!",
                            "Check Out Complete",
                            JOptionPane.INFORMATION_MESSAGE);

                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to check out guest.\n" +
                            "Please try again or contact system administrator.",
                            "Check Out Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error during check-out: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void clearForm() {
        guestNameField.setText("");
        nicField.setText("");
        phoneField.setText("");
        roomNumberField.setText("");
        nightCountField.setText("");
        checkOutDateField.setText("");
        removeGuestButton.setEnabled(false);
        currentBooking = null;
        roomTypeCombo.setSelectedIndex(0);
        roomNumberCombo.setSelectedIndex(-1);
        updateRoomNumberList();
    }

    
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        styleComboBox(comboBox);
        return comboBox;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBackground(new Color(40, 40, 40));
        comboBox.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1));
        comboBox.setMaximumRowCount(8);
        
        
        for (int i = 0; i < comboBox.getComponentCount(); i++) {
            if (comboBox.getComponent(i) instanceof JButton) {
                JButton button = (JButton) comboBox.getComponent(i);
                button.setBackground(new Color(40, 40, 40));
                button.setBorder(BorderFactory.createEmptyBorder());
            }
        }
        
        
        if (comboBox.isEditable()) {
            Component editor = comboBox.getEditor().getEditorComponent();
            if (editor instanceof JTextField) {
                JTextField textField = (JTextField) editor;
                textField.setForeground(TEXT_COLOR);
                textField.setCaretColor(TEXT_COLOR);
                textField.setBackground(new Color(40, 40, 40));
                textField.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            }
        }
        
        
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                         int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? GOLD_COLOR : new Color(40, 40, 40));
                setForeground(isSelected ? Color.BLACK : TEXT_COLOR);
                setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                return this;
            }
        });
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
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(TEXT_COLOR);
        textField.setBackground(new Color(40, 40, 40));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        fieldGbc.weightx = 1.0;
        parent.add(textField, fieldGbc);

        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isEnabled()) {
                    g2.setColor(getBackground());
                } else {
                    g2.setColor(new Color(80, 80, 80));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(BUTTON_BG_COLOR);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled() && button.getBackground().equals(BUTTON_BG_COLOR)) {
                    button.setBackground(BUTTON_HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled() && button.getBackground().equals(BUTTON_HOVER_COLOR)) {
                    button.setBackground(BUTTON_BG_COLOR);
                }
            }
        });

        return button;
    }
}
