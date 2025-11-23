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
import java.util.GregorianCalendar;
import java.util.Random;
import javax.swing.text.JTextComponent;

public class AddStaffPanel extends JPanel {

    // --- Color Constants ---
    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);
    private final Color CALENDAR_BG = new Color(40, 40, 40);

    public AddStaffPanel() {
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

        // --- Form Layout Configuration ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel titleLabel = new JLabel("Add New Staff");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 15, 40, 15);
        formContainer.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 15, 5, 15);

        // --- Row 1: Full Name ---
        gbc.gridy++;
        addLabel(formContainer, "Full Name", gbc, 0, gbc.gridy, 4);
        gbc.gridy++;
        addTextField(formContainer, "", gbc, 0, gbc.gridy, 4);

        // --- Row 2: Date of Birth & Staff Id ---
        gbc.gridy++;
        addLabel(formContainer, "Date Of Birth", gbc, 0, gbc.gridy, 2);
        addLabel(formContainer, "Staff Id", gbc, 2, gbc.gridy, 2);

        gbc.gridy++;
        // Custom Calendar Picker
        addDatePicker(formContainer, gbc, 0, gbc.gridy, 2);

        // UPDATED: Auto-generated Staff ID (Non-editable)
        String autoId = "STF-" + (1000 + new Random().nextInt(9000)); // Generates STF-XXXX
        addAutoIdField(formContainer, autoId, gbc, 2, gbc.gridy, 2);

        // --- Row 3: NIC/Passport & Dropdowns ---
        gbc.gridy++;
        addLabel(formContainer, "NIC/Passport No.", gbc, 0, gbc.gridy, 2);

        gbc.gridy++;
        addTextField(formContainer, "", gbc, 0, gbc.gridy, 2);

        // Dropdowns
        addComboBox(formContainer, new String[]{"Mr", "Ms", "Mrs"}, "Mr", gbc, 2, gbc.gridy, 1);
        addComboBox(formContainer, new String[]{"Role", "Admin", "Manager", "Staff"}, "Role", gbc, 3, gbc.gridy, 1);

        // --- Row 4: Email & Phone ---
        gbc.gridy++;
        addLabel(formContainer, "Email Address", gbc, 0, gbc.gridy, 2);
        addLabel(formContainer, "Phone number", gbc, 2, gbc.gridy, 2);

        gbc.gridy++;
        addTextField(formContainer, "", gbc, 0, gbc.gridy, 2);
        addTextField(formContainer, "", gbc, 2, gbc.gridy, 2);

        // --- Row 5: Buttons ---
        gbc.gridy++;
        gbc.insets = new Insets(30, 15, 10, 15);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        buttonPanel.setOpaque(false);

        JButton addAnotherBtn = new JButton("+  Add another");
        addAnotherBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addAnotherBtn.setForeground(GOLD_COLOR);
        addAnotherBtn.setContentAreaFilled(false);
        addAnotherBtn.setBorderPainted(false);
        addAnotherBtn.setFocusPainted(false);
        addAnotherBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(addAnotherBtn);

        JButton addBtn = new JButton("Add") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(180, 180, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        addBtn.setPreferredSize(new Dimension(100, 35));
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(new Color(181, 139, 0));
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(addBtn);

        gbc.gridx = 0;
        gbc.gridwidth = 4;
        formContainer.add(buttonPanel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);
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

    // --- NEW: Auto ID Field Helper ---
    private void addAutoIdField(JPanel parent, String value, GridBagConstraints gbc, int x, int y, int width) {
        JTextField textField = new JTextField(value) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        textField.setOpaque(false);
        textField.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Bold for ID
        textField.setForeground(TEXT_COLOR); // White text (not gray placeholder)
        textField.setCaretColor(GOLD_COLOR);
        textField.setEditable(false); // Read-only
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(textField, fieldGbc);
    }

    private void addDatePicker(JPanel parent, GridBagConstraints gbc, int x, int y, int width) {
        final JTextField dateField = new JTextField("dd/MM/yyyy") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        dateField.setEditable(false);
        dateField.setOpaque(false);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setForeground(TEXT_COLOR);
        dateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
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

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = x;
        fieldGbc.gridy = y;
        fieldGbc.gridwidth = width;
        parent.add(dateField, fieldGbc);
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
                        int w = 8;
                        int h = 5;
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

            // Header (Month/Year Navigation)
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

            // Days Grid
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

            // Update Month Label
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
            monthLabel.setText(sdf.format(currentCalendar.getTime()));

            // Add Week Headers
            String[] weekDays = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
            for (String day : weekDays) {
                JLabel lbl = new JLabel(day, JLabel.CENTER);
                lbl.setForeground(Color.GRAY);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                daysPanel.add(lbl);
            }

            // Calculate blank days
            Calendar temp = (Calendar) currentCalendar.clone();
            temp.set(Calendar.DAY_OF_MONTH, 1);
            int startDay = temp.get(Calendar.DAY_OF_WEEK); // 1=Sunday
            int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

            // Add empty slots for days before 1st of month
            for (int i = 1; i < startDay; i++) {
                daysPanel.add(new JLabel(""));
            }

            // Add Day Buttons
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

                if (isToday(temp, day)) {
                    dayBtn.setForeground(GOLD_COLOR);
                    dayBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    dayBtn.setBorder(BorderFactory.createLineBorder(GOLD_COLOR));
                }

                dayBtn.addActionListener(e -> {
                    selectDate(day);
                });

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
            targetField.setForeground(TEXT_COLOR);
            parentPopup.setVisible(false);
        }

        private boolean isToday(Calendar cal, int day) {
            Calendar today = Calendar.getInstance();
            return today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                    today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) == day;
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
}