import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.text.JTextComponent;

// NOTE: Ensure AdminDashboardFrame.java is in the same folder/package.

public class AdminLoginFrame extends JFrame implements ActionListener {

    // --- Temporary Credentials ---
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "temp123";

    // --- Color Constants (Matching Dashboard Theme) ---
    private final Color GOLD_COLOR = new Color(255, 180, 60);
    private final Color LABEL_COLOR = GOLD_COLOR;
    private final Color FIELD_BORDER_COLOR = GOLD_COLOR;
    private final Color PLACEHOLDER_COLOR = new Color(150, 150, 150);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FORM_BACKGROUND = new Color(0, 0, 0, 150); // Dark transparent

    // --- GUI Components ---
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private BufferedImage backgroundImage;

    public AdminLoginFrame() {
        // --- 1. Frame Setup ---
        setTitle("Hotel Management System - Admin Login");
        setSize(800, 600); // Standard size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setResizable(false);

        // --- Load Background Image ---
        try {
            // Check your path: "/Rescources/a170b7e6b576d72403c665e6337322e1.jpg"
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/Rescources/a170b7e6b576d72403c665e6337322e1.jpg")));
        } catch (IOException e) {
            System.err.println("Background image not found. Using solid background.");
            backgroundImage = null;
        } catch (IllegalArgumentException e) {
            System.err.println("Resource path is null. Check image file existence/path.");
            backgroundImage = null;
        }

        // --- Custom JPanel for background image ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Draw the image scaled to fit the panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(20, 20, 20)); // Dark Fallback
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // Center the login panel

        // --- Login Panel Container ---
        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FORM_BACKGROUND);
                // Rounded rectangle background
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginPanel.setOpaque(false);
        loginPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // --- 1. Icon ---
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            // Attempt to load icon
            ImageIcon icon = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/Rescources/b41ff478e42e31fd71584d9dae338ffa-removebg-preview.png")))
                    .getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            iconLabel.setIcon(icon);
        } catch (Exception e) {
            // Fallback icon or text
            iconLabel.setText("<html><span style='font-size:40px; color:#FFB43C;'>&#128100;</span></html>"); // User emoji
        }
        gbc.gridwidth = 2;
        loginPanel.add(iconLabel, gbc);

        // --- 2. Title ---
        gbc.gridy++;
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(titleLabel, gbc);

        // --- 3. Subtitle ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel subtitleLabel = new JLabel("Welcome back! Please login to continue.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(subtitleLabel, gbc);

        // Reset insets
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridwidth = 1;

        // --- 4. Username Field ---
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(LABEL_COLOR);
        loginPanel.add(userLabel, gbc);

        gbc.gridy++;
        userTextField = createStyledTextField("Enter your username");
        loginPanel.add(userTextField, gbc);

        // --- 5. Password Field ---
        gbc.gridy++;
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(LABEL_COLOR);
        loginPanel.add(passLabel, gbc);

        gbc.gridy++;
        passwordField = createStyledPasswordField("Enter your password");
        loginPanel.add(passwordField, gbc);

        // --- 6. Login Button ---
        gbc.gridy++;
        gbc.insets = new Insets(25, 0, 10, 0);
        loginButton = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(GOLD_COLOR.darker());
                } else {
                    g2.setColor(GOLD_COLOR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Draw Text manually to ensure it's centered and right color
                g2.setColor(Color.BLACK); // Black text on Gold
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };
        loginButton.setPreferredSize(new Dimension(200, 45));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);

        loginPanel.add(loginButton, gbc);

        // --- 7. Message Label ---
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(messageLabel, gbc);

        backgroundPanel.add(loginPanel);
        add(backgroundPanel);

        setVisible(true);
    }

    // --- Helper: Create Styled TextField ---
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(PLACEHOLDER_COLOR);
        field.setCaretColor(GOLD_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.addFocusListener(new PlaceholderFocusListener(placeholder, field));
        return field;
    }

    // --- Helper: Create Styled PasswordField ---
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(PLACEHOLDER_COLOR);
        field.setCaretColor(GOLD_COLOR);
        field.setEchoChar((char) 0); // Show text initially
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.addFocusListener(new PlaceholderFocusListener(placeholder, field));
        return field;
    }

    // --- Logic ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String user = userTextField.getText();
            String password = new String(passwordField.getPassword());

            final String USER_PLACEHOLDER = "Enter your username";
            final String PASS_PLACEHOLDER = "Enter your password";

            if (user.equals(USER_PLACEHOLDER) || password.equals(PASS_PLACEHOLDER) || user.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Username and Password are required.");
                messageLabel.setForeground(Color.RED);
                return;
            }

            // Authentication Check
            if (ADMIN_USER.equals(user) && ADMIN_PASS.equals(password)) {
                messageLabel.setText("Login Successful!");
                messageLabel.setForeground(new Color(100, 255, 100)); // Green

                // Close Login and Open Dashboard
                dispose();
                new AdminDashboardFrame();
            } else {
                messageLabel.setText("Invalid Username or Password.");
                messageLabel.setForeground(Color.RED);
            }
        }
    }

    // --- Placeholder Listener ---
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
                if (component instanceof JPasswordField) {
                    ((JPasswordField) component).setEchoChar('•');
                }
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (component.getText().isEmpty()) {
                component.setText(placeholder);
                component.setForeground(PLACEHOLDER_COLOR);
                if (component instanceof JPasswordField) {
                    ((JPasswordField) component).setEchoChar((char) 0);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLoginFrame::new);
    }
}