import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ReceptionistCheckouts extends JFrame {

    // === GOLD BUTTON CLASS ===
    class GoldButton extends JButton {

        public GoldButton(String text) {
            super(text);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);  // We paint the button manually
            setForeground(Color.BLACK);   // Text color

            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setBackground(new Color(255, 180, 60));  // GOLD
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fill rounded rectangle
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // === BACKGROUND PANEL ===
    class BackgroundPanel extends JPanel {
        private Image img;

        public BackgroundPanel(String imgPath) {
            java.net.URL imgURL = getClass().getResource(imgPath);
            if (imgURL != null) {
                img = new ImageIcon(imgURL).getImage();
            } else {
                System.out.println("⚠ Image not found: " + imgPath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // === MAIN UI ===
    public ReceptionistCheckouts() {

        setTitle("Check OUT");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === SIDEBAR ===
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(20, 20, 20));

        // === LOGO ===
        JLabel logo = new JLabel();
        java.net.URL logoURL = getClass().getResource("/Rescources/logo.png");
        if (logoURL != null) {
            logo.setIcon(new ImageIcon(logoURL));
        }
        logo.setBounds(50, 20, 120, 100);
        sidebar.add(logo);

        // === GOLD BUTTONS ===
        sidebar.add(createSidebarButton("Dashboard", 140));
        sidebar.add(createSidebarButton("Settings and Profile", 200));
        sidebar.add(createSidebarButton("Exams", 260));
        sidebar.add(createSidebarButton("Features  NEW", 600));

        add(sidebar, BorderLayout.WEST);

        // === MAIN BACKGROUND ===
        BackgroundPanel mainPanel = new BackgroundPanel("/Rescources/1303755c519208ef762b4077b243707e.jpg");
        mainPanel.setLayout(null);

        JLabel title = new JLabel("Today’s Check-Outs");
        title.setFont(new Font("Serif", Font.BOLD, 38));
        title.setForeground(new Color(255, 215, 0));
        title.setBounds(360, 25, 500, 40);
        mainPanel.add(title);

        String[] columns = {
                "Guest Name", "Room Number", "Check-In Date",
                "Check-Out Date & Time", "Total Nights", "Total Bill", "Status"
        };

        Object[][] data = {
                {"John Silva", "118 – Standard", "16/11/2025", "18/11/2025 11:00 AM", "2", "Rs. 22,500", "Completed"},
                {"Maya Perera", "302 – Deluxe", "15/11/2025", "17/11/2025 10:30 AM", "2", "Rs. 35,800", "Completed"},
                {"Hiruni Jayasinghe", "501 – Suite", "14/11/2025", "17/11/2025 9:00 AM", "3", "Rs. 72,000", "Completed"},
                {"Kevin Rodrigo", "207 – Superior", "14/11/2025", "18/11/2025 2:00 PM", "2", "Rs. 28,400", "Late Check-Out"},
                {"Amaya Fernando", "412 – Premium", "14/11/2025", "15/11/2025 1:45 PM", "2", "Rs. 40,200", "Completed"},
                {"Shun Tanaka", "620 – Executive", "12/11/2025", "15/11/2025 8:20 AM", "3", "Rs. 95,600", "Completed"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(100, 350, 840, 3000);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        mainPanel.add(sp);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // === SIDEBAR BUTTON CREATOR WITH NAVIGATION ===
    private JButton createSidebarButton(String text, int y) {
        GoldButton b = new GoldButton(text);
        b.setBounds(20, y, 180, 45);

        // Hover effect
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setForeground(Color.BLACK);
            }
        });

        // ===== ACTIONS (PAGE NAVIGATION) =====
        b.addActionListener(e -> {

            switch (text) {

                case "Dashboard":
                    new ReceptionistDashboardFrame();
                    dispose();
                    break;

                case "Features  NEW": // Check-Outs page
                    new ReceptionistCheckouts();
                    dispose();
                    break;
            }

        });

        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReceptionistCheckouts::new);
    }
}
