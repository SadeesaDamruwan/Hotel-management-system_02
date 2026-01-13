package com.hotel.management;
import com.hotel.management.view.LoginSelectionFrame;

import javax.swing.*;
public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginSelectionFrame loginFrame = new LoginSelectionFrame();
            loginFrame.setVisible(true);
        });
    }
}
