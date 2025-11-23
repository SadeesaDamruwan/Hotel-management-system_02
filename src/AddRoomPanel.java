import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.text.JTextComponent;

public class AddRoomPanel extends JPanel {

    // --- Color Constants ---
    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);

    // --- Components for Logic ---
    private JComboBox<String> roomNumCombo;
    private JTextField currentStatusField;
    private JComboBox<String> newStatusCombo;
    private JComboBox<String> staffCombo;

    public AddRoomPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        // Form Container
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

        // --- Title ---
        JLabel titleLabel = new JLabel("Update Room Status");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 40, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);

        // --- Row 1: Select Room Number ---
        gbc.gridy++;
        addLabel(formContainer, "Select Room Number", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        String[] rooms = {"101", "102", "201", "205", "301", "Penthouse-1"};
        roomNumCombo = addComboBox(formContainer, rooms, "101", gbc, 0, gbc.gridy, 2);

        // --- Row 2: Current Status (Read-Only) ---
        gbc.gridy++;
        addLabel(formContainer, "Current Status", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        currentStatusField = addReadOnlyTextField(formContainer, "Checking...", gbc, 0, gbc.gridy, 2);

        // --- Row 3: New Status / Task ---
        gbc.gridy++;
        addLabel(formContainer, "Set New Status / Task", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        String[] statuses = {"Available", "Occupied", "Needs Cleaning", "Under Maintenance", "Reserved"};
        newStatusCombo = addComboBox(formContainer, statuses, "Needs Cleaning", gbc, 0, gbc.gridy, 2);

        // --- Row 4: Assign Staff ---
        gbc.gridy++;
        addLabel(formContainer, "Assign Staff Member", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        String[] staff = {"Unassigned", "John Doe (Housekeeping)", "Jane Smith (Maintenance)", "Mike Brown (Manager)"};
        staffCombo = addComboBox(formContainer, staff, "Unassigned", gbc, 0, gbc.gridy, 2);

        // --- Row 5: Update Button ---
        gbc.gridy++;
        gbc.insets = new Insets(40, 15, 10, 15);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);

        JButton updateBtn = new JButton("Update & Assign") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 150, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        updateBtn.setPreferredSize(new Dimension(200, 45));
        updateBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateBtn.setForeground(new Color(181, 139, 0));
        updateBtn.setFocusPainted(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setContentAreaFilled(false);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Action Logic
        updateBtn.addActionListener(e -> {
            String room = (String) roomNumCombo.getSelectedItem();
            String status = (String) newStatusCombo.getSelectedItem();
            String assignedStaff = (String) staffCombo.getSelectedItem();

            String msg = "Room " + room + " updated to '" + status + "'.\n";
            if (!assignedStaff.equals("Unassigned")) {
                msg += "Task assigned to: " + assignedStaff;
            } else {
                msg += "No staff assigned.";
            }

            JOptionPane.showMessageDialog(this, msg, "Status Updated", JOptionPane.INFORMATION_MESSAGE);

            // Reflect change in "Current Status" immediately
            currentStatusField.setText(status);
            // Re-color based on new status
            if (status.equals("Available")) currentStatusField.setForeground(new Color(100, 255, 100));
            else if (status.equals("Occupied")) currentStatusField.setForeground(new Color(255, 100, 100));
            else currentStatusField.setForeground(GOLD_COLOR);
        });

        btnPanel.add(updateBtn);
        formContainer.add(btnPanel, gbc);

        roomNumCombo.addActionListener(e -> checkRoomStatus());
        checkRoomStatus();
    }

    private void checkRoomStatus() {
        String[] mockStatuses = {"Occupied", "Available", "Needs Cleaning"};
        String randomStatus = mockStatuses[new Random().nextInt(mockStatuses.length)];
        currentStatusField.setText(randomStatus);

        if (randomStatus.equals("Available")) {
            currentStatusField.setForeground(new Color(100, 255, 100));
        } else if (randomStatus.equals("Occupied")) {
            currentStatusField.setForeground(new Color(255, 100, 100));
        } else {
            currentStatusField.setForeground(GOLD_COLOR);
        }
    }

    // --- Helper Methods ---

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
        comboBox.setForeground(GOLD_COLOR); // UPDATED: Main text color is GOLD
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
                    // Displayed Item
                    setOpaque(false);
                    setForeground(GOLD_COLOR); // UPDATED: Force displayed item to GOLD
                } else {
                    // Dropdown List Item
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
}