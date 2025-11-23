import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

// NOTE: Ensure all panel files (AddStaffPanel, RemoveStaffPanel, ViewStaffPanel,
// PriceAdjustmentPanel, AddRoomPanel, CircleProgressPanel) are in the same folder.

public class AdminDashboardFrame extends JFrame {

    private BufferedImage backgroundImage;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // --- Color Constants ---
    private final Color SELECTED_COLOR = new Color(255, 180, 60);
    private final Color DEFAULT_COLOR = new Color(40, 40, 40);
    private final Color HOVER_COLOR = new Color(60, 60, 60);
    private final Color SUB_ITEM_HOVER_COLOR = new Color(255, 180, 60);
    private final Color SUB_MENU_BG = new Color(35, 35, 35);

    public AdminDashboardFrame() {
        // --- Frame Setup ---
        setTitle("Hotel Management System - Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // --- Load Background Image ---
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Rescources/a170b7e6b576d72403c665e6337322e1.jpg")));
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

        // --- ADDING THE VIEWS ---
        mainContentPanel.add(createDashboardHome(), "Dashboard");
        mainContentPanel.add(new AddStaffPanel(), "AddStaff");
        mainContentPanel.add(new RemoveStaffPanel(), "DeleteStaff");
        mainContentPanel.add(new ViewStaffPanel(), "ViewStaff");
        mainContentPanel.add(new PriceAdjustmentPanel(), "PriceAdjustment");
        mainContentPanel.add(new AddRoomPanel(), "AddRooms"); // Renamed to "Update Status" in UI, keeping ID same

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
        controls.add(new JLabel("77%"));

        panel.add(title, BorderLayout.WEST);
        panel.add(controls, BorderLayout.EAST);
        return panel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 700));
        sidebar.setBackground(new Color(30, 30, 30, 200));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.DARK_GRAY));

        // --- Logo Area ---
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(30, 0, 30, 0));

        try {
            BufferedImage logoImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Rescources/logo.png")));
            Image scaledLogo = logoImg.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } catch (Exception e) {
            logoLabel.setText("<html><div style='text-align:center;'><span style='color:#FFB43C; font-size:24pt;'>innova</span><br><span style='color:gray; font-size:8pt;'>NEGOCIOS IMOBILIARIOS</span></div></html>");
        }
        sidebar.add(logoLabel);

        // Dashboard Button
        JButton dashboardButton = createNavButton("Dashboard", "ICON_DASHBOARD", true);
        dashboardButton.addActionListener(e -> cardLayout.show(mainContentPanel, "Dashboard"));
        sidebar.add(dashboardButton);

        // Dropdowns
        sidebar.add(createDropdownMenu("Manage Staff", "ICON_STAFF",
                new String[]{"Add staff", "Delete staff", "View staff"},
                new String[]{"AddStaff", "DeleteStaff", "ViewStaff"}));

        // UPDATED: "Update Status" instead of "Add new rooms"
        sidebar.add(createDropdownMenu("Manage Rooms", "ICON_ROOMS",
                new String[]{"Price Adjustment", "Update Status", "Update Rooms"},
                new String[]{"PriceAdjustment", "AddRooms", "UpdateRooms"}));

        sidebar.add(createNavButton("View Reports", "ICON_REPORTS", false));
        sidebar.add(createNavButton("Settings", "ICON_SETTINGS", false));

        // Footer
        sidebar.add(Box.createVerticalGlue());
        JPanel featuresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        featuresPanel.setOpaque(false);
        featuresPanel.add(createSmallButton("Features", new Color(255, 180, 60)));
        featuresPanel.add(createSmallButton("NEW", new Color(255, 100, 100)));
        featuresPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        sidebar.add(featuresPanel);

        return sidebar;
    }

    private JPanel createDropdownMenu(String title, String iconType, String[] subItemTexts, String[] subItemCardNames) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(Component.CENTER_ALIGNMENT);

        container.setMaximumSize(new Dimension(180, 300));

        JButton mainButton = createNavButton(title, iconType, false);

        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setBackground(SUB_MENU_BG);
        subMenuPanel.setOpaque(true);
        subMenuPanel.setVisible(false);

        subMenuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (int i = 0; i < subItemTexts.length; i++) {
            String itemText = subItemTexts[i];
            String cardName = (subItemCardNames != null && i < subItemCardNames.length) ? subItemCardNames[i] : null;

            JButton subBtn = new JButton(itemText);
            subBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subBtn.setForeground(Color.LIGHT_GRAY);
            subBtn.setBackground(SUB_MENU_BG);
            subBtn.setOpaque(false);
            subBtn.setContentAreaFilled(false);
            subBtn.setBorderPainted(false);
            subBtn.setFocusPainted(false);
            subBtn.setHorizontalAlignment(SwingConstants.LEFT);

            subBtn.setBorder(new EmptyBorder(5, 50, 5, 10));

            subBtn.setMaximumSize(new Dimension(180, 30));
            subBtn.setPreferredSize(new Dimension(180, 30));
            subBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            subBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    subBtn.setForeground(SUB_ITEM_HOVER_COLOR);
                    subBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    subBtn.setForeground(Color.LIGHT_GRAY);
                    subBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            if (cardName != null) {
                subBtn.addActionListener(e -> {
                    cardLayout.show(mainContentPanel, cardName);
                });
            }
            subMenuPanel.add(subBtn);
        }

        subMenuPanel.add(Box.createVerticalStrut(5));

        mainButton.addActionListener(e -> {
            boolean isVisible = subMenuPanel.isVisible();
            subMenuPanel.setVisible(!isVisible);

            if (!isVisible) {
                mainButton.setBackground(SELECTED_COLOR);
                mainButton.setForeground(Color.BLACK);
            } else {
                mainButton.setBackground(DEFAULT_COLOR);
                mainButton.setForeground(Color.WHITE);
            }

            container.revalidate();
            container.getParent().revalidate();
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

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 45));
        button.setPreferredSize(new Dimension(180, 45));

        button.setIcon(new SidebarIcon(iconType));
        button.setIconTextGap(15);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        if (isSelected) {
            button.setBackground(SELECTED_COLOR);
            button.setForeground(Color.BLACK);
        } else {
            button.setBackground(DEFAULT_COLOR);
            button.setForeground(Color.WHITE);
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(SELECTED_COLOR)) {
                    button.setBackground(HOVER_COLOR);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(SELECTED_COLOR)) {
                    button.setBackground(DEFAULT_COLOR);
                    button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        return button;
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

        // 1. STATS CARDS
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(1200, 180));

        statsPanel.add(createStatCard("Total Users", 300));
        statsPanel.add(createStatCard("Total Rooms", 300));
        statsPanel.add(createStatCard("Total Bookings", 300));

        content.add(statsPanel);
        content.add(Box.createVerticalStrut(30));

        // 2. STAFF MANAGEMENT SECTION
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JPanel staffChart = createStaffManagementPanel();
        staffChart.setPreferredSize(new Dimension(450, 400));
        staffChart.setMaximumSize(new Dimension(450, 400));

        bottomPanel.add(staffChart, BorderLayout.WEST);

        content.add(bottomPanel);
        content.add(Box.createVerticalGlue());

        return content;
    }

    private JPanel createStatCard(String title, int value) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 20, 20, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        card.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        valueLabel.setForeground(SELECTED_COLOR);
        card.add(valueLabel, gbc);

        return card;
    }

    private JPanel createStaffManagementPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 20, 20, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Staff Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(SELECTED_COLOR);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        panel.add(createStaffItem("Administrative Staff", "78 Registered", 32));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStaffItem("Restaurant Staff", "60 Registered", 43));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStaffItem("Maintenance Staff", "80 Registered", 67));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStaffItem("House Keeping", "104 Registered", 56));

        return panel;
    }

    private JPanel createStaffItem(String role, String subtitle, int percent) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setOpaque(false);
        itemPanel.setMaximumSize(new Dimension(400, 60));

        JLabel textLabel = new JLabel("<html><b style='color:white; font-size:11px;'>" + role + "</b><br><span style='color:gray; font-size:9px;'>" + subtitle + "</span></html>");
        itemPanel.add(textLabel, BorderLayout.CENTER);

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