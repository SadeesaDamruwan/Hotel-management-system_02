package com.hotel.management.view.admin;

import com.hotel.management.model.Room;
import com.hotel.management.service.RoomService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import javax.swing.text.JTextComponent;

public class AddRoomPanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);

    private JComboBox<String> roomNumCombo;
    private JTextField roomTypeField;
    private JTextField currentStatusField;
    private final RoomService roomService;

    public AddRoomPanel() {
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

        add(formContainer);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("View Room Status");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 40, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);

        gbc.gridy++;
        addLabel(formContainer, "Select Room Number", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        
        JPanel roomSelectorPanel = new JPanel(new BorderLayout(10, 0));
        roomSelectorPanel.setOpaque(false);
        
        List<Room> allRooms = roomService.getAllRooms();
        String[] rooms;
        if (allRooms.isEmpty()) {
            rooms = new String[]{"No rooms available"};
        } else {
            rooms = allRooms.stream()
                .map(Room::getRoomNumber)
                .toArray(String[]::new);
        }
        
        roomNumCombo = new JComboBox<>(rooms);
        roomNumCombo.setSelectedItem(rooms.length > 0 ? rooms[0] : "No rooms available");
        roomNumCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roomNumCombo.setForeground(Color.BLACK);
        roomNumCombo.setOpaque(false);
        roomNumCombo.setBackground(new Color(0,0,0,0));
        
        styleComboBox(roomNumCombo);
        roomSelectorPanel.add(roomNumCombo, BorderLayout.CENTER);
        
        JButton refreshBtn = new JButton() {
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        refreshBtn.setPreferredSize(new Dimension(45, 35));
        refreshBtn.setIcon(new RefreshIcon());
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setToolTipText("Refresh room list");
        refreshBtn.addActionListener(e -> refreshRoomList());
        
        roomSelectorPanel.add(refreshBtn, BorderLayout.EAST);
        
        GridBagConstraints selectorGbc = (GridBagConstraints) gbc.clone();
        selectorGbc.gridx = 0;
        selectorGbc.gridy = gbc.gridy;
        selectorGbc.gridwidth = 2;
        formContainer.add(roomSelectorPanel, selectorGbc);

        gbc.gridy++;
        addLabel(formContainer, "Room Type", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        roomTypeField = addReadOnlyTextField(formContainer, "Loading...", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        addLabel(formContainer, "Current Status", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        currentStatusField = addReadOnlyTextField(formContainer, "Checking...", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);

        roomNumCombo.addActionListener(e -> checkRoomStatus());
        checkRoomStatus();
    }
   
    private void refreshRoomList() {
        
        String currentSelection = (String) roomNumCombo.getSelectedItem();
        
        List<Room> allRooms = roomService.getAllRooms();
        
        roomNumCombo.removeAllItems();
        
        if (allRooms.isEmpty()) {
            roomNumCombo.addItem("No rooms available");
        } else {
            for (Room room : allRooms) {
                roomNumCombo.addItem(room.getRoomNumber());
            }
            
            boolean found = false;
            for (int i = 0; i < roomNumCombo.getItemCount(); i++) {
                if (roomNumCombo.getItemAt(i).equals(currentSelection)) {
                    roomNumCombo.setSelectedIndex(i);
                    found = true;
                    break;
                }
            }
            if (!found && roomNumCombo.getItemCount() > 0) {
                roomNumCombo.setSelectedIndex(0);
            }
        }
        
        checkRoomStatus();
    }

    private void checkRoomStatus() {
        String selectedRoomNumber = (String) roomNumCombo.getSelectedItem();
        
        if (selectedRoomNumber == null || selectedRoomNumber.equals("No rooms available")) {
            roomTypeField.setText("No room selected");
            roomTypeField.setForeground(Color.GRAY);
            currentStatusField.setText("No room selected");
            currentStatusField.setForeground(Color.GRAY);
            return;
        }
        
        Room room = roomService.findRoomByNumber(selectedRoomNumber);
        
        if (room != null) {
            String roomType = room.getRoomType();
            roomTypeField.setText(roomType);
            roomTypeField.setForeground(GOLD_COLOR);
            
            String status = room.getStatus();
            currentStatusField.setText(status);
            
            if (status.equals("Available")) {
                currentStatusField.setForeground(new Color(100, 255, 100));
            } else if (status.equals("Occupied")) {
                currentStatusField.setForeground(new Color(255, 100, 100));
            } else if (status.equals("Needs Cleaning")) {
                currentStatusField.setForeground(GOLD_COLOR);
            } else if (status.equals("Under Maintenance")) {
                currentStatusField.setForeground(new Color(255, 150, 0));
            } else if (status.equals("Reserved")) {
                currentStatusField.setForeground(new Color(100, 150, 255));
            } else {
                currentStatusField.setForeground(TEXT_COLOR);
            }
        } else {
            roomTypeField.setText("Room not found");
            roomTypeField.setForeground(Color.RED);
            currentStatusField.setText("Room not found");
            currentStatusField.setForeground(Color.RED);
        }
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

    private JTextField addReadOnlyTextField(JPanel parent, String text, GridBagConstraints gbc, int x, int y, int width) {
        JTextField textField = new JTextField(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        textField.setOpaque(false);
        textField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(GOLD_COLOR);
        textField.setEditable(false);
        textField.setFocusable(false);
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

    private JComboBox<String> addComboBox(JPanel parent, String[] items, String defaultItem, GridBagConstraints gbc, int x, int y, int width) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setSelectedItem(defaultItem);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(Color.BLACK); // Black text
        comboBox.setOpaque(false);
        comboBox.setBackground(new Color(0,0,0,0));

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
    }
    
    private class RefreshIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            
            int size = 16;
            int centerX = x + size / 2;
            int centerY = y + size / 2;
            
            g2.drawArc(x + 2, y + 2, size - 4, size - 4, 45, 300);
            
            int[] xPoints = {centerX + 6, centerX + 6, centerX + 3};
            int[] yPoints = {y + 2, y + 5, y + 2};
            g2.fillPolygon(xPoints, yPoints, 3);
            
            g2.dispose();
        }
        
        @Override
        public int getIconWidth() { return 20; }
        @Override
        public int getIconHeight() { return 20; }
    }
}