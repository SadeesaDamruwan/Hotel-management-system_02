import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.text.JTextComponent;

public class PriceAdjustmentPanel extends JPanel {

    // --- Color Constants ---
    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);
    private final Color CALENDAR_BG = new Color(40, 40, 40);

    // --- Component References (Needed for Calculation) ---
    private JComboBox<String> roomTypeCombo;
    private JTextField percentageField;

    public PriceAdjustmentPanel() {
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
        JLabel titleLabel = new JLabel("Seasonal Price Adjustment");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 40, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);

        // --- Row 1: Room Type ---
        gbc.gridy++;
        addLabel(formContainer, "Room Type :", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        // Capture the ComboBox to read selection later
        roomTypeCombo = addComboBox(formContainer, new String[]{"Standard Room", "Deluxe Suite", "Family Room", "Penthouse"}, "Room Type", gbc, 0, gbc.gridy, 1);

        gbc.gridx = 1;
        formContainer.add(Box.createGlue(), gbc);

        // --- Row 2: Date Range ---
        gbc.gridx = 0; // Reset X
        gbc.gridy++;
        addLabel(formContainer, "Date Range :", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        JPanel dateRow = new JPanel(new GridLayout(1, 2, 20, 0));
        dateRow.setOpaque(false);

        JPanel p1 = new JPanel(new BorderLayout());
        p1.setOpaque(false);
        addDatePicker(p1);
        dateRow.add(p1);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.setOpaque(false);
        addDatePicker(p2);
        dateRow.add(p2);

        gbc.gridwidth = 1;
        GridBagConstraints dateGbc = (GridBagConstraints) gbc.clone();
        dateGbc.gridwidth = 2;
        formContainer.add(dateRow, dateGbc);

        // --- Row 3: Percentage Adjustment ---
        gbc.gridy++;
        addLabel(formContainer, "Percentage Adjustment (%)", gbc, 0, gbc.gridy, 2);
        gbc.gridy++;
        // Capture the TextField to read input later
        percentageField = addTextField(formContainer, "Enter percentage (e.g. 15)", gbc, 0, gbc.gridy, 2);

        // --- Row 4: Update Button ---
        gbc.gridy++;
        gbc.insets = new Insets(40, 15, 10, 15);
        gbc.gridwidth = 2;

        JButton updateBtn = new JButton("   Update") {
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
        updateBtn.setPreferredSize(new Dimension(150, 40));
        updateBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateBtn.setForeground(GOLD_COLOR);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setContentAreaFilled(false);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.setIcon(new UpdateIcon());

        // --- CALCULATION LOGIC ---
        updateBtn.addActionListener(e -> performPriceCalculation());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(updateBtn);

        formContainer.add(btnPanel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);
    }

    /**
     * Calculates the new price based on room type base price and percentage increase.
     */
    private void performPriceCalculation() {
        String percentStr = percentageField.getText().trim();
        String roomType = (String) roomTypeCombo.getSelectedItem();

        // Validate Input
        if (percentStr.isEmpty() || percentStr.equals("Enter percentage (e.g. 15)")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid percentage.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double percentage = Double.parseDouble(percentStr);

            // Get Mock Base Price
            double basePrice = getBasePrice(roomType);

            // Calculate Increase
            double increaseAmount = basePrice * (percentage / 100.0);
            double newPrice = basePrice + increaseAmount;

            // Show Result
            String message = String.format(
                    "Room Type: %s\n" +
                            "Current Base Price: $%.2f\n" +
                            "Adjustment: +%.2f%%\n" +
                            "Increase Amount: $%.2f\n" +
                            "--------------------------------\n" +
                            "New Seasonal Price: $%.2f",
                    roomType, basePrice, percentage, increaseAmount, newPrice
            );

            JOptionPane.showMessageDialog(this, message, "Price Updated Successfully", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid percentage format. Please enter a number (e.g. 10.5).", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Mock method to get current price from "Database".
     */
    private double getBasePrice(String roomType) {
        switch (roomType) {
            case "Standard Room": return 100.00;
            case "Deluxe Suite": return 250.00;
            case "Family Room": return 400.00;
            case "Penthouse": return 1200.00;
            default: return 100.00;
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
        labelGbc.insets = new Insets(10, 15, 5, 15);
        parent.add(label, labelGbc);
    }

    // Updated to return JTextField
    private JTextField addTextField(JPanel parent, String placeholder, GridBagConstraints gbc, int x, int y, int width) {
        JTextField textField = createStyledTextField(placeholder);

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(textField, fieldGbc);
        return textField;
    }

    private JTextField createStyledTextField(String placeholder) {
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
            textField.setForeground(PLACEHOLDER_COLOR);
            textField.addFocusListener(new PlaceholderFocusListener(placeholder, textField));
        }
        return textField;
    }

    private void addDatePicker(JPanel parent) {
        final JTextField dateField = createStyledTextField("DD/MM/YYYY");
        dateField.setEditable(false);
        dateField.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPopupMenu calendarPopup = new JPopupMenu();
        calendarPopup.setBorder(BorderFactory.createLineBorder(GOLD_COLOR));
        calendarPopup.setBackground(CALENDAR_BG);

        CalendarPanel calendarPanel = new CalendarPanel(dateField, calendarPopup);
        calendarPopup.add(calendarPanel);

        dateField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!calendarPopup.isVisible()) {
                    calendarPopup.show(dateField, 0, dateField.getHeight());
                } else {
                    calendarPopup.setVisible(false);
                }
            }
        });
        parent.add(dateField);
    }

    // Updated to return JComboBox
    private JComboBox<String> addComboBox(JPanel parent, String[] items, String defaultItem, GridBagConstraints gbc, int x, int y, int width) {
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

    // --- Custom Calendar Panel Logic ---
    private class CalendarPanel extends JPanel {
        private Calendar currentCalendar = Calendar.getInstance();
        private JLabel monthLabel;
        private JPanel daysPanel;
        private JTextField targetField;
        private JPopupMenu parentPopup;

        public CalendarPanel(JTextField target, JPopupMenu popup) {
            this.targetField = target;
            this.parentPopup = popup;
            setLayout(new BorderLayout());
            setBackground(CALENDAR_BG);
            setPreferredSize(new Dimension(300, 250));

            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(CALENDAR_BG);
            header.setBorder(new EmptyBorder(5, 5, 5, 5));

            JButton prevBtn = createArrowButton("<");
            prevBtn.addActionListener(e -> navigateMonth(-1));

            JButton nextBtn = createArrowButton(">");
            nextBtn.addActionListener(e -> navigateMonth(1));

            monthLabel = new JLabel("", JLabel.CENTER);
            monthLabel.setForeground(GOLD_COLOR);
            monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

            header.add(prevBtn, BorderLayout.WEST);
            header.add(monthLabel, BorderLayout.CENTER);
            header.add(nextBtn, BorderLayout.EAST);

            add(header, BorderLayout.NORTH);

            daysPanel = new JPanel(new GridLayout(0, 7, 5, 5));
            daysPanel.setBackground(CALENDAR_BG);
            daysPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
            add(daysPanel, BorderLayout.CENTER);

            updateCalendar();
        }

        private void navigateMonth(int offset) {
            currentCalendar.add(Calendar.MONTH, offset);
            updateCalendar();
        }

        private void updateCalendar() {
            daysPanel.removeAll();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
            monthLabel.setText(sdf.format(currentCalendar.getTime()));

            String[] weekDays = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
            for (String day : weekDays) {
                JLabel lbl = new JLabel(day, JLabel.CENTER);
                lbl.setForeground(Color.GRAY);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                daysPanel.add(lbl);
            }

            Calendar temp = (Calendar) currentCalendar.clone();
            temp.set(Calendar.DAY_OF_MONTH, 1);
            int startDay = temp.get(Calendar.DAY_OF_WEEK);
            int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 1; i < startDay; i++) daysPanel.add(new JLabel(""));

            for (int i = 1; i <= maxDays; i++) {
                final int day = i;
                JButton dayBtn = new JButton(String.valueOf(day));
                dayBtn.setContentAreaFilled(false);
                dayBtn.setBorderPainted(false);
                dayBtn.setFocusPainted(false);
                dayBtn.setForeground(TEXT_COLOR);
                dayBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                dayBtn.setMargin(new Insets(0,0,0,0));

                dayBtn.addActionListener(e -> selectDate(day));
                daysPanel.add(dayBtn);
            }
            daysPanel.revalidate();
            daysPanel.repaint();
        }

        private void selectDate(int day) {
            Calendar selectedDate = (Calendar) currentCalendar.clone();
            selectedDate.set(Calendar.DAY_OF_MONTH, day);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            targetField.setText(sdf.format(selectedDate.getTime()));
            parentPopup.setVisible(false);
        }

        private JButton createArrowButton(String text) {
            JButton btn = new JButton(text);
            btn.setForeground(GOLD_COLOR);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }
    }

    // --- Update Icon ---
    private class UpdateIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.setStroke(new BasicStroke(2f));

            int size = 14;
            g2.drawArc(x, y, size, size, 45, 270); // Circular arrow

            // Arrow head
            int arrowX = x + size - 3;
            int arrowY = y + 4;
            int[] px = {arrowX, arrowX + 4, arrowX};
            int[] py = {arrowY, arrowY + 3, arrowY + 6};
            g2.fillPolygon(px, py, 3);

            g2.dispose();
        }
        @Override
        public int getIconWidth() { return 16; }
        @Override
        public int getIconHeight() { return 16; }
    }
}