package com.hotel.management.view.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class AdminSettingsPanel extends JPanel {

    // --- Color Constants ---
    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = new Color(180, 180, 180); // Softer gray for labels
    private final Color TEXT_COLOR = Color.WHITE;               // Bright white for values
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150);
    private final Color PROFILE_BG = new Color(40, 40, 40);
    private final Color SEPARATOR_COLOR = new Color(60, 60, 60);

    // Placeholder image variable
    private BufferedImage profileImage;
    private JLabel photoLabel;

    // List to track editable fields for toggling
    private List<JTextField> editableFields = new ArrayList<>();
    private JButton saveBtn;

    public AdminSettingsPanel() {
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

        // --- Title ---
        JLabel titleLabel = new JLabel("Admin Profile Settings");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 50, 0);
        formContainer.add(titleLabel, gbc);

        // --- Content Area ---
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.NORTH;

        // --- Left Column: Profile Photo ---
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        formContainer.add(createPhotoPanel(), gbc);

        // --- Right Column: Compact Details List ---
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(0, 40, 0, 0); // Spacing between photo and details
        formContainer.add(createCompactDetailsPanel(), gbc);

        // --- Vertical Spacer ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        formContainer.add(Box.createVerticalGlue(), gbc);
    }

    private JPanel createPhotoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Circular Photo Placeholder
        photoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (profileImage == null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(PROFILE_BG);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.setColor(new Color(150, 150, 150));
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 16));

                    FontMetrics fm = g2.getFontMetrics();
                    String text = "No Photo";
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(text, x, y);
                    g2.dispose();
                }
            }
        };
        photoLabel.setPreferredSize(new Dimension(160, 160));
        photoLabel.setMinimumSize(new Dimension(160, 160));
        photoLabel.setMaximumSize(new Dimension(160, 160));
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadDefaultImage();

        JButton uploadBtn = new JButton("Change Photo");
        uploadBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uploadBtn.setForeground(GOLD_COLOR);
        uploadBtn.setContentAreaFilled(false);
        uploadBtn.setBorderPainted(false);
        uploadBtn.setFocusPainted(false);
        uploadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadBtn.addActionListener(e -> selectAndDisplayImage());

        panel.add(photoLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(uploadBtn);

        return panel;
    }

    private void loadDefaultImage() {
        profileImage = null;
        photoLabel.repaint();
    }

    private void selectAndDisplayImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage original = ImageIO.read(fileChooser.getSelectedFile());
                if (original != null) {
                    profileImage = original;
                    // Create circular mask
                    BufferedImage circular = new BufferedImage(160, 160, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = circular.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 160, 160));
                    g2.drawImage(profileImage.getScaledInstance(160, 160, Image.SCALE_SMOOTH), 0, 0, null);
                    g2.dispose();
                    photoLabel.setIcon(new ImageIcon(circular));
                }
            } catch (IOException ex) { /* Handle error */ }
        }
    }

    private JPanel createCompactDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0); // Padding between rows
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        int y = 0;

        // --- Header Row with Edit Button ---
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        JLabel headerLabel = new JLabel("Personal Information");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(TEXT_COLOR);
        panel.add(headerLabel, gbc);

        // Edit Button (Pen Icon)
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;

        JButton editBtn = new JButton(new PenIcon());
        editBtn.setContentAreaFilled(false);
        editBtn.setBorderPainted(false);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setToolTipText("Edit Details");
        editBtn.addActionListener(e -> toggleEditMode(true));
        panel.add(editBtn, gbc);

        // Reset for fields
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        y++;

        // --- Detail Rows ---
        y = addDetailRow(panel, gbc, "First Name", "John", y);
        y = addSeparator(panel, gbc, y);

        y = addDetailRow(panel, gbc, "Last Name", "Doe", y);
        y = addSeparator(panel, gbc, y);

        y = addDetailRow(panel, gbc, "Email ID", "admin@hotel.com", y);
        y = addSeparator(panel, gbc, y);

        y = addDetailRow(panel, gbc, "Phone", "+1 234 567 890", y);
        y = addSeparator(panel, gbc, y);

        y = addDetailRow(panel, gbc, "Location", "New York, USA", y);
        y = addSeparator(panel, gbc, y);

        y = addDetailRow(panel, gbc, "Bio", "Senior Administrator managing daily operations...", y);

        // --- Save Button (CENTERED) ---
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 3; // Span all columns
        gbc.insets = new Insets(30, 0, 0, 0); // Padding top
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        gbc.fill = GridBagConstraints.NONE; // Do not stretch

        saveBtn = new JButton("Save Changes") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Rounded
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        saveBtn.setPreferredSize(new Dimension(150, 40));
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setBackground(GOLD_COLOR); // Match Gold Theme
        saveBtn.setForeground(Color.BLACK); // Black Text
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setContentAreaFilled(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setVisible(false); // Hidden initially

        saveBtn.addActionListener(e -> {
            toggleEditMode(false);
            JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(saveBtn, gbc);

        return panel;
    }

    private int addDetailRow(JPanel parent, GridBagConstraints gbc, String labelText, String valueText, int y) {
        // Label (e.g., "First Name")
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.WEST; // Left align label

        JLabel label = new JLabel(labelText);
        label.setForeground(LABEL_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        parent.add(label, gbc);

        // Value (Editable Field that looks like a Label initially)
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.gridwidth = 2; // Span to the end

        JTextField valueField = new JTextField(valueText);
        valueField.setForeground(TEXT_COLOR);
        valueField.setFont(new Font("Segoe UI", Font.BOLD, 15));
        valueField.setOpaque(false);
        valueField.setEditable(false);
        valueField.setBorder(null); // No border implies "View Mode"
        valueField.setCaretColor(GOLD_COLOR);
        valueField.setSelectionColor(new Color(255, 180, 60)); // Fully opaque selection
        valueField.setSelectedTextColor(Color.BLACK); // Black text when selected

        // Add to tracking list
        editableFields.add(valueField);

        parent.add(valueField, gbc);

        return y + 1;
    }

    private void toggleEditMode(boolean enable) {
        for (JTextField field : editableFields) {
            field.setEditable(enable);
            if (enable) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GOLD_COLOR, 1),
                        BorderFactory.createEmptyBorder(2, 5, 2, 5)
                ));
                field.setOpaque(true);
                field.setBackground(new Color(50, 50, 50)); // Fully opaque - no transparency
                field.setSelectionColor(new Color(255, 180, 60)); // Fully opaque selection
                field.setSelectedTextColor(Color.BLACK); // Black text when selected
            } else {
                field.setBorder(null);
                field.setOpaque(false);
                field.setBackground(null);
            }
        }
        saveBtn.setVisible(enable);
        revalidate();
        repaint();
    }

    private int addSeparator(JPanel parent, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 0, 0); // Tight spacing for line
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JSeparator separator = new JSeparator();
        separator.setForeground(SEPARATOR_COLOR);
        separator.setBackground(new Color(0,0,0,0)); // Transparent bg
        parent.add(separator, gbc);

        // Reset
        gbc.gridwidth = 1;
        gbc.insets = new Insets(12, 0, 12, 0); // Restore padding
        return y + 1;
    }

    // --- Custom Pen Icon ---
    private class PenIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(GOLD_COLOR);

            // Draw a styled pencil
            Path2D path = new Path2D.Double();
            path.moveTo(x + 10, y + 2); // Top Right
            path.lineTo(x + 13, y + 5);
            path.lineTo(x + 5, y + 13); // Bottom Left
            path.lineTo(x + 2, y + 13);
            path.lineTo(x + 2, y + 10);
            path.closePath();

            g2.fill(path);
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return 16; }
        @Override
        public int getIconHeight() { return 16; }
    }
}