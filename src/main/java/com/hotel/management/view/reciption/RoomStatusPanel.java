package com.hotel.management.view.receptionist;

import com.hotel.management.model.Room;
import com.hotel.management.model.Staff;
import com.hotel.management.service.RoomService;
import com.hotel.management.service.StaffService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class RoomStatusPanel extends JPanel {
    
    private RoomService roomService;
    private StaffService staffService;
    
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    private JComboBox<String> roomTypeFilterCombo;
    private JComboBox<String> statusFilterCombo;
    private JTextField searchField;
    
    private JLabel availableCountLabel;
    private JLabel occupiedCountLabel;
    private JLabel cleaningCountLabel;
    private JLabel maintenanceCountLabel;
    
    private List<Room> allRooms;
    
    private final Color AVAILABLE_COLOR = new Color(46, 204, 113); 
    private final Color OCCUPIED_COLOR = new Color(231, 76, 60); 
    private final Color CLEANING_COLOR = new Color(241, 196, 15); 
    private final Color MAINTENANCE_COLOR = new Color(230, 126, 34); 
    private final Color CARD_BG = new Color(40, 40, 45, 230);
    private final Color TABLE_BG = new Color(30, 30, 35, 200);
    private final Color HEADER_BG = new Color(50, 50, 55);
    private final Color ROW_EVEN = new Color(45, 45, 50, 180);
    private final Color ROW_ODD = new Color(35, 35, 40, 180);
    
    public RoomStatusPanel() {
        this.roomService = new RoomService();
        this.staffService = new StaffService();
        
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        
        initComponents();
        
        loadRoomData();
    }
    
    private void initComponents() {
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setOpaque(false);
        
        mainContainer.add(createTopSection(), BorderLayout.NORTH);
        
        mainContainer.add(createTableSection(), BorderLayout.CENTER);
        
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private JPanel createTopSection() {
        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Room Status Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        topPanel.add(createStatsPanel(), BorderLayout.CENTER);
        
        topPanel.add(createFiltersPanel(), BorderLayout.SOUTH);
        
        return topPanel;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        availableCountLabel = new JLabel("0");
        occupiedCountLabel = new JLabel("0");
        cleaningCountLabel = new JLabel("0");
        maintenanceCountLabel = new JLabel("0");
        
        statsPanel.add(createStatCard("Available", availableCountLabel, AVAILABLE_COLOR, ""));
        statsPanel.add(createStatCard("Occupied", occupiedCountLabel, OCCUPIED_COLOR, ""));
        statsPanel.add(createStatCard("Needs Cleaning", cleaningCountLabel, CLEANING_COLOR, ""));
        statsPanel.add(createStatCard("Maintenance", maintenanceCountLabel, MAINTENANCE_COLOR, ""));
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, JLabel countLabel, Color accentColor, String icon) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout(10, 5));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JPanel topSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topSection.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(200, 200, 200));
        
        if (!icon.isEmpty()) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            topSection.add(iconLabel);
        }
        topSection.add(titleLabel);
        
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        countLabel.setForeground(accentColor);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(topSection, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createFiltersPanel() {
        JPanel filtersPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        filtersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        filtersPanel.setOpaque(false);
        
        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        typeLabel.setForeground(Color.WHITE);
        
        roomTypeFilterCombo = new JComboBox<>(new String[]{
            "All Types", "Standard Rooms", "Deluxe suite", "Family Room", "Penthouse"
        });
        roomTypeFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roomTypeFilterCombo.setPreferredSize(new Dimension(180, 35));
        roomTypeFilterCombo.addActionListener(e -> applyFilters());
        
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(Color.WHITE);
        
        statusFilterCombo = new JComboBox<>(new String[]{
            "All Status", "Available", "Occupied", "Needs Cleaning", "Maintenance"
        });
        statusFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilterCombo.setPreferredSize(new Dimension(180, 35));
        statusFilterCombo.addActionListener(e -> applyFilters());
        
        JLabel searchLabel = new JLabel("Search Room:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(Color.WHITE);
        
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(150, 35));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilters();
            }
        });
        
        JButton refreshBtn = createStyledButton("Refresh", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> {
            loadRoomData();
            JOptionPane.showMessageDialog(this, "Room data refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        filtersPanel.add(typeLabel);
        filtersPanel.add(roomTypeFilterCombo);
        filtersPanel.add(Box.createHorizontalStrut(10));
        filtersPanel.add(statusLabel);
        filtersPanel.add(statusFilterCombo);
        filtersPanel.add(Box.createHorizontalStrut(10));
        filtersPanel.add(searchLabel);
        filtersPanel.add(searchField);
        filtersPanel.add(Box.createHorizontalStrut(10));
        filtersPanel.add(refreshBtn);
        
        return filtersPanel;
    }
    
    private JScrollPane createTableSection() {
        String[] columnNames = {"Room Number", "Room Type", "Price", "Status", "Actions", "Assign Staff"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; 
            }
        };
        
        roomTable = new JTable(tableModel);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roomTable.setRowHeight(50);
        roomTable.setBackground(TABLE_BG);
        roomTable.setForeground(Color.WHITE);
        roomTable.setGridColor(new Color(60, 60, 65));
        roomTable.setSelectionBackground(new Color(255, 180, 60));
        roomTable.setSelectionForeground(Color.WHITE);
        roomTable.setShowGrid(true);
        roomTable.setOpaque(true);
        
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomTable.getTableHeader().setBackground(HEADER_BG);
        roomTable.getTableHeader().setForeground(new Color(255, 200, 100));
        roomTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        
        roomTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        roomTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        roomTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        roomTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        roomTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        roomTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        roomTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        roomTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        roomTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        roomTable.getColumnModel().getColumn(5).setCellRenderer(new AssignStaffRenderer());
        roomTable.getColumnModel().getColumn(5).setCellEditor(new AssignStaffEditor(new JCheckBox()));
        
        roomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setOpaque(true);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        label.setBackground(ROW_EVEN);
                    } else {
                        label.setBackground(ROW_ODD);
                    }
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(255, 180, 60));
                    label.setForeground(Color.WHITE);
                }
                
                if (column != 3) {
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
                
                return label;
            }
        });
        
        rowSorter = new TableRowSorter<>(tableModel);
        roomTable.setRowSorter(rowSorter);
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        return scrollPane;
    }
    
    private void loadRoomData() {
        try {
            allRooms = roomService.getAllRooms();
            updateTable(allRooms);
            updateStats(allRooms);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading room data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTable(List<Room> rooms) {
        tableModel.setRowCount(0);
        
        for (Room room : rooms) {
            Object[] row = {
                room.getRoomNumber(),
                room.getRoomType(),
                "LKR " + String.format("%,.2f", room.getPrice()),
                room.getStatus(),
                room.getStatus(), 
                "+" 
            };
            tableModel.addRow(row);
        }
    }
    
    private void updateStats(List<Room> rooms) {
        int available = 0, occupied = 0, cleaning = 0, maintenance = 0;
        
        for (Room room : rooms) {
            switch (room.getStatus()) {
                case "Available":
                    available++;
                    break;
                case "Occupied":
                    occupied++;
                    break;
                case "Needs Cleaning":
                    cleaning++;
                    break;
                case "Maintenance":
                    maintenance++;
                    break;
            }
        }
        
        availableCountLabel.setText(String.valueOf(available));
        occupiedCountLabel.setText(String.valueOf(occupied));
        cleaningCountLabel.setText(String.valueOf(cleaning));
        maintenanceCountLabel.setText(String.valueOf(maintenance));
    }
    
    private void applyFilters() {
        String selectedType = (String) roomTypeFilterCombo.getSelectedItem();
        String selectedStatus = (String) statusFilterCombo.getSelectedItem();
        String searchText = searchField.getText().trim().toLowerCase();
        
        List<Room> filtered = allRooms.stream()
            .filter(room -> {
                boolean typeMatch = selectedType.equals("All Types") || 
                                  room.getRoomType().equalsIgnoreCase(selectedType);
                
                boolean statusMatch = selectedStatus.equals("All Status") || 
                                    room.getStatus().equalsIgnoreCase(selectedStatus);
                
                boolean searchMatch = searchText.isEmpty() || 
                                    room.getRoomNumber().toLowerCase().contains(searchText);
                
                return typeMatch && statusMatch && searchMatch;
            })
            .collect(Collectors.toList());
        
        updateTable(filtered);
    }
    
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 8));
            panel.setOpaque(true);
            
            if (!isSelected) {
                panel.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
            } else {
                panel.setBackground(table.getSelectionBackground());
            }
            
            String status = (String) value;
            Color badgeColor;
            
            switch (status) {
                case "Available":
                    badgeColor = AVAILABLE_COLOR;
                    break;
                case "Occupied":
                    badgeColor = OCCUPIED_COLOR;
                    break;
                case "Needs Cleaning":
                    badgeColor = CLEANING_COLOR;
                    break;
                case "Maintenance":
                    badgeColor = MAINTENANCE_COLOR;
                    break;
                default:
                    badgeColor = Color.GRAY;
            }
            
            JLabel badge = new JLabel(status) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(badgeColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
            badge.setForeground(Color.WHITE);
            badge.setOpaque(false);
            badge.setBorder(new EmptyBorder(5, 15, 5, 15));
            
            panel.add(badge);
            return panel;
        }
    }
    
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton actionButton;
        
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 8));
            setOpaque(true);
            actionButton = new JButton();
            actionButton.setFocusPainted(false);
            add(actionButton);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            setBackground(isSelected ? table.getSelectionBackground() : 
                         (row % 2 == 0 ? ROW_EVEN : ROW_ODD));
            
            styleButton(actionButton, "View Details", new Color(52, 152, 219));
            
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton actionButton;
        private String currentStatus;
        private int currentRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 8));
            panel.setOpaque(true);
            actionButton = new JButton();
            actionButton.setFocusPainted(false);
            
            actionButton.addActionListener(e -> {
                // Handle action first, then stop editing to prevent UI shift
                handleAction();
                // Use SwingUtilities.invokeLater to ensure dialog is closed first
                SwingUtilities.invokeLater(() -> fireEditingStopped());
            });
            
            panel.add(actionButton);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            currentRow = row;
            currentStatus = (String) value;
            // Set solid background color to prevent transparency
            panel.setOpaque(true);
            panel.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
            
            styleButton(actionButton, "View Details", new Color(52, 152, 219));
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return currentStatus;
        }
        
        private void handleAction() {
            int modelRow = roomTable.convertRowIndexToModel(currentRow);
            String roomNumber = (String) tableModel.getValueAt(modelRow, 0);
            
            Room selectedRoom = allRooms.stream()
                .filter(r -> r.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElse(null);
            
            if (selectedRoom == null) return;
            
            showRoomDetails(selectedRoom);
        }
    }
    
    class AssignStaffRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton plusButton;
        
        public AssignStaffRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 8));
            setOpaque(true);
            plusButton = new JButton("+");
            plusButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
            plusButton.setForeground(Color.WHITE);
            plusButton.setBackground(new Color(46, 204, 113));
            plusButton.setFocusPainted(false);
            plusButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            plusButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            add(plusButton);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            setBackground(isSelected ? table.getSelectionBackground() : 
                         (row % 2 == 0 ? ROW_EVEN : ROW_ODD));
            
            return this;
        }
    }
    
    class AssignStaffEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton plusButton;
        private int currentRow;
        
        public AssignStaffEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 8));
            plusButton = new JButton("+");
            plusButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
            plusButton.setForeground(Color.WHITE);
            plusButton.setBackground(new Color(46, 204, 113));
            plusButton.setFocusPainted(false);
            plusButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            plusButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            plusButton.addActionListener(e -> {
                fireEditingStopped();
                handleAssignStaff();
            });
            
            panel.add(plusButton);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            currentRow = row;
            panel.setBackground(table.getSelectionBackground());
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "+";
        }
        
        private void handleAssignStaff() {
            int modelRow = roomTable.convertRowIndexToModel(currentRow);
            String roomNumber = (String) tableModel.getValueAt(modelRow, 0);
            
            Room selectedRoom = allRooms.stream()
                .filter(r -> r.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElse(null);
            
            if (selectedRoom == null) return;
            
            showStaffTypeSelectionDialog(selectedRoom);
        }
    }
    
    private void showStaffTypeSelectionDialog(Room room) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Select Staff Type", true);
        dialog.setSize(600, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 35));
        
        JLabel titleLabel = new JLabel("Assign Staff to Room " + room.getRoomNumber());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel messageLabel = new JLabel("Select the type of staff to assign:");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(200, 200, 200));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(messageLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        JButton cleaningBtn = createStyledButton("Cleaning Staff", CLEANING_COLOR);
        cleaningBtn.addActionListener(e -> {
            dialog.dispose();
            showStaffAssignmentDialog(room, "Cleaning Staff");
        });
        
        JButton maintenanceBtn = createStyledButton("Maintenance Staff", MAINTENANCE_COLOR);
        maintenanceBtn.addActionListener(e -> {
            dialog.dispose();
            showStaffAssignmentDialog(room, "Maintenance Staff");
        });
        
        JButton cancelBtn = createStyledButton("Cancel", new Color(231, 76, 60));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(cleaningBtn);
        buttonPanel.add(maintenanceBtn);
        
        centerPanel.add(buttonPanel);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(cancelBtn);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private void showStaffAssignmentDialog(Room room, String requiredRole) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Assign " + requiredRole, true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 35));
        
        JLabel titleLabel = new JLabel("Select " + requiredRole + " for Room " + room.getRoomNumber());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        List<Staff> allStaff = staffService.findStaffByRole(requiredRole);
        
        List<String> currentlyAssignedStaffNames = roomService.getCurrentlyAssignedStaff();
        final List<Staff> availableStaff = allStaff.stream()
            .filter(staff -> !currentlyAssignedStaffNames.contains(staff.getName()))
            .collect(java.util.stream.Collectors.toList());
        
        if (availableStaff.isEmpty()) {
            JLabel noStaffLabel = new JLabel("<html><center>No " + requiredRole + " available!<br>" +
                "All staff are currently assigned to tasks.</center></html>");
            noStaffLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            noStaffLabel.setForeground(Color.RED);
            noStaffLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(noStaffLabel, BorderLayout.CENTER);
        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Staff staff : availableStaff) {
                listModel.addElement(staff.getName() + " - " + staff.getPhone());
            }
            
            JList<String> staffList = new JList<>(listModel);
            staffList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            staffList.setBackground(new Color(40, 40, 45));
            staffList.setForeground(Color.WHITE);
            staffList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            JScrollPane scrollPane = new JScrollPane(staffList);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            buttonPanel.setOpaque(false);
            
            JButton assignBtn = createStyledButton("Assign Staff", AVAILABLE_COLOR);
            assignBtn.addActionListener(e -> {
                int selectedIndex = staffList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Staff selectedStaff = availableStaff.get(selectedIndex);
                    
                    String newStatus;
                    if (requiredRole.equals("Cleaning Staff")) {
                        newStatus = "Needs Cleaning";
                    } else if (requiredRole.equals("Maintenance Staff")) {
                        newStatus = "Maintenance";
                    } else {
                        newStatus = room.getStatus();
                    }
                    
                    boolean success;
                    if (requiredRole.equals("Cleaning Staff")) {
                        success = roomService.updateRoomStatus(room.getRoomNumber(), newStatus, selectedStaff.getName());
                        if (success) {
                            try {
                                roomService.markRoomNeedsCleaning(room.getRoomNumber());
                            } catch (Exception ex) {
                                System.err.println("Error setting cleaning timestamp: " + ex.getMessage());
                            }
                        }
                    } else {
                        success = roomService.updateRoomStatus(room.getRoomNumber(), newStatus, selectedStaff.getName());
                    }
                    
                    if (success) {
                        room.setStatus(newStatus);
                        room.setAssignedStaff(selectedStaff.getName());
                        
                        loadRoomData();
                        
                        JOptionPane.showMessageDialog(dialog,
                            selectedStaff.getName() + " assigned to Room " + room.getRoomNumber() + "\n" +
                            "Room status updated to: " + newStatus,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                            "Failed to assign staff. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Please select a staff member!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                }
            });
            
            JButton cancelBtn = createStyledButton("Cancel", new Color(231, 76, 60));
            cancelBtn.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(assignBtn);
            buttonPanel.add(cancelBtn);
            
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private void showRoomDetails(Room room) {
        String details = String.format(
            "Room Number: %s\n" +
            "Room Type: %s\n" +
            "Price: LKR %.2f\n" +
            "Status: %s\n" +
            "Capacity: %d guests\n" +
            "Assigned Staff: %s",
            room.getRoomNumber(),
            room.getRoomType(),
            room.getPrice(),
            room.getStatus(),
            room.getCapacity(),
            room.getAssignedStaff()
        );
        
        JOptionPane.showMessageDialog(this, details, 
            "Room Details", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void styleButton(JButton button, String text, Color bgColor) {
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
