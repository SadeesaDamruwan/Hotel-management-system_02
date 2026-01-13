package com.hotel.management.view.receptionist;

import com.hotel.management.model.Ticket;
import com.hotel.management.service.TicketService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewTicketsPanel extends JPanel {
    
    private TicketService ticketService;
    
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilterCombo;
    private JTextField searchField;
    
    private final Color BG_COLOR = new Color(30, 30, 35);
    private final Color CARD_BG = new Color(40, 40, 45, 230);
    private final Color TABLE_BG = new Color(30, 30, 35, 200);
    private final Color HEADER_BG = new Color(50, 50, 55);
    private final Color ROW_EVEN = new Color(45, 45, 50, 180);
    private final Color ROW_ODD = new Color(35, 35, 40, 180);
    
    private final Color URGENT_COLOR = new Color(231, 76, 60);
    private final Color HIGH_COLOR = new Color(230, 126, 34);
    private final Color MEDIUM_COLOR = new Color(241, 196, 15);
    private final Color LOW_COLOR = new Color(46, 204, 113);
    
    public ViewTicketsPanel() {
        this.ticketService = new TicketService();
        
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        
        initComponents();
        loadTickets();
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
        
        JLabel titleLabel = new JLabel("My Support Tickets");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        topPanel.add(createFiltersPanel(), BorderLayout.CENTER);
        
        return topPanel;
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
        
        JLabel statusLabel = new JLabel("Filter by Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(Color.WHITE);
        
        statusFilterCombo = new JComboBox<>(new String[]{
                "All Status", "Open", "In Progress", "Resolved", "Closed"
        });
        statusFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilterCombo.setPreferredSize(new Dimension(150, 35));
        statusFilterCombo.addActionListener(e -> applyFilter());
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(Color.WHITE);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                applyFilter();
            }
        });
        
        JButton refreshBtn = createStyledButton("Refresh", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> {
            loadTickets();
            JOptionPane.showMessageDialog(this, "Tickets refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
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
        String[] columnNames = {"Ticket ID", "Title", "Category", "Priority", "Status", "Room", "Created", "Admin Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        ticketTable = new JTable(tableModel);
        ticketTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ticketTable.setRowHeight(45);
        ticketTable.setBackground(TABLE_BG);
        ticketTable.setForeground(Color.WHITE);
        ticketTable.setGridColor(new Color(60, 60, 65));
        ticketTable.setSelectionBackground(new Color(255, 180, 60, 100));
        ticketTable.setSelectionForeground(Color.WHITE);
        
        ticketTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        ticketTable.getTableHeader().setBackground(HEADER_BG);
        ticketTable.getTableHeader().setForeground(new Color(255, 200, 100));
        ticketTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        ticketTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        ticketTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        ticketTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        ticketTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        ticketTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        ticketTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        ticketTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        ticketTable.getColumnModel().getColumn(7).setPreferredWidth(200);
        
        ticketTable.getColumnModel().getColumn(3).setCellRenderer(new PriorityCellRenderer());
        
        ticketTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(ROW_EVEN);
                    } else {
                        c.setBackground(ROW_ODD);
                    }
                    c.setForeground(Color.WHITE);
                }
                
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                
                return c;
            }
        });
        
        ticketTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = ticketTable.getSelectedRow();
                    if (row != -1) {
                        showTicketDetails(row);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        return scrollPane;
    }
    
    private void loadTickets() {
        tableModel.setRowCount(0);
        List<Ticket> tickets = ticketService.getAllTickets(); 
        
        for (Ticket ticket : tickets) {
            Object[] row = {
                    ticket.getTicketId(),
                    ticket.getTitle(),
                    ticket.getCategory(),
                    ticket.getPriority(),
                    ticket.getStatus(),
                    ticket.getRoomNumber() != null && !ticket.getRoomNumber().isEmpty() ? ticket.getRoomNumber() : "N/A",
                    TicketService.formatDate(ticket.getCreatedAt()),
                    ticket.getAdminNotes() != null ? ticket.getAdminNotes() : "No response yet"
            };
            tableModel.addRow(row);
        }
    }
    
    private void applyFilter() {
        String statusFilter = (String) statusFilterCombo.getSelectedItem();
        String searchText = searchField.getText().trim().toLowerCase();
        
        tableModel.setRowCount(0);
        List<Ticket> tickets = ticketService.getAllTickets();
        
        for (Ticket ticket : tickets) {
            if (!statusFilter.equals("All Status") && !ticket.getStatus().equals(statusFilter)) {
                continue;
            }
            
            if (!searchText.isEmpty()) {
                boolean matches = ticket.getTicketId().toLowerCase().contains(searchText) ||
                                ticket.getTitle().toLowerCase().contains(searchText) ||
                                (ticket.getRoomNumber() != null && ticket.getRoomNumber().toLowerCase().contains(searchText));
                if (!matches) continue;
            }
            
            Object[] row = {
                    ticket.getTicketId(),
                    ticket.getTitle(),
                    ticket.getCategory(),
                    ticket.getPriority(),
                    ticket.getStatus(),
                    ticket.getRoomNumber() != null && !ticket.getRoomNumber().isEmpty() ? ticket.getRoomNumber() : "N/A",
                    TicketService.formatDate(ticket.getCreatedAt()),
                    ticket.getAdminNotes() != null ? ticket.getAdminNotes() : "No response yet"
            };
            tableModel.addRow(row);
        }
    }
    
    private void showTicketDetails(int row) {
        String ticketId = (String) tableModel.getValueAt(row, 0);
        Ticket ticket = ticketService.getTicketById(ticketId);
        
        if (ticket != null) {
            String details = String.format(
                    "Ticket ID: %s\n" +
                    "Title: %s\n" +
                    "Description: %s\n" +
                    "Category: %s\n" +
                    "Priority: %s\n" +
                    "Status: %s\n" +
                    "Room Number: %s\n" +
                    "Guest Name: %s\n" +
                    "Created: %s\n" +
                    "Created By: %s\n" +
                    "Assigned To: %s\n" +
                    "Admin Notes: %s",
                    ticket.getTicketId(),
                    ticket.getTitle(),
                    ticket.getDescription(),
                    ticket.getCategory(),
                    ticket.getPriority(),
                    ticket.getStatus(),
                    ticket.getRoomNumber() != null ? ticket.getRoomNumber() : "N/A",
                    ticket.getGuestName() != null ? ticket.getGuestName() : "N/A",
                    TicketService.formatDate(ticket.getCreatedAt()),
                    ticket.getCreatedBy(),
                    ticket.getAssignedTo() != null ? ticket.getAssignedTo() : "Unassigned",
                    ticket.getAdminNotes() != null ? ticket.getAdminNotes() : "No response yet"
            );
            
            JOptionPane.showMessageDialog(this, details, "Ticket Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    class PriorityCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setOpaque(true);
            
            if (!isSelected) {
                panel.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
            } else {
                panel.setBackground(table.getSelectionBackground());
            }
            
            String priority = (String) value;
            Color badgeColor;
            
            switch (priority) {
                case "Urgent":
                    badgeColor = URGENT_COLOR;
                    break;
                case "High":
                    badgeColor = HIGH_COLOR;
                    break;
                case "Medium":
                    badgeColor = MEDIUM_COLOR;
                    break;
                case "Low":
                    badgeColor = LOW_COLOR;
                    break;
                default:
                    badgeColor = Color.GRAY;
            }
            
            JLabel badge = new JLabel(priority) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(badgeColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
            badge.setForeground(Color.WHITE);
            badge.setOpaque(false);
            badge.setBorder(new EmptyBorder(4, 12, 4, 12));
            
            panel.add(badge);
            return panel;
        }
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
