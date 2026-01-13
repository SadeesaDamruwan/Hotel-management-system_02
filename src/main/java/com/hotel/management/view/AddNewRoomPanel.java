package com.hotel.management.view.admin;

import com.hotel.management.model.Room;
import com.hotel.management.service.RoomService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

public class AddNewRoomPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);

    private JTextField roomNumberField;
    private JComboBox<String> roomTypeComboBox;
    private JTextField priceField;
    private JComboBox<String> statusComboBox;
    
    private final RoomService roomService;

    public AddNewRoomPanel() {
        this.roomService = new RoomService();
        setOpaque(false);
        setLayout(new GridBagLayout());

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

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.weightx = 1.0;
        mainGbc.weighty = 1.0;
        add(formContainer, mainGbc);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add New Room");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 40, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);

        gbc.gridy++;
        addLabel(formContainer, "Room Number", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        roomNumberField = addTextField(formContainer, "e.g., 101, 205, Penthouse-1", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        addLabel(formContainer, "Room Type", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        roomTypeComboBox = addComboBox(formContainer, 
                new String[]{"Standard Rooms", "Deluxe suite", "Family Room", "Penthouse"}, 
                "Standard Rooms", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        addLabel(formContainer, "Price per Night (LKR)", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        priceField = addTextField(formContainer, "1500.00", gbc, 0, gbc.gridy, 2);
        priceField.setEditable(false);
        priceField.setForeground(TEXT_COLOR);
        priceField.setBackground(new Color(60, 60, 60, 100));
        
        roomTypeComboBox.addActionListener(e -> updatePriceForRoomType());

        gbc.gridy++;
        addLabel(formContainer, "Initial Status", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        statusComboBox = addComboBox(formContainer, 
                new String[]{"Available", "Needs Cleaning", "Under Maintenance"}, 
                "Available", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        gbc.insets = new Insets(40, 15, 10, 15);
        gbc.gridwidth = 2;

        JButton addBtn = new JButton("   Add Room") {
            private boolean isHovered = false;
            private boolean isPressed = false;
            
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                    
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        isPressed = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        isPressed = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor;
                if (isPressed) {
                    bgColor = new Color(200, 160, 60); 
                } else if (isHovered) {
                    bgColor = new Color(255, 200, 80); 
                } else {
                    bgColor = GOLD_COLOR; 
                }
                
                if (!isPressed) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(2, 3, getWidth() - 2, getHeight() - 2, 10, 10);
                }
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    0, getHeight(), bgColor.darker()
                );
                g2.setPaint(gradient);
                
                int offset = isPressed ? 2 : 0;
                g2.fillRoundRect(offset, offset, getWidth() - offset, getHeight() - offset, 10, 10);
                
                if (isHovered && !isPressed) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(offset + 1, offset + 1, getWidth() - offset - 2, getHeight() / 2, 10, 10);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        addBtn.setPreferredSize(new Dimension(160, 45));
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(Color.BLACK);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addBtn.setIcon(new PlusIcon());
        
        addBtn.addActionListener(e -> handleAddRoom());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(addBtn);

        formContainer.add(btnPanel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);
    }
    
    private void updatePriceForRoomType() {
        String roomType = (String) roomTypeComboBox.getSelectedItem();
        double price = getFixedPriceForRoomType(roomType);
        priceField.setText(String.format("%.2f", price));
        priceField.setForeground(TEXT_COLOR);
    }
    
    private double getFixedPriceForRoomType(String roomType) {
        switch (roomType) {
            case "Standard Rooms":
                return 1500.00;
            case "Deluxe suite":
                return 10000.00;
            case "Family Room":
                return 5000.00;
            case "Penthouse":
                return 20000.00;
            default:
                return 1500.00;
        }
    }
    
    private void handleAddRoom() {
        String roomNumber = getFieldText(roomNumberField);
        String roomType = (String) roomTypeComboBox.getSelectedItem();
        String priceStr = getFieldText(priceField);
        String status = (String) statusComboBox.getSelectedItem();
        
        if (roomNumber.isEmpty()) {
            showError("Please enter room number");
            return;
        }
        
        if (priceStr.isEmpty()) {
            showError("Please enter price");
            return;
        }
        
        double price;
        
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                showError("Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid price format. Please enter a valid number.");
            return;
        }
        
        Room room = new Room(roomNumber, roomType, status, price);
        
        if (roomService.addRoom(room)) {
            JOptionPane.showMessageDialog(this,
                    "Room " + roomNumber + " added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            showError("Failed to add room. Room number might already exist.");
        }
    }
    
    private void clearForm() {
        roomNumberField.setText("e.g., 101, 205, Penthouse-1");
        roomNumberField.setForeground(PLACEHOLDER_COLOR);
        
        roomTypeComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        
        updatePriceForRoomType();
    }
    
    private String getFieldText(JTextField field) {
        String text = field.getText().trim();
        
        if (field == priceField) {
            return text;
        }
        
        Color fg = field.getForeground();
        
        if (fg.equals(PLACEHOLDER_COLOR)) {
            return "";
        }
        
        return text;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }


    private void addLabel(JPanel parent, String text, GridBagConstraints gbc, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(LABEL_COLOR);

        GridBagConstraints labelGbc = (GridBagConstraints) gbc.clone();
        labelGbc.gridx = x;
        labelGbc.gridy = y;
        labelGbc.gridwidth = width;
        labelGbc.insets = new Insets(10, 15, 5, 15);
        parent.add(label, labelGbc);
    }

    private JTextField addTextField(JPanel parent, String placeholder, GridBagConstraints gbc, int x, int y, int width) {
        JTextField textField = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        textField.setOpaque(false);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(PLACEHOLDER_COLOR);
        textField.setCaretColor(GOLD_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        textField.addFocusListener(new PlaceholderFocusListener(placeholder, textField));

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(textField, fieldGbc);
        
        return textField;
    }

    private JComboBox<String> addComboBox(JPanel parent, String[] items, String defaultItem, GridBagConstraints gbc, int x, int y, int width) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setSelectedItem(defaultItem);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(Color.BLACK);
        comboBox.setOpaque(false);

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
                        int w = 8; int h = 5;
                        int mx = c.getWidth() / 2 - w/2;
                        int my = c.getHeight() / 2 - h/2;
                        int[] xPoints = {mx, mx + w, mx + w / 2};
                        int[] yPoints = {my, my, my + h};
                        g2.fillPolygon(xPoints, yPoints, 3);
                        g2.dispose();
                    }
                    @Override
                    public int getIconWidth() { return 10; }
                    @Override
                    public int getIconHeight() { return 10; }
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

        GridBagConstraints comboGbc = (GridBagConstraints) gbc.clone();
        comboGbc.gridx = x;
        comboGbc.gridy = y;
        comboGbc.gridwidth = width;
        parent.add(comboBox, comboGbc);
        
        return comboBox;
    }

    private class PlaceholderFocusListener implements FocusListener {
        private final String placeholder;
        private final JTextComponent component;

        public PlaceholderFocusListener(String placeholder, JTextComponent component) {
            this.placeholder = placeholder;
            this.component = component;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (component.getText().equals(placeholder)) {
                component.setText("");
                component.setForeground(TEXT_COLOR);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (component.getText().isEmpty()) {
                component.setText(placeholder);
                component.setForeground(PLACEHOLDER_COLOR);
            }
        }
    }

    private class PlusIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(2));

            int size = 12;
            int centerX = x + size / 2;
            int centerY = y + size / 2;

            g2.drawLine(centerX, y + 2, centerX, y + size - 2);
            g2.drawLine(x + 2, centerY, x + size - 2, centerY);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return 12; }
        @Override
        public int getIconHeight() { return 12; }
    }
}
