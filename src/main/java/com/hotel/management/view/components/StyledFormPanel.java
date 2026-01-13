package com.hotel.management.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;


public class StyledFormPanel extends JPanel {
    private static final int FORM_WIDTH = 550;
    private static final int CORNER_RADIUS = 15;
    private final Color backgroundColor;
    private final Color headerColor;

    private final JPanel mainFormContent;
    private final JPanel buttonPanel;

    public StyledFormPanel(String title, String name, Color bgColor, Color hColor) {
        this.backgroundColor = bgColor;
        this.headerColor = hColor;
        this.setName(name);

        setLayout(new GridBagLayout());
        setOpaque(false);


        mainFormContent = new JPanel(new BorderLayout());

        mainFormContent.setPreferredSize(new Dimension(FORM_WIDTH, 650));
        mainFormContent.setBackground(backgroundColor);
        mainFormContent.setOpaque(false);
        mainFormContent.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Header
        JLabel headerLabel = new JLabel(title, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(headerColor.brighter());
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Header Panel (to give some background to the title)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        mainFormContent.add(headerPanel, BorderLayout.NORTH);

        // Button Panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        mainFormContent.add(buttonPanel, BorderLayout.SOUTH);

        // Add the styled panel to the center of the StyledFormPanel
        add(mainFormContent, new GridBagConstraints());
    }


    public void setMainInputPanel(JPanel inputPanel) {
        // Use a padding of 10 for the content area
        inputPanel.setBorder(new EmptyBorder(10, 25, 10, 25));
        mainFormContent.add(inputPanel, BorderLayout.CENTER);
    }


    public void addButton(JButton button) {
        buttonPanel.add(button);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point loc = mainFormContent.getLocation();
        g2.translate(loc.x, loc.y);

        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, mainFormContent.getWidth(), mainFormContent.getHeight(), CORNER_RADIUS, CORNER_RADIUS));

        g2.dispose();

        super.paintChildren(g);
    }
}