package com.hotel.management.view;

import com.hotel.management.view.admin.AdminLoginFrame;
import com.hotel.management.view.receptionist.ReceptionistLoginFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class LoginSelectionFrame extends JFrame {

    private BufferedImage backgroundImage;

    // --- Colors ---
    private final Color ADMIN_BG = new Color(0, 0, 0, 180);
    private final Color ADMIN_TEXT = new Color(255, 180, 60); // Gold
    private final Color RECEPTION_BG = new Color(255, 255, 255, 180);
    private final Color RECEPTION_TEXT = Color.BLACK;

    public LoginSelectionFrame() {
        setTitle("Hotel Management System - Welcome");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Load Background ---
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/a170b7e6b576d72403c665e6337322e1.jpg")));
        } catch (Exception e) {
            System.err.println("Background image not found.");
        }

        // --- Main Panel ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                // Dark Overlay for contrast
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // --- Content Container ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 50, 0);

        // Header
        JLabel titleLabel = new JLabel("Welcome to Innova");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        contentPanel.add(titleLabel, gbc);

        gbc.gridy++;
        JLabel subTitle = new JLabel("Select your role to continue");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subTitle.setForeground(new Color(220, 220, 220));
        contentPanel.add(subTitle, gbc);

        // --- Selection Cards ---
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 30, 20, 30);

        // 1. Admin Card
        JPanel adminCard = createSelectionCard("Admin", "", ADMIN_BG, ADMIN_TEXT, true);
        adminCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Close selection
                new AdminLoginFrame().setVisible(true); // Open Admin Login
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                adminCard.setBorder(BorderFactory.createLineBorder(ADMIN_TEXT, 2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                adminCard.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Invisible border to maintain size
            }
        });
        gbc.gridx = 0;
        contentPanel.add(adminCard, gbc);

        // 2. Receptionist Card
        JPanel receptionCard = createSelectionCard("Receptionist", "", RECEPTION_BG, RECEPTION_TEXT, false);
        receptionCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Close selection
                new ReceptionistLoginFrame().setVisible(true); // Open Reception Login
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                receptionCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                receptionCard.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            }
        });
        gbc.gridx = 1;
        contentPanel.add(receptionCard, gbc);

        backgroundPanel.add(contentPanel);
        add(backgroundPanel);
        setVisible(true);
    }

    private JPanel createSelectionCard(String title, String desc, Color bg, Color fg, boolean isAdmin) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 30, 30);

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 30, 30);

                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 30),
                    0, getHeight() / 2, new Color(255, 255, 255, 0)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() / 2, 30, 30);
                
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(280, 350));
        card.setMinimumSize(new Dimension(280, 350));
        card.setMaximumSize(new Dimension(280, 350));
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Initial empty border

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(30, 10, 20, 10);

        // Icon with background circle
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Circle background for icon
                g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 20));
                g2.fillOval(10, 10, 100, 100);
                
                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(120, 120));
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel icon = new JLabel(new RoleIcon(fg, isAdmin));
        iconPanel.add(icon);
        card.add(iconPanel, gbc);

        // Title with increased spacing
        gbc.gridy++;
        gbc.insets = new Insets(20, 10, 10, 10);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(fg);
        card.add(titleLabel, gbc);

        // Description (hidden but spacing maintained)
        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 30, 10);
        JLabel descLabel = new JLabel("<html><center>" + desc + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(fg);
        descLabel.setPreferredSize(new Dimension(200, 0));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(descLabel, gbc);

        // Enhanced button visual with rounded background
        gbc.gridy++;
        gbc.insets = new Insets(10, 10, 30, 10);
        
        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Button background
                g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 25));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                
                g2.dispose();
            }
        };
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(160, 50));
        buttonPanel.setMinimumSize(new Dimension(160, 50));
        buttonPanel.setMaximumSize(new Dimension(160, 50));
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        JLabel arrowLabel = new JLabel("Login \u2192"); 
        arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        arrowLabel.setForeground(fg);
        buttonPanel.add(arrowLabel);
        
        card.add(buttonPanel, gbc);

        return card;
    }

    // Custom Icon Drawing
    private class RoleIcon implements Icon {
        private Color color;
        private boolean isAdmin;

        public RoleIcon(Color color, boolean isAdmin) {
            this.color = color;
            this.isAdmin = isAdmin;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);

            if (isAdmin) {
                // Admin Shield/Lock Icon
                g2.fillRoundRect(x+20, y+20, 40, 40, 10, 10); // Body
                g2.setStroke(new BasicStroke(4));
                g2.drawArc(x+20, y, 40, 40, 0, 180); // Handle
                // Tie
                g2.setColor(color.equals(Color.WHITE) ? Color.BLACK : new Color(0,0,0,50));
                int[] tx = {x+40, x+30, x+40, x+50};
                int[] ty = {x+20, x+60, x+50, x+60};
                g2.fillPolygon(tx, ty, 4);
            } else {
                // Receptionist Bell Icon
                g2.fillArc(x+10, y+20, 60, 50, 0, 180); // Bell dome
                g2.fillRect(x+40, y+10, 6, 10); // Button
                g2.fillRect(x+5, y+45, 70, 6); // Base
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return 80; }
        @Override
        public int getIconHeight() { return 80; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginSelectionFrame::new);
    }
}