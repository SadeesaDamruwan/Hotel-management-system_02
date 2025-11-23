import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * Custom Component to draw the circular progress charts seen in the Staff Management section.
 */
public class CircleProgressPanel extends JPanel {

    private int percentage;
    private Color progressColor = new Color(255, 180, 60); // Gold
    private Color trackColor = new Color(60, 60, 60);      // Dark Gray
    private final int THICKNESS = 5;
    private final int SIZE = 45; // Size of the circle

    public CircleProgressPanel(int percentage) {
        this.percentage = percentage;
        setOpaque(false);
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setMaximumSize(new Dimension(SIZE, SIZE));
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int drawSize = SIZE - THICKNESS;
        int offset = THICKNESS / 2;
        int arcStart = 90;

        // 1. Draw Background Track
        g2.setColor(trackColor);
        g2.setStroke(new BasicStroke(THICKNESS));
        g2.draw(new Arc2D.Double(offset, offset, drawSize, drawSize, 0, 360, Arc2D.OPEN));

        // 2. Draw Progress Arc (Gold)
        g2.setColor(progressColor);
        int angle = (int) (360 * percentage / 100.0);
        g2.setStroke(new BasicStroke(THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Arc2D.Double(offset, offset, drawSize, drawSize, arcStart, -angle, Arc2D.OPEN));

        // 3. Draw Percentage Text
        String text = percentage + "%";
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();
        int x = (SIZE - fm.stringWidth(text)) / 2;
        int y = (SIZE - fm.getHeight()) / 2 + fm.getAscent();

        g2.drawString(text, x, y);
        g2.dispose();
    }
}