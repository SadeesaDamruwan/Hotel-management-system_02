package com.hotel.management.view.admin;

import com.hotel.management.model.Booking;
import com.hotel.management.model.Room;
import com.hotel.management.model.Staff;
import com.hotel.management.service.GuestActivityManagement;
import com.hotel.management.service.GuestBookingService;
import com.hotel.management.service.ReceptionistStaffManagement;
import com.hotel.management.service.RoomService;
import com.hotel.management.service.StaffService;
import com.hotel.management.service.UserService;
import com.hotel.management.view.LoginSelectionFrame;
import com.hotel.management.view.components.CircleProgressPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class AdminDashboardFrame extends JFrame {

    private BufferedImage backgroundImage;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    
    // Track currently selected navigation button
    private JButton currentlySelectedButton;
    private JButton currentlySelectedSubMenuButton; // Track selected submenu item
    
    // Track dropdown menus for collapse functionality
    private java.util.List<JPanel> dropdownSubMenus = new java.util.ArrayList<>();
    private java.util.List<JButton> dropdownMainButtons = new java.util.ArrayList<>();

    private final ReceptionistStaffManagement receptionistBackend = new ReceptionistStaffManagement();
    // Guest activity backend
    private final GuestActivityManagement guestActivityBackend = new GuestActivityManagement();



    // --- Color Constants ---
    private final Color SELECTED_COLOR = new Color(255, 180, 60);
    private final Color SELECTED_TEXT_COLOR = new Color(30, 30, 30);
    private final Color DEFAULT_COLOR = new Color(45, 45, 48, 0);
    private final Color HOVER_COLOR = new Color(70, 70, 75, 180);
    private final Color SUB_ITEM_HOVER_COLOR = new Color(255, 180, 60);
    private final Color SUB_MENU_BG = new Color(25, 25, 28, 200);
    private final Color SIDEBAR_BG_TOP = new Color(28, 28, 32);
    private final Color SIDEBAR_BG_BOTTOM = new Color(20, 20, 24);
    private final Color SEPARATOR_COLOR = new Color(60, 60, 65, 100);

    public AdminDashboardFrame() {
        // --- Frame Setup ---
        setTitle("Hotel Management System - Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // --- Load Background Image ---
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/a170b7e6b576d72403c665e6337322e1.jpg")));
        } catch (IOException e) {
            System.err.println("Dashboard background image not found.");
            backgroundImage = null;
        } catch (IllegalArgumentException e) {
            System.err.println("Dashboard resource path is null.");
            backgroundImage = null;
        }

        // --- Main Background Panel ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(20, 20, 20));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // --- Header and Main View Container ---
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        // 1. TOP BAR
        JPanel topBar = createTopBar();
        topBar.setOpaque(false);
        container.add(topBar, BorderLayout.NORTH);

        // 2. MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(10, 0));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 2a. SIDEBAR
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // 2b. DASHBOARD CONTENT (CardLayout)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setOpaque(false);
        GuestActivityManagement guestActivityBackend = new GuestActivityManagement();


        // --- ADDING THE VIEWS ---
        mainContentPanel.add(createDashboardHome(), "Dashboard");
        mainContentPanel.add(new AddStaffPanel(), "AddStaff");
        mainContentPanel.add(new RemoveStaffPanel(), "DeleteStaff");
        mainContentPanel.add(new ViewStaffPanel(), "ViewStaff");
        mainContentPanel.add(new SupportTicketsPanel(), "SupportTickets");
        mainContentPanel.add(new PriceAdjustmentPanel(), "PriceAdjustment");
        mainContentPanel.add(new AddRoomPanel(), "AddRooms"); // Renamed to "Update Status" in UI, keeping ID same
        mainContentPanel.add(new AddNewRoomPanel(), "AddNewRoom");
        mainContentPanel.add(new RemoveRoomPanel(), "RemoveRoom");
        mainContentPanel.add(new ViewReportsPanel(), "ViewReports");
        mainContentPanel.add(new AdminSettingsPanel(), "Settings");

        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        container.add(mainPanel, BorderLayout.CENTER);
        backgroundPanel.add(container, BorderLayout.CENTER);
        add(backgroundPanel);

        setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 30, 0, 30));

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.setOpaque(false);
        
        // Logout Button
        JButton logoutButton = new JButton("Logout") {
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
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(SELECTED_COLOR);
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(255, 200, 100));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(SELECTED_COLOR);
            }
        });
        
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginSelectionFrame().setVisible(true);
        });
        
        controls.add(logoutButton);

        panel.add(title, BorderLayout.WEST);
        panel.add(controls, BorderLayout.EAST);
        return panel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, SIDEBAR_BG_TOP,
                    0, getHeight(), SIDEBAR_BG_BOTTOM
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Subtle right border with shadow effect
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRect(getWidth() - 2, 0, 2, getHeight());
                
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setMinimumSize(new Dimension(220, 700));
        sidebar.setMaximumSize(new Dimension(220, Integer.MAX_VALUE));
        sidebar.setOpaque(false);

        // --- Logo Area ---
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(30, 0, 30, 0));

        try {
            BufferedImage logoImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/logo.png")));
            Image scaledLogo = logoImg.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } catch (Exception e) {
            logoLabel.setText("<html><div style='text-align:center;'><span style='color:#FFB43C; font-size:24pt;'>innova</span><br><span style='color:gray; font-size:8pt;'>NEGOCIOS IMOBILIARIOS</span></div></html>");
        }
        sidebar.add(logoLabel);
        sidebar.add(createSeparator());
        sidebar.add(Box.createVerticalStrut(10));

        // Dashboard Button
        JButton dashboardButton = createNavButton("Dashboard", "ICON_DASHBOARD", true);
        currentlySelectedButton = dashboardButton; // Set as initially selected
        dashboardButton.addActionListener(e -> {
            collapseAllDropdowns();
            setSelectedButton(dashboardButton);
            cardLayout.show(mainContentPanel, "Dashboard");
        });
        sidebar.add(dashboardButton);
        sidebar.add(Box.createVerticalStrut(5));

        // Dropdowns
        sidebar.add(createDropdownMenu("Manage Staff", "ICON_STAFF",
                new String[]{"Add staff", "Delete staff", "View staff"},
                new String[]{"AddStaff", "DeleteStaff", "ViewStaff"}));
        sidebar.add(Box.createVerticalStrut(5));

        // UPDATED: "Update Status" instead of "Add new rooms"
        sidebar.add(createDropdownMenu("Manage Rooms", "ICON_ROOMS",
                new String[]{"Add New Room", "Remove Room", "Price Adjustment", "Room Status"},
                new String[]{"AddNewRoom", "RemoveRoom", "PriceAdjustment", "AddRooms"}));
        sidebar.add(Box.createVerticalStrut(5));

        // --- Support Tickets ---
        JButton btnSupportTickets = createNavButton("Support Tickets", "ICON_TICKET", false);
        btnSupportTickets.addActionListener(e -> {
            collapseAllDropdowns();
            setSelectedButton(btnSupportTickets);
            cardLayout.show(mainContentPanel, "SupportTickets");
        });
        sidebar.add(btnSupportTickets);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(createSeparator());
        sidebar.add(Box.createVerticalStrut(10));

        JButton viewReportsBtn = createNavButton("View Reports", "ICON_REPORTS", false);
        viewReportsBtn.addActionListener(e -> {
            collapseAllDropdowns();
            setSelectedButton(viewReportsBtn);
            cardLayout.show(mainContentPanel, "ViewReports");
        });
        sidebar.add(viewReportsBtn);
        sidebar.add(Box.createVerticalStrut(5));

        JButton settingsBtn = createNavButton("Settings", "ICON_SETTINGS", false);
        settingsBtn.addActionListener(e -> {
            collapseAllDropdowns();
            setSelectedButton(settingsBtn);
            cardLayout.show(mainContentPanel, "Settings");
        });
        sidebar.add(settingsBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSeparator());

        return sidebar;
    }

    private JPanel createDropdownMenu(String title, String iconType, String[] subItemTexts, String[] subItemCardNames) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton mainButton = createNavButton(title, iconType, false);

        JPanel subMenuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Rounded background with slight indent
                g2.setColor(SUB_MENU_BG);
                g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 8, 8);
                
                // Left border accent
                g2.setColor(new Color(SELECTED_COLOR.getRed(), SELECTED_COLOR.getGreen(), SELECTED_COLOR.getBlue(), 100));
                g2.fillRoundRect(15, 5, 3, getHeight() - 10, 3, 3);
                
                g2.dispose();
            }
        };
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setOpaque(true); // Make opaque to ensure visibility
        subMenuPanel.setBackground(new Color(25, 25, 28)); // Solid background
        subMenuPanel.setVisible(false);
        subMenuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subMenuPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        for (int i = 0; i < subItemTexts.length; i++) {
            String itemText = subItemTexts[i];
            String cardName = (subItemCardNames != null && i < subItemCardNames.length) ? subItemCardNames[i] : null;

            JButton subBtn = new JButton(itemText) {
                @Override
                protected void paintComponent(Graphics g) {
                    // Highlight background if this is the selected submenu item
                    if (this == currentlySelectedSubMenuButton) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(255, 180, 60, 50));
                        g2.fillRoundRect(25, 0, getWidth() - 35, getHeight(), 6, 6);
                        g2.dispose();
                    } else if (getModel().isRollover()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(255, 180, 60, 30));
                        g2.fillRoundRect(25, 0, getWidth() - 35, getHeight(), 6, 6);
                        g2.dispose();
                    }
                    super.paintComponent(g);
                }
            };
            subBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subBtn.setForeground(new Color(200, 200, 200));
            subBtn.setOpaque(false);
            subBtn.setContentAreaFilled(false);
            subBtn.setBorderPainted(false);
            subBtn.setFocusPainted(false);
            subBtn.setHorizontalAlignment(SwingConstants.LEFT);

            subBtn.setBorder(new EmptyBorder(6, 55, 6, 10));

            subBtn.setMaximumSize(new Dimension(200, 32));
            subBtn.setPreferredSize(new Dimension(200, 32));
            subBtn.setMinimumSize(new Dimension(200, 32));
            subBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            subBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    subBtn.setForeground(SUB_ITEM_HOVER_COLOR);
                    subBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    // Keep gold color if this is the selected item
                    if (subBtn == currentlySelectedSubMenuButton) {
                        subBtn.setForeground(SUB_ITEM_HOVER_COLOR);
                    } else {
                        subBtn.setForeground(new Color(200, 200, 200));
                    }
                    subBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            if (cardName != null) {
                subBtn.addActionListener(e -> {
                    setSelectedButton(null); // Deselect main navigation buttons
                    setSelectedSubMenuButton(subBtn); // Select this submenu item
                    cardLayout.show(mainContentPanel, cardName);
                });
            }
            subMenuPanel.add(subBtn);
        }

        subMenuPanel.add(Box.createVerticalStrut(5));

        // Register this dropdown for collapse tracking
        dropdownSubMenus.add(subMenuPanel);
        dropdownMainButtons.add(mainButton);

        mainButton.addActionListener(e -> {
            boolean isVisible = subMenuPanel.isVisible();
            
            // Deselect any currently selected main navigation button
            setSelectedButton(null);
            
            // First collapse all other dropdowns
            for (int i = 0; i < dropdownSubMenus.size(); i++) {
                JPanel otherSubMenu = dropdownSubMenus.get(i);
                JButton otherMainBtn = dropdownMainButtons.get(i);
                
                // Skip the current dropdown
                if (otherSubMenu != subMenuPanel && otherSubMenu.isVisible()) {
                    otherSubMenu.setVisible(false);
                    otherMainBtn.setBackground(DEFAULT_COLOR);
                    otherMainBtn.setForeground(new Color(220, 220, 220));
                }
            }
            
            // Then toggle the current dropdown
            subMenuPanel.setVisible(!isVisible);

            if (!isVisible) {
                mainButton.setBackground(SELECTED_COLOR);
                mainButton.setForeground(SELECTED_TEXT_COLOR);
            } else {
                mainButton.setBackground(DEFAULT_COLOR);
                mainButton.setForeground(new Color(220, 220, 220));
            }
            
            // Force complete repaint from root
            SwingUtilities.invokeLater(() -> {
                container.revalidate();
                Component root = SwingUtilities.getRoot(container);
                if (root != null) {
                    root.repaint();
                }
            });
        });

        container.add(mainButton);
        container.add(subMenuPanel);
        return container;
    }

    private JButton createNavButton(String text, String iconType, boolean isSelected) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int margin = 8;
                int width = getWidth() - (margin * 2);
                int height = getHeight();
                
                // Shadow effect for selected/hover state
                if (getBackground().getAlpha() > 0 || getBackground().equals(SELECTED_COLOR)) {
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(margin + 2, 2, width, height, 12, 12);
                }
                
                // Main button background
                g2.setColor(getBackground());
                g2.fillRoundRect(margin, 0, width, height, 12, 12);
                
                // Left accent bar for selected state
                if (getBackground().equals(SELECTED_COLOR)) {
                    g2.setColor(new Color(255, 200, 100));
                    g2.fillRoundRect(margin, height / 4, 4, height / 2, 3, 3);
                }

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 44));
        button.setPreferredSize(new Dimension(200, 44));

        button.setIcon(new SidebarIcon(iconType));
        button.setIconTextGap(12);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        if (isSelected) {
            button.setBackground(SELECTED_COLOR);
            button.setForeground(SELECTED_TEXT_COLOR);
        } else {
            button.setBackground(DEFAULT_COLOR);
            button.setForeground(new Color(220, 220, 220));
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(SELECTED_COLOR)) {
                    button.setBackground(HOVER_COLOR);
                    button.setForeground(Color.WHITE);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(SELECTED_COLOR)) {
                    button.setBackground(DEFAULT_COLOR);
                    button.setForeground(new Color(220, 220, 220));
                    button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        return button;
    }

    /**
     * Helper method to manage navigation button selection state
     */
    private void setSelectedButton(JButton newSelectedButton) {
        // Reset previously selected button
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setBackground(DEFAULT_COLOR);
            currentlySelectedButton.setForeground(new Color(220, 220, 220));
            currentlySelectedButton.repaint();
        }
        
        // Set new selected button
        if (newSelectedButton != null) {
            newSelectedButton.setBackground(SELECTED_COLOR);
            newSelectedButton.setForeground(SELECTED_TEXT_COLOR);
            newSelectedButton.repaint();
        }
        
        currentlySelectedButton = newSelectedButton;
        
        // Reset submenu selection when main button is selected
        if (newSelectedButton != null) {
            setSelectedSubMenuButton(null);
        }
    }

    /**
     * Helper method to manage submenu button selection state
     */
    private void setSelectedSubMenuButton(JButton newSelectedSubMenuButton) {
        // Reset previously selected submenu button
        if (currentlySelectedSubMenuButton != null) {
            currentlySelectedSubMenuButton.setForeground(new Color(200, 200, 200));
            currentlySelectedSubMenuButton.repaint();
        }
        
        // Set new selected submenu button
        if (newSelectedSubMenuButton != null) {
            newSelectedSubMenuButton.setForeground(SUB_ITEM_HOVER_COLOR);
            newSelectedSubMenuButton.repaint();
        }
        
        currentlySelectedSubMenuButton = newSelectedSubMenuButton;
    }

    /**
     * Helper method to collapse all dropdown menus
     */
    private void collapseAllDropdowns() {
        for (int i = 0; i < dropdownSubMenus.size(); i++) {
            JPanel subMenu = dropdownSubMenus.get(i);
            JButton mainBtn = dropdownMainButtons.get(i);
            
            if (subMenu.isVisible()) {
                subMenu.setVisible(false);
                mainBtn.setBackground(DEFAULT_COLOR);
                mainBtn.setForeground(new Color(220, 220, 220));
            }
        }
    }

    private JPanel createSeparator() {
        JPanel separator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw gradient line
                int margin = 25;
                GradientPaint gradient = new GradientPaint(
                    margin, 0, new Color(60, 60, 65, 0),
                    getWidth() / 2, 0, SEPARATOR_COLOR,
                    true
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(margin, 0, getWidth() - (margin * 2), 1, 1, 1);
                
                g2.dispose();
            }
        };
        separator.setOpaque(false);
        separator.setMaximumSize(new Dimension(220, 1));
        separator.setPreferredSize(new Dimension(220, 1));
        return separator;
    }

    private JButton createSmallButton(String text, Color bgColor) {
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        return button;
    }

    // --- CUSTOM ICON CLASS ---
    private class SidebarIcon implements Icon {
        private final String type;
        private final int size = 16;

        public SidebarIcon(String type) {
            this.type = type;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());

            g2.setStroke(new BasicStroke(1.5f));

            switch (type) {
                case "ICON_DASHBOARD":
                    int gap = 2;
                    int sqSize = (size - gap) / 2;
                    g2.fillRect(x, y, sqSize, sqSize);
                    g2.fillRect(x + sqSize + gap, y, sqSize, sqSize);
                    g2.fillRect(x, y + sqSize + gap, sqSize, sqSize);
                    g2.fillRect(x + sqSize + gap, y + sqSize + gap, sqSize, sqSize);
                    break;

                case "ICON_STAFF":
                    g2.fillOval(x + 4, y, 8, 8);
                    g2.fillArc(x, y + 8, 16, 12, 0, 180);
                    break;

                case "ICON_ROOMS":
                    g2.drawOval(x, y, 8, 8);
                    g2.drawLine(x + 8, y + 4, x + 14, y + 4);
                    g2.drawLine(x + 12, y + 4, x + 12, y + 7);
                    break;

                case "ICON_REPORTS":
                    g2.drawRect(x + 2, y, 10, 14);
                    g2.drawLine(x + 4, y + 4, x + 10, y + 4);
                    g2.drawLine(x + 4, y + 7, x + 10, y + 7);
                    g2.drawLine(x + 4, y + 10, x + 8, y + 10);
                    break;

                case "ICON_SETTINGS":
                    g2.drawOval(x + 2, y + 2, 12, 12);
                    g2.drawOval(x + 6, y + 6, 4, 4);
                    g2.drawLine(x + 8, y, x + 8, y + 2);
                    g2.drawLine(x + 8, y + 14, x + 8, y + 16);
                    g2.drawLine(x, y + 8, x + 2, y + 8);
                    g2.drawLine(x + 14, y + 8, x + 16, y + 8);
                    break;

                case "ICON_TICKET":
                    // Ticket/document icon with corner fold
                    g2.drawLine(x + 2, y, x + 10, y);
                    g2.drawLine(x + 10, y, x + 14, y + 4);
                    g2.drawLine(x + 14, y + 4, x + 14, y + 16);
                    g2.drawLine(x + 14, y + 16, x + 2, y + 16);
                    g2.drawLine(x + 2, y + 16, x + 2, y);
                    g2.drawLine(x + 10, y, x + 10, y + 4);
                    g2.drawLine(x + 10, y + 4, x + 14, y + 4);
                    // Lines inside ticket
                    g2.drawLine(x + 4, y + 7, x + 12, y + 7);
                    g2.drawLine(x + 4, y + 10, x + 12, y + 10);
                    g2.drawLine(x + 4, y + 13, x + 9, y + 13);
                    break;
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }

    // --- DASHBOARD HOME VIEW ---
    private JPanel createDashboardHome() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialize services for real data
        GuestBookingService bookingService = new GuestBookingService();
        RoomService roomService = new RoomService();
        StaffService staffService = new StaffService();

        // Calculate real statistics
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Room> allRooms = roomService.getAllRooms();
        
        // Active guests (CHECKED_IN status)
        long activeGuests = allBookings.stream()
            .filter(b -> "CHECKED_IN".equals(b.getBookingStatus()))
            .count();
        
        // Total rooms and availability
        long totalRooms = allRooms.size();
        long availableRooms = allRooms.stream()
            .filter(r -> "Available".equals(r.getStatus()))
            .count();
        
        // Bookings this month
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        long bookingsThisMonth = allBookings.stream()
            .filter(b -> {
                Date bookingDate = b.getBookingDate();
                if (bookingDate == null) return false;
                LocalDate bookingLocalDate = bookingDate.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
                return !bookingLocalDate.isBefore(monthStart);
            })
            .count();
        
        // Monthly revenue (all payments - both cash and card - from bookings made this month)
        double monthlyRevenue = allBookings.stream()
            .filter(b -> {
                if (b.getTotalAmount() <= 0) return false;
                Date bookingDate = b.getBookingDate();
                if (bookingDate == null) return false;
                LocalDate bookingLocalDate = bookingDate.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
                return !bookingLocalDate.isBefore(monthStart);
            })
            .mapToDouble(Booking::getTotalAmount)
            .sum();

        // 1. STATS CARDS (4 cards)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(1200, 160));

        statsPanel.add(createModernStatCard("Total Guests", String.valueOf(activeGuests), 
            "Currently Checked In", new Color(46, 204, 113), "👥"));
        statsPanel.add(createModernStatCard("Total Rooms", String.valueOf(totalRooms), 
            availableRooms + " Available", new Color(52, 152, 219), "🏨"));
        statsPanel.add(createModernStatCard("Bookings", String.valueOf(bookingsThisMonth), 
            "This Month", new Color(155, 89, 182), "📅"));
        statsPanel.add(createModernStatCard("Revenue", "LKR " + String.format("%,.0f", monthlyRevenue), 
            "This Month", SELECTED_COLOR, "💰"));

        content.add(statsPanel);
        content.add(Box.createVerticalStrut(20));

        // 2. MIDDLE SECTION - Staff Management + Revenue Chart
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        middlePanel.setOpaque(false);
        middlePanel.setMaximumSize(new Dimension(1200, 150000));

        // Staff Management Panel (Real Data)
        JPanel staffPanel = createModernStaffManagementPanel(staffService);
        middlePanel.add(staffPanel);

        // Revenue Chart Panel (30 days)
        JPanel revenuePanel = createRevenueChartPanel(bookingService);
        middlePanel.add(revenuePanel);

        content.add(middlePanel);
        content.add(Box.createVerticalGlue());

        return content;
    }

    private JPanel createModernStatCard(String title, String value, String subtitle, Color accentColor, String emoji) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Accent border
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 100));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
                
                // Subtle glow effect
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                g2.fillRoundRect(5, getHeight() - 50, getWidth() - 10, 45, 15, 15);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout(10, 10));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top section - Title and emoji
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(180, 180, 180));
        topSection.add(titleLabel, BorderLayout.WEST);
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        emojiLabel.setForeground(new Color(255, 255, 255, 180)); // Lighter for better visibility
        topSection.add(emojiLabel, BorderLayout.EAST);
        
        card.add(topSection, BorderLayout.NORTH);

        // Center section - Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accentColor);
        card.add(valueLabel, BorderLayout.CENTER);

        // Bottom section - Subtitle
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitleLabel.setForeground(new Color(140, 140, 140));
        card.add(subtitleLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createModernStaffManagementPanel(StaffService staffService) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel title = new JLabel("Staff Distribution");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(SELECTED_COLOR);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        
        JLabel subtitle = new JLabel("");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(new Color(140, 140, 140));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(40));

        // Get real staff data from MongoDB
        // Admin and Receptionist are in User collection, others in Staff collection
        UserService userService = new UserService();
        List<Staff> allStaff = staffService.getAllStaff();
        
        Map<String, Integer> staffCounts = new HashMap<>();
        
        // Count from Staff collection (Maintenance and Cleaning)
        for (Staff staff : allStaff) {
            String role = staff.getRole();
            staffCounts.put(role, staffCounts.getOrDefault(role, 0) + 1);
        }
        
        // Count from User collection (Admin and Receptionist)
        int adminCount = (int) userService.getAllUsers().stream()
            .filter(user -> "admin".equalsIgnoreCase(user.getRole()))
            .count();
        int receptionistCount = (int) userService.getAllUsers().stream()
            .filter(user -> "receptionist".equalsIgnoreCase(user.getRole()))
            .count();
        
        int maintenanceCount = staffCounts.getOrDefault("Maintenance Staff", 0);
        int cleaningCount = staffCounts.getOrDefault("Cleaning Staff", 0);
        
        int total = adminCount + receptionistCount + maintenanceCount + cleaningCount;
        if (total == 0) total = 1; // Avoid division by zero

        int adminPct = (adminCount * 100) / total;
        int receptionistPct = (receptionistCount * 100) / total;
        int maintenancePct = (maintenanceCount * 100) / total;
        int cleaningPct = (cleaningCount * 100) / total;

        panel.add(createStaffItem("👤 Administrative", adminCount + " Staff", adminPct));
        panel.add(Box.createVerticalStrut(25));

        panel.add(createStaffItem("📞 Receptionists", receptionistCount + " Staff", receptionistPct));
        panel.add(Box.createVerticalStrut(25));

        panel.add(createStaffItem("🔧 Maintenance", maintenanceCount + " Staff", maintenancePct));
        panel.add(Box.createVerticalStrut(25));

        panel.add(createStaffItem("📦 Cleaning", cleaningCount + " Staff", cleaningPct));
        panel.add(Box.createVerticalStrut(30));
        
        // Total count at bottom
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.setOpaque(false);
        JLabel totalLabel = new JLabel("Total Staff: " + total);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(SELECTED_COLOR);
        totalPanel.add(totalLabel);
        panel.add(totalPanel);

        return panel;
    }
    
    private JPanel createRevenueChartPanel(GuestBookingService bookingService) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Revenue Trends");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(SELECTED_COLOR);
        headerPanel.add(title, BorderLayout.WEST);
        
        JLabel periodLabel = new JLabel("Last 30 Days");
        periodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        periodLabel.setForeground(new Color(140, 140, 140));
        headerPanel.add(periodLabel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Chart area
        JPanel chartPanel = createRevenueChart(bookingService);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createRevenueChart(GuestBookingService bookingService) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int padding = 40;
                int chartWidth = width - (padding * 2);
                int chartHeight = height - (padding * 2);
                
                // Calculate daily revenue for last 30 days
                LocalDate today = LocalDate.now();
                double[] dailyRevenue = new double[30];
                double maxRevenue = 0;
                
                List<Booking> allBookings = bookingService.getAllBookings();
                for (int i = 0; i < 30; i++) {
                    LocalDate date = today.minusDays(29 - i);
                    double revenue = allBookings.stream()
                        .filter(b -> {
                            if (!"CHECKED_OUT".equals(b.getBookingStatus())) return false;
                            Date checkoutDate = b.getActualCheckOutDate();
                            if (checkoutDate == null) return false;
                            LocalDate checkoutLocalDate = checkoutDate.toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate();
                            return checkoutLocalDate.equals(date);
                        })
                        .mapToDouble(Booking::getTotalAmount)
                        .sum();
                    dailyRevenue[i] = revenue;
                    if (revenue > maxRevenue) maxRevenue = revenue;
                }
                
                if (maxRevenue == 0) maxRevenue = 1000; // Default scale
                
                // Draw grid lines
                g2.setColor(new Color(60, 60, 65, 80));
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
                for (int i = 0; i <= 5; i++) {
                    int y = padding + (chartHeight * i / 5);
                    g2.drawLine(padding, y, padding + chartWidth, y);
                }
                
                // Draw Y-axis labels
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.setColor(new Color(140, 140, 140));
                for (int i = 0; i <= 5; i++) {
                    int y = padding + (chartHeight * i / 5);
                    double value = maxRevenue * (5 - i) / 5;
                    String label = "$" + (value >= 1000 ? String.format("%.0fK", value / 1000) : String.format("%.0f", value));
                    g2.drawString(label, 5, y + 4);
                }
                
                // Draw revenue line chart
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // Create gradient for line
                GradientPaint lineGradient = new GradientPaint(
                    0, padding, SELECTED_COLOR,
                    0, padding + chartHeight, new Color(255, 180, 60, 100)
                );
                g2.setPaint(lineGradient);
                
                // Draw line
                int[] xPoints = new int[30];
                int[] yPoints = new int[30];
                
                for (int i = 0; i < 30; i++) {
                    xPoints[i] = padding + (chartWidth * i / 29);
                    double normalizedValue = maxRevenue > 0 ? dailyRevenue[i] / maxRevenue : 0;
                    yPoints[i] = padding + chartHeight - (int)(chartHeight * normalizedValue);
                }
                
                // Draw gradient fill area
                int[] fillXPoints = new int[32];
                int[] fillYPoints = new int[32];
                System.arraycopy(xPoints, 0, fillXPoints, 0, 30);
                System.arraycopy(yPoints, 0, fillYPoints, 0, 30);
                fillXPoints[30] = xPoints[29];
                fillYPoints[30] = padding + chartHeight;
                fillXPoints[31] = xPoints[0];
                fillYPoints[31] = padding + chartHeight;
                
                GradientPaint fillGradient = new GradientPaint(
                    0, padding, new Color(255, 180, 60, 50),
                    0, padding + chartHeight, new Color(255, 180, 60, 10)
                );
                g2.setPaint(fillGradient);
                g2.fillPolygon(fillXPoints, fillYPoints, 32);
                
                // Draw line
                g2.setPaint(lineGradient);
                g2.drawPolyline(xPoints, yPoints, 30);
                
                // Draw data points
                g2.setColor(SELECTED_COLOR);
                for (int i = 0; i < 30; i++) {
                    if (dailyRevenue[i] > 0) {
                        g2.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                        g2.setColor(new Color(255, 255, 255, 200));
                        g2.fillOval(xPoints[i] - 2, yPoints[i] - 2, 4, 4);
                        g2.setColor(SELECTED_COLOR);
                    }
                }
                
                // Draw X-axis labels (show every 5 days)
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                g2.setColor(new Color(140, 140, 140));
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
                for (int i = 0; i < 30; i += 5) {
                    LocalDate date = today.minusDays(29 - i);
                    Date javaDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    String dateStr = sdf.format(javaDate);
                    g2.drawString(dateStr, xPoints[i] - 15, padding + chartHeight + 15);
                }
                
                g2.dispose();
            }
        };
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel title = new JLabel("Quick Actions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(SELECTED_COLOR);
        panel.add(title, BorderLayout.NORTH);

        // Actions grid
        JPanel actionsGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        actionsGrid.setOpaque(false);
        actionsGrid.setBorder(new EmptyBorder(20, 0, 0, 0));

        actionsGrid.add(createQuickActionButton("➕ Add Staff", "AddStaff"));
        actionsGrid.add(createQuickActionButton("🏨 Add Room", "AddNewRoom"));
        actionsGrid.add(createQuickActionButton("📊 View Reports", "ViewReports"));
        actionsGrid.add(createQuickActionButton("💰 Adjust Prices", "PriceAdjustment"));

        panel.add(actionsGrid, BorderLayout.CENTER);

        return panel;
    }
    
    private JButton createQuickActionButton(String text, String cardName) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(255, 180, 60, 200));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 180, 60, 150));
                } else {
                    g2.setColor(new Color(60, 60, 65, 150));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(15, 10, 15, 10));
        
        button.addActionListener(e -> {
            collapseAllDropdowns();
            setSelectedButton(null);
            cardLayout.show(mainContentPanel, cardName);
        });
        
        return button;
    }
    
    private JPanel createRecentActivityPanel(GuestBookingService bookingService) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 28, 240),
                    0, getHeight(), new Color(20, 20, 24, 240)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel title = new JLabel("Recent Activity");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(SELECTED_COLOR);
        panel.add(title, BorderLayout.NORTH);

        // Activity list
        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setOpaque(false);
        activityList.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Get last 5 bookings
        List<Booking> recentBookings = bookingService.getAllBookings();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
        
        int count = 0;
        for (int i = recentBookings.size() - 1; i >= 0 && count < 5; i--) {
            Booking booking = recentBookings.get(i);
            String status = booking.getBookingStatus();
            String icon = status.equals("CHECKED_OUT") ? "✓" : status.equals("CHECKED_IN") ? "🔑" : "📅";
            String dateStr = booking.getBookingDate() != null ? sdf.format(booking.getBookingDate()) : "N/A";
            
            activityList.add(createActivityItem(
                icon,
                booking.getGuestName(),
                "Room " + booking.getRoomNumber() + " - " + status,
                dateStr
            ));
            activityList.add(Box.createVerticalStrut(8));
            count++;
        }
        
        if (count == 0) {
            JLabel noActivity = new JLabel("No recent activity");
            noActivity.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            noActivity.setForeground(new Color(120, 120, 120));
            activityList.add(noActivity);
        }

        panel.add(activityList, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createActivityItem(String icon, String name, String action, String time) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(500, 40));
        item.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        item.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(Color.WHITE);
        textPanel.add(nameLabel);
        
        JLabel actionLabel = new JLabel(action);
        actionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        actionLabel.setForeground(new Color(160, 160, 160));
        textPanel.add(actionLabel);
        
        item.add(textPanel, BorderLayout.CENTER);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        timeLabel.setForeground(new Color(120, 120, 120));
        item.add(timeLabel, BorderLayout.EAST);

        return item;
    }

    private JPanel createStaffItem(String role, String subtitle, int percent) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setOpaque(false);
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));


        JLabel textLabel = new JLabel("<html><b style='color:white; font-size:13px;'>" + role + "</b><br><span style='color:gray; font-size:11px;'>" + subtitle + "</span></html>");
        itemPanel.add(textLabel, BorderLayout.WEST);

        CircleProgressPanel circleChart = new CircleProgressPanel(percent);

        JPanel progressContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        progressContainer.setOpaque(false);
        progressContainer.add(circleChart);

        itemPanel.add(progressContainer, BorderLayout.EAST);

        return itemPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboardFrame::new);
    }
}