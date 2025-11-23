import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ViewStaffPanel extends JPanel {

    private JTable staffTable;
    private DefaultTableModel tableModel;

    // --- Color Constants ---
    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color TABLE_BG = new Color(40, 40, 40);
    private final Color HEADER_BG = new Color(30, 30, 30);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color DELETE_COLOR = new Color(255, 80, 80);

    public ViewStaffPanel() {
        setLayout(new BorderLayout());
        setOpaque(false); // Make transparent to see dashboard background
        setBorder(new EmptyBorder(30, 30, 30, 30)); // Padding around the panel

        // --- Title ---
        JLabel titleLabel = new JLabel("View Staff");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columnNames = {"Name/Id", "Email", "Phone", "Role", "Join Date", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the "Action" column is editable
            }
        };

        staffTable = new JTable(tableModel);
        staffTable.setFillsViewportHeight(true);
        staffTable.setRowHeight(40);
        staffTable.setShowVerticalLines(false);
        staffTable.setGridColor(new Color(60, 60, 60));
        staffTable.setBackground(TABLE_BG);
        staffTable.setForeground(TEXT_COLOR);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Header Styling
        JTableHeader header = staffTable.getTableHeader();
        header.setBackground(HEADER_BG);
        header.setForeground(GOLD_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GOLD_COLOR));

        // --- Custom Renderers ---
        // Center text in all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        staffTable.setDefaultRenderer(Object.class, centerRenderer);

        // Custom Renderer for "Role" column to highlight in gold
        staffTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(GOLD_COLOR);
                return c;
            }
        });

        // Custom Renderer and Editor for "Action" column (Delete button)
        staffTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        staffTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        // --- Scroll Pane ---
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(GOLD_COLOR, 1));
        scrollPane.getViewport().setBackground(TABLE_BG);
        add(scrollPane, BorderLayout.CENTER);

        // --- Load Dummy Data ---
        loadDummyData();
    }

    private void loadDummyData() {
        tableModel.addRow(new Object[]{"John Doe\nSTF-1001", "john.doe@example.com", "+1 555-0101", "Manager", "01/01/2023", "Delete"});
        tableModel.addRow(new Object[]{"Jane Smith\nSTF-1002", "jane.smith@example.com", "+1 555-0102", "Receptionist", "15/02/2023", "Delete"});
        tableModel.addRow(new Object[]{"Mike Brown\nSTF-1003", "mike.brown@example.com", "+1 555-0103", "Chef", "10/03/2023", "Delete"});
        tableModel.addRow(new Object[]{"Sarah Davis\nSTF-1004", "sarah.davis@example.com", "+1 555-0104", "Housekeeping", "20/04/2023", "Delete"});
        tableModel.addRow(new Object[]{"Chris Wilson\nSTF-1005", "chris.wilson@example.com", "+1 555-0105", "Maintenance", "05/05/2023", "Delete"});
    }

    // --- Button Renderer for Action Column ---
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

    // --- Button Editor for Action Column ---
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

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
                    fireEditingStopped();
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
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = staffTable.getSelectedRow();
                if (row != -1) {
                    // Confirm before deletion
                    int confirm = JOptionPane.showConfirmDialog(ViewStaffPanel.this,
                            "Are you sure you want to delete this staff member?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(row);
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    // --- Custom Icon for Delete Button ---
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
            // Draw an 'X'
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