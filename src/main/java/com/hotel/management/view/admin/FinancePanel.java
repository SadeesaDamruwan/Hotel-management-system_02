package com.hotel.management.view.admin;

import com.hotel.management.model.FinancialRecord;
import com.hotel.management.view.components.StyledFormPanel;
import com.toedter.calendar.JDateChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class FinancePanel extends JPanel {

    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = new Color(255, 200, 100);
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color BLOCK_BACKGROUND = new Color(30, 30, 30, 220);


    // Styling constants
    private final Font KPI_TITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private final Font KPI_VALUE_FONT = new Font("Verdana", Font.BOLD, 48);
    private final Font SECTION_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);

    private final Font INPUT_LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font INPUT_FIELD_FONT = new Font("Verdana", Font.PLAIN, 14);
    private final Font BUTTON_FONT_SIZE = new Font("Segoe UI", Font.BOLD, 16);

    private BufferedImage backgroundImage;
    private final DailyReportTablePanel reportPanel;


    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContentPanel;

    private JLabel totalRevenueValueLabel;
    private JLabel totalExpensesValueLabel;
    private JLabel profitLossValueLabel;
    private JLabel totalRecordsValueLabel;

    private final java.text.DecimalFormat df = new java.text.DecimalFormat("'Rs. '#,##0.00");


    public FinancePanel() {
        reportPanel = new DailyReportTablePanel();
        loadBackgroundImage();

        setLayout(new BorderLayout(10, 10));

        mainContentPanel = createBackgroundPanel();
        mainContentPanel.setLayout(cardLayout);

        JPanel dashboardView = new JPanel(new BorderLayout());
        dashboardView.setOpaque(false);

        final JLabel mainTitle = new JLabel("Financial Records", SwingConstants.CENTER);
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        mainTitle.setForeground(GOLD_COLOR);
        mainTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        dashboardView.add(mainTitle, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);


        JPanel dashboardContent = createDashboardContentPanel();

        centerWrapper.add(dashboardContent, new GridBagConstraints());
        dashboardView.add(centerWrapper, BorderLayout.CENTER);

        mainContentPanel.add(dashboardView, "Dashboard");

        add(mainContentPanel, BorderLayout.CENTER);

        calculateProfitLoss();
    }


    private JPanel createDashboardContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 30));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel summaryContainer = createDashboardSummaryPanel();
        JPanel actionButtonPanel = createActionButtonPanel();

        contentPanel.add(summaryContainer, BorderLayout.NORTH);
        contentPanel.add(actionButtonPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private class SummaryDataBlock extends JPanel {
        private static final int BLOCK_RADIUS = 8;

        public SummaryDataBlock(String title, JLabel valueLabel, Color valueColor) {
            setLayout(new BorderLayout(0, 5));
            setOpaque(false);
            setBackground(BLOCK_BACKGROUND);
            setBorder(new EmptyBorder(25, 20, 25, 20));

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(KPI_TITLE_FONT);
            titleLabel.setForeground(LABEL_COLOR);

            valueLabel.setFont(KPI_VALUE_FONT);
            valueLabel.setForeground(valueColor);
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

            add(titleLabel, BorderLayout.NORTH);
            add(valueLabel, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), BLOCK_RADIUS, BLOCK_RADIUS));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // DASHBOARD SUMMARY PANEL
    private JPanel createDashboardSummaryPanel() {
        JPanel summaryContainer = new JPanel(new BorderLayout(0, 30));
        summaryContainer.setOpaque(false);

        JPanel kpiBlocksPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        kpiBlocksPanel.setOpaque(false);

        totalRevenueValueLabel = new JLabel("Rs. 0.00");
        totalExpensesValueLabel = new JLabel("Rs. 0.00");
        profitLossValueLabel = new JLabel("Rs. 0.00");

        // All KPI value labels use the same LABEL_COLOR (Gold/Orange)
        kpiBlocksPanel.add(new SummaryDataBlock("Total Revenue", totalRevenueValueLabel, LABEL_COLOR));
        kpiBlocksPanel.add(new SummaryDataBlock("Total Expenses", totalExpensesValueLabel, LABEL_COLOR));
        kpiBlocksPanel.add(new SummaryDataBlock("Net Profit/Loss", profitLossValueLabel, LABEL_COLOR));

        final int recordsBlockRadius = 15;
        JPanel detailBlockWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), recordsBlockRadius, recordsBlockRadius));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        detailBlockWrapper.setOpaque(false);
        detailBlockWrapper.setBackground(BLOCK_BACKGROUND);
        detailBlockWrapper.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel detailTitle = new JLabel("", SwingConstants.LEFT);
        detailTitle.setFont(SECTION_TITLE_FONT);
        detailTitle.setForeground(GOLD_COLOR);
        detailBlockWrapper.add(detailTitle, BorderLayout.NORTH);

        totalRecordsValueLabel = new JLabel("Total Records: 0", SwingConstants.CENTER);
        totalRecordsValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalRecordsValueLabel.setForeground(LABEL_COLOR);

        JPanel placeholderDetail = new JPanel(new FlowLayout(FlowLayout.CENTER));
        placeholderDetail.setOpaque(false);
        placeholderDetail.add(totalRecordsValueLabel);

        detailBlockWrapper.add(placeholderDetail, BorderLayout.CENTER);

        summaryContainer.add(kpiBlocksPanel, BorderLayout.NORTH);
        summaryContainer.add(detailBlockWrapper, BorderLayout.CENTER);

        return summaryContainer;
    }

    // ACTION BUTTON PANEL
    private JPanel createActionButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panel.setOpaque(false);

        // All buttons use GOLD_COLOR background and BLACK foreground
        JButton addRevenueBtn = createActionButton("➕ Add Revenue", GOLD_COLOR, Color.BLACK, e -> showRevenueForm());
        JButton addExpenseBtn = createActionButton("➖ Add Expense", GOLD_COLOR, Color.BLACK, e -> showExpenseForm());
        JButton viewReportBtn = createActionButton("\uD83D\uDCB0 View Full Report", GOLD_COLOR, Color.BLACK, e -> openReportWindow());

        panel.add(addRevenueBtn);
        panel.add(addExpenseBtn);
        panel.add(viewReportBtn);

        return panel;
    }

    //PANEL MANAGEMENT METHODS

    private void showRevenueForm() {
        final Color formBgColor = new Color(0, 0, 0, 150);
        StyledFormPanel formPanel = createRevenueFormPanel(formBgColor);
        mainContentPanel.add(formPanel, "RevenueForm");
        cardLayout.show(mainContentPanel, "RevenueForm");
    }

    private void showExpenseForm() {
        final Color formBgColor = new Color(0, 0, 0, 150);
        StyledFormPanel formPanel = createExpenseFormPanel(formBgColor);
        mainContentPanel.add(formPanel, "ExpenseForm");
        cardLayout.show(mainContentPanel, "ExpenseForm");
    }

    private void hideFormPanel(String formName) {
        cardLayout.show(mainContentPanel, "Dashboard");
        for (Component comp : mainContentPanel.getComponents()) {
            if (comp instanceof StyledFormPanel && Objects.equals(comp.getName(), formName)) {
                mainContentPanel.remove(comp);
                mainContentPanel.revalidate();
                mainContentPanel.repaint();
                break;
            }
        }
    }

    private StyledFormPanel createRevenueFormPanel(Color formBgColor) {
        final String formName = "RevenueForm";
        StyledFormPanel formPanel = new StyledFormPanel("Record New Revenue", formName, formBgColor, GOLD_COLOR);

        final JTextField amountField = createStyledTextField(15);
        final JTextField clientVendorField = createStyledTextField(15);
        final JTextField refIdField = createStyledTextField(15);
        final JComboBox<String> categoryCombo = createStyledComboBox(new String[]{"Sales", "Service Fee", "Interest"});
        final JComboBox<String> taxRateCombo = createStyledComboBox(new String[]{"0%", "5%", "10%", "18%"});
        final JComboBox<String> paidViaCombo = createStyledComboBox(new String[]{"Cash", "Card", "Bank Transfer"});
        final JDateChooser dateChooser = createStyledDateChooser();
        final JTextArea descArea = createStyledTextArea(3, 15);
        final JScrollPane descScrollPane = new JScrollPane(descArea);

        descScrollPane.getViewport().setBackground(new Color(30, 30, 30));
        descScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;
        row = addInputRow(inputPanel, "Date:", dateChooser, gbc, row);
        row = addInputRow(inputPanel, "Client:", clientVendorField, gbc, row);
        row = addInputRow(inputPanel, "Ref. ID:", refIdField, gbc, row);
        row = addInputRow(inputPanel, "Amount (Rs.):", amountField, gbc, row);
        row = addInputRow(inputPanel, "Category:", categoryCombo, gbc, row);
        row = addInputRow(inputPanel, "Tax Rate:", taxRateCombo, gbc, row);
        row = addInputRow(inputPanel, "Paid Via:", paidViaCombo, gbc, row);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0;
        gbc.insets = new Insets(15, 10, 5, 10);
        inputPanel.add(createStyledLabel("Description / Notes:"), gbc);
        row++;

        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 20, 10);
        inputPanel.add(descScrollPane, gbc);

        formPanel.setMainInputPanel(inputPanel);

        // Record Button
        JButton recordButton = createActionButton("Record Revenue", GOLD_COLOR, Color.BLACK, e -> {
            try {
                Double amount = parseDoubleStrict(amountField.getText(), "Amount");
                if (amount == null) return;

                String date = new SimpleDateFormat("dd/MM/yyyy").format(Objects.requireNonNull(dateChooser.getDate()));
                String category = (String) categoryCombo.getSelectedItem();
                String refId = refIdField.getText();
                String clientVendor = clientVendorField.getText();
                String taxRate = (String) taxRateCombo.getSelectedItem();
                String paidVia = (String) paidViaCombo.getSelectedItem();
                String description = descArea.getText();

                String fullDescription = String.format("Client: %s | Ref: %s | Tax: %s | Via: %s | Note: %s",
                        clientVendor, refId, taxRate, paidVia, description);

                FinancialRecord newRevenue = new FinancialRecord(date, "Revenue", category, amount, fullDescription);
                reportPanel.addRecord(newRevenue);
                calculateProfitLoss();

                hideFormPanel(formName);
                JOptionPane.showMessageDialog(this, "Revenue recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please check all fields, especially the Date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel Button
        JButton cancelButton = createActionButton("Cancel", Color.GRAY.darker(), Color.WHITE, e -> hideFormPanel(formName));

        formPanel.addButton(recordButton);
        formPanel.addButton(cancelButton);

        return formPanel;
    }

    private StyledFormPanel createExpenseFormPanel(Color formBgColor) {
        final String formName = "ExpenseForm";
        StyledFormPanel formPanel = new StyledFormPanel("Record New Expense", formName, formBgColor, GOLD_COLOR);

        final JTextField amountField = createStyledTextField(15);
        final JTextField clientVendorField = createStyledTextField(15);
        final JTextField refIdField = createStyledTextField(15);
        final JComboBox<String> categoryCombo = createStyledComboBox(new String[]{"Salary", "Maintenance", "Supplies", "Utilities"});
        final JComboBox<String> taxRateCombo = createStyledComboBox(new String[]{"0%", "5%", "10%", "18%"});
        final JComboBox<String> paidViaCombo = createStyledComboBox(new String[]{"Cash", "Check", "Bank Transfer"});
        final JDateChooser dateChooser = createStyledDateChooser();
        final JTextArea descArea = createStyledTextArea(3, 15);
        final JScrollPane descScrollPane = new JScrollPane(descArea);

        descScrollPane.getViewport().setBackground(new Color(30, 30, 30));
        descScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        row = addInputRow(inputPanel, "Date:", dateChooser, gbc, row);
        row = addInputRow(inputPanel, "Vendor:", clientVendorField, gbc, row);
        row = addInputRow(inputPanel, "Ref. ID:", refIdField, gbc, row);
        row = addInputRow(inputPanel, "Amount (Rs.):", amountField, gbc, row);
        row = addInputRow(inputPanel, "Category:", categoryCombo, gbc, row);
        row = addInputRow(inputPanel, "Tax Rate:", taxRateCombo, gbc, row);
        row = addInputRow(inputPanel, "Paid Via:", paidViaCombo, gbc, row);


        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0;
        gbc.insets = new Insets(15, 10, 5, 10);
        inputPanel.add(createStyledLabel("Description / Notes:"), gbc);

        row++;

        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 20, 10);
        inputPanel.add(descScrollPane, gbc);

        formPanel.setMainInputPanel(inputPanel);

        // Record Button
        JButton recordButton = createActionButton("Record Expense", GOLD_COLOR, Color.BLACK, e -> {
            try {
                Double amount = parseDoubleStrict(amountField.getText(), "Amount");
                if (amount == null) return;

                String date = new SimpleDateFormat("dd/MM/yyyy").format(Objects.requireNonNull(dateChooser.getDate()));
                String category = (String) categoryCombo.getSelectedItem();
                String refId = refIdField.getText();
                String clientVendor = clientVendorField.getText();
                String taxRate = (String) taxRateCombo.getSelectedItem();
                String paidVia = (String) paidViaCombo.getSelectedItem();
                String description = descArea.getText();

                String fullDescription = String.format("Vendor: %s | Ref: %s | Tax: %s | Via: %s | Note: %s",
                        clientVendor, refId, taxRate, paidVia, description);

                FinancialRecord newExpense = new FinancialRecord(date, "Expense", category, amount, fullDescription);
                reportPanel.addRecord(newExpense);
                calculateProfitLoss();

                hideFormPanel(formName);
                JOptionPane.showMessageDialog(this, "Expense recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please check all fields, especially the Date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel Button
        JButton cancelButton = createActionButton("Cancel", Color.GRAY.darker(), Color.WHITE, e -> hideFormPanel(formName));

        formPanel.addButton(recordButton);
        formPanel.addButton(cancelButton);

        return formPanel;
    }

    // Helper to streamline adding rows in the panels
    private int addInputRow(JPanel panel, String labelText, Component inputComponent, GridBagConstraints gbc, int row) {
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(createStyledLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(inputComponent, gbc);

        return row + 1;
    }

    // STYLING HELPER METHODS

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(INPUT_LABEL_FONT);
        label.setForeground(LABEL_COLOR);
        return label;
    }

    private JTextField createStyledTextField(int cols) {
        JTextField field = new JTextField(cols);
        field.setFont(INPUT_FIELD_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBackground(new Color(30, 30, 30));
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR,1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 30));
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(INPUT_FIELD_FONT);
        combo.setForeground(Color.BLACK);
        combo.setBackground(Color.WHITE);
        combo.setEditable(false);
        combo.setPreferredSize(new Dimension(combo.getPreferredSize().width, 30));
        return combo;
    }

    private JDateChooser createStyledDateChooser() {
        JDateChooser dateChooser = new JDateChooser(new Date());
        dateChooser.setFont(INPUT_FIELD_FONT);
        dateChooser.setPreferredSize(new Dimension(200, 35));
        JComponent dateEditor = dateChooser.getDateEditor().getUiComponent();
        dateEditor.setFont(INPUT_FIELD_FONT);
        dateEditor.setForeground(TEXT_COLOR);
        dateEditor.setBackground(new Color(30, 30, 30));
        dateEditor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        return dateChooser;
    }

    private JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(INPUT_FIELD_FONT);
        area.setForeground(TEXT_COLOR);
        area.setBackground(new Color(30, 30, 30));
        area.setCaretColor(TEXT_COLOR);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return area;
    }


    private void loadBackgroundImage() {
        try {
            final String imagePath = "/images/1303755c519208ef762b4077b243707e.jpg";
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath)));
        } catch (IOException | NullPointerException e) {
            System.err.println("Could not load background image: " + e.getMessage() + ". Using default.");
        }
    }

    private JPanel createBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
    }

    private void openReportWindow() {
        JFrame window = new JFrame("Financial Records");
        window.add(reportPanel);
        window.setSize(1000, 800);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void calculateProfitLoss() {
        AtomicReference<Double> totalRevenue = new AtomicReference<>(0.0);
        AtomicReference<Double> totalExpense = new AtomicReference<>(0.0);

        reportPanel.getRecords().forEach(record -> {
            if (record.getType().equals("Revenue")) {
                totalRevenue.updateAndGet(v -> v + record.getAmount());
            } else if (record.getType().equals("Expense")) {
                totalExpense.updateAndGet(v -> v + record.getAmount());
            }
        });

        final double netProfit = totalRevenue.get() - totalExpense.get();

        SwingUtilities.invokeLater(() -> {
            totalRevenueValueLabel.setText(df.format(totalRevenue.get()));
            totalExpensesValueLabel.setText(df.format(totalExpense.get()));
            profitLossValueLabel.setText(df.format(netProfit));
            totalRecordsValueLabel.setText("Total Records: " + reportPanel.getRecords().size());

            // Set color to the same gold/orange as other values
            profitLossValueLabel.setForeground(LABEL_COLOR);
        });
    }

    private JButton createActionButton(final String text, final Color bgColor, final Color fgColor, final java.awt.event.ActionListener listener) {
        final JButton button = new JButton(text);
        button.setFont(BUTTON_FONT_SIZE);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        button.setOpaque(true);
        if (listener != null) {
            button.addActionListener(listener);
        }
        return button;
    }


    private Double parseDoubleStrict(String val, final String fieldName) {
        try {
            val = val.trim();
            if (val.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a value for " + fieldName + ".",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            final double d = Double.parseDouble(val);
            if (d <= 0) {
                JOptionPane.showMessageDialog(this,
                        fieldName + " must be a positive number.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return d;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for " + fieldName + ".",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(final String[] args) {
        final JFrame frame = new JFrame("Hotel Financial Reporting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null);
        frame.add(new FinancePanel());
        frame.setVisible(true);
    }
}
