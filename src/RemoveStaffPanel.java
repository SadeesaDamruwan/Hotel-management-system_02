import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

public class RemoveStaffPanel extends JPanel {

    // --- Color Constants ---
    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);

    public RemoveStaffPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        // Form Container with Dark Transparent Background
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

        // --- Form Layout ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel titleLabel = new JLabel("Remove Staff");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2; // Span 2 columns (simplified layout)
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 40, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);

        // --- Row 1: Staff Id ---
        gbc.gridy++;
        addLabel(formContainer, "Staff Id", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        addTextField(formContainer, "", gbc, 0, gbc.gridy, 2);

        // --- Row 2: NIC/Passport No ---
        gbc.gridy++;
        addLabel(formContainer, "NIC/Passport No.", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        addTextField(formContainer, "", gbc, 0, gbc.gridy, 2);

        // --- Row 3: Dropdowns (Mr | Role) ---
        gbc.gridy++;
        // No explicit labels above dropdowns in screenshot, usually inline or implied
        gbc.insets = new Insets(20, 15, 5, 15); // Add some space before dropdowns

        // Split the row for two dropdowns
        addComboBox(formContainer, new String[]{"Mr", "Ms", "Mrs"}, "Mr", gbc, 0, gbc.gridy, 1);
        addComboBox(formContainer, new String[]{"Role", "Admin", "Manager", "Staff"}, "Role", gbc, 1, gbc.gridy, 1);

        // --- Row 4: Remove Button ---
        gbc.gridy++;
        gbc.insets = new Insets(40, 15, 10, 15);
        gbc.gridwidth = 2;

        JButton removeBtn = new JButton("   Remove") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(180, 180, 180)); // Light grey background
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        removeBtn.setPreferredSize(new Dimension(150, 40));
        removeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        removeBtn.setForeground(new Color(181, 139, 0)); // Gold text
        removeBtn.setFocusPainted(false);
        removeBtn.setBorderPainted(false);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add Trash Icon
        removeBtn.setIcon(new TrashIcon());

        // Wrap in panel to align left (or center as per design preference)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(removeBtn);

        formContainer.add(btnPanel, gbc);

        // Push content to top
        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);
    }

    // --- Helper Methods (Same as AddStaffPanel) ---

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

    private void addTextField(JPanel parent, String placeholder, GridBagConstraints gbc, int x, int y, int width) {
        JTextField textField = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        textField.setOpaque(false);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(GOLD_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        if (!placeholder.isEmpty()) {
            textField.setText(placeholder);
            textField.setForeground(PLACEHOLDER_COLOR);
            textField.addFocusListener(new PlaceholderFocusListener(placeholder, textField));
        }

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(textField, fieldGbc);
    }

    private void addComboBox(JPanel parent, String[] items, String defaultItem, GridBagConstraints gbc, int x, int y, int width) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setSelectedItem(defaultItem);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(TEXT_COLOR);
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
                    setOpaque(false);
                    setForeground(TEXT_COLOR);
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

    // --- Custom Trash Icon ---
    private class TrashIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground()); // Use button's text color (Gold)

            // Draw Trash Can
            int w = 12;
            int h = 14;
            int yOffset = 2;

            g2.fillRect(x + 3, y, 6, 2); // Handle
            g2.fillRect(x, y + 2, w, 2); // Lid
            g2.fillRect(x + 2, y + 4, 8, 10); // Bin

            // Lines
            g2.setColor(new Color(180, 180, 180)); // Background color for "cutouts"
            g2.fillRect(x + 4, y + 6, 1, 6);
            g2.fillRect(x + 7, y + 6, 1, 6);

            g2.dispose();
        }

        @Override
        public int getIconWidth() { return 12; }
        @Override
        public int getIconHeight() { return 16; }
    }
}