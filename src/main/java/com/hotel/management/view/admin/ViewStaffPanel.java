package com.hotel.management.view.admin;

import com.hotel.management.model.Staff;
import com.hotel.management.service.StaffService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ViewStaffPanel extends JPanel {

    private JTable staffTable;
    private DefaultTableModel tableModel;
    private final StaffService staffService;

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color TABLE_BG = new Color(40, 40, 40);
    private final Color HEADER_BG = new Color(30, 30, 30);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color DELETE_COLOR = new Color(255, 80, 80);

    public ViewStaffPanel() {
        this.staffService = new StaffService();
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("View Staff");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(GOLD_COLOR);
        topPanel.add(titleLabel);
        
        JButton refreshBtn = new JButton() {
            private boolean isHovered = false;
            
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
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background hover effect
                if (isHovered) {
                    g2.setColor(new Color(255, 180, 60, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = 8;
                
                g2.setColor(GOLD_COLOR);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // Draw circular arc
                g2.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 45, 270);
                
                // Draw arrow head 1 (top right)
                int arrowSize = 4;
                int[] xPoints1 = {centerX + radius - 2, centerX + radius - 2, centerX + radius + 2};
                int[] yPoints1 = {centerY - radius + 2, centerY - radius - 3, centerY - radius + 2};
                g2.fillPolygon(xPoints1, yPoints1, 3);
                
                // Draw arrow head 2 (bottom left)
                int[] xPoints2 = {centerX - radius + 2, centerX - radius + 2, centerX - radius - 2};
                int[] yPoints2 = {centerY + radius - 2, centerY + radius + 3, centerY + radius - 2};
                g2.fillPolygon(xPoints2, yPoints2, 3);
                
                g2.dispose();
            }
        };
        refreshBtn.setPreferredSize(new Dimension(36, 36));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setToolTipText("Refresh staff list");
        refreshBtn.addActionListener(e -> loadStaffData());
        topPanel.add(refreshBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Name/Id", "Email", "Phone", "Role", "Join Date", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        staffTable = new JTable(tableModel);
        staffTable.setFillsViewportHeight(true);
        staffTable.setRowHeight(100);
        staffTable.setShowVerticalLines(false);
        staffTable.setShowHorizontalLines(true);
        staffTable.setGridColor(new Color(50, 50, 50));
        staffTable.setBackground(TABLE_BG);
        staffTable.setForeground(TEXT_COLOR);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        staffTable.setSelectionBackground(new Color(60, 60, 70));
        staffTable.setSelectionForeground(Color.WHITE);
        staffTable.setIntercellSpacing(new Dimension(5, 1));
        
        
        JTableHeader header = staffTable.getTableHeader();
        header.setBackground(HEADER_BG);
        header.setForeground(GOLD_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GOLD_COLOR));
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        header.setReorderingAllowed(false);

        
        DefaultTableCellRenderer professionalRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(new EmptyBorder(10, 15, 10, 15));
                
                if (!isSelected) {
                    
                    if (row % 2 == 0) {
                        c.setBackground(TABLE_BG);
                    } else {
                        c.setBackground(new Color(45, 45, 50));
                    }
                    c.setForeground(TEXT_COLOR);
                } else {
                    c.setBackground(new Color(60, 60, 70));
                    c.setForeground(Color.WHITE);
                }
                
                
                if (column == 0 && value != null) {
                    String text = value.toString();
                    if (text.contains("\n")) {
                        String[] parts = text.split("\n");
                        setText("<html><div style='text-align: center;'><b style='font-size: 14px;'>" + parts[0] + 
                               "</b><br><span style='color: #FFB43C; font-size: 12px;'>" + 
                               (parts.length > 1 ? parts[1] : "") + "</span></div></html>");
                    }
                }
                
                return c;
            }
        };
        
        
        for (int i = 0; i < 5; i++) {
            staffTable.getColumnModel().getColumn(i).setCellRenderer(professionalRenderer);
        }

        
        staffTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(new EmptyBorder(10, 15, 10, 15));
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        label.setBackground(TABLE_BG);
                    } else {
                        label.setBackground(new Color(45, 45, 50));
                    }
                } else {
                    label.setBackground(new Color(60, 60, 70));
                }
                
                
                if (value != null) {
                    String role = value.toString();
                    label.setText("<html><div style='background-color: rgba(255, 180, 60, 0.15); " +
                                 "padding: 6px 16px; border-radius: 16px; display: inline-block; " +
                                 "border: 1px solid rgba(255, 180, 60, 0.4);'>" +
                                 "<b style='color: #FFB43C; font-size: 13px;'>" + role + "</b></div></html>");
                }
                
                return label;
            }
        });

        
        staffTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        staffTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD_COLOR, 2),
            BorderFactory.createLineBorder(new Color(30, 30, 30), 3)
        ));
        scrollPane.getViewport().setBackground(TABLE_BG);
        
        
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = GOLD_COLOR;
                this.trackColor = new Color(30, 30, 30);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
        
        add(scrollPane, BorderLayout.CENTER);

        
        loadStaffData();
    }

    private void loadStaffData() {
        
        tableModel.setRowCount(0);
        
       
        List<Staff> staffList = staffService.getAllStaff();
        
        for (Staff staff : staffList) {
            String nameId = staff.getName() + "\n" + staff.getId();
            tableModel.addRow(new Object[]{
                    nameId,
                    staff.getEmail(),
                    staff.getPhone(),
                    staff.getRole(),
                    staff.getJoinDate(),
                    "Delete"
            });
        }
        
        System.out.println("✓ Loaded " + staffList.size() + " staff members into table");
    }
    

    private void removeStaffFromDatabase(int row) {
     
        String nameId = (String) tableModel.getValueAt(row, 0);
        String staffId = nameId.split("\n")[1]; 
        
        // Delete from MongoDB
        if (staffService.deleteStaff(staffId)) {
            tableModel.removeRow(row);
            JOptionPane.showMessageDialog(this,
                    "Staff member removed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to remove staff from database.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDummyData() {
        tableModel.addRow(new Object[]{"John Doe\nSTF-1001", "john.doe@example.com", "+1 555-0101", "Manager", "01/01/2023", "Delete"});
        tableModel.addRow(new Object[]{"Jane Smith\nSTF-1002", "jane.smith@example.com", "+1 555-0102", "Receptionist", "15/02/2023", "Delete"});
        tableModel.addRow(new Object[]{"Mike Brown\nSTF-1003", "mike.brown@example.com", "+1 555-0103", "Chef", "10/03/2023", "Delete"});
        tableModel.addRow(new Object[]{"Sarah Davis\nSTF-1004", "sarah.davis@example.com", "+1 555-0104", "Housekeeping", "20/04/2023", "Delete"});
        tableModel.addRow(new Object[]{"Chris Wilson\nSTF-1005", "chris.wilson@example.com", "+1 555-0105", "Maintenance", "05/05/2023", "Delete"});
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setIcon(new DeleteIcon(DELETE_COLOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int editingRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setIcon(new DeleteIcon(DELETE_COLOR));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isPushed = true;
                    handleDelete();
                    cancelCellEditing();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            editingRow = row;
            isPushed = false;
            return button;
        }
        
        private void handleDelete() {;
            if (editingRow >= 0 && editingRow < tableModel.getRowCount()) {
                int confirm = JOptionPane.showConfirmDialog(ViewStaffPanel.this,
                        "Are you sure you want to delete this staff member?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    removeStaffFromDatabase(editingRow);
                }
            }
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    private class DeleteIcon implements Icon {
        private final Color color;
        private final int size = 16;

        public DeleteIcon(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(x, y, x + size, y + size);
            g2.drawLine(x + size, y, x, y + size);
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }
}
