package cmsc125.project4.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardView extends JFrame {
    private JButton btnSettings;
    private JButton btnStart;
    private JButton btnExit;

    public DashboardView() {
        setTitle("Seekulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("Seekulator", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        headerLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 18));
        tabbedPane.addTab("Help", createHelpPanel());
        tabbedPane.addTab("About", createAboutPanel());
        add(tabbedPane, BorderLayout.CENTER);

        // Footer Navigation
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        btnSettings = createImageButton("/icons/settings.png" ,"Settings");

        btnStart = new JButton("Start");
        btnStart.setFont(new Font("Arial", Font.BOLD, 28));

        btnExit = createImageButton("/icons/exit.png" ,"Exit");

        bottomPanel.add(btnSettings, BorderLayout.WEST);
        bottomPanel.add(btnStart, BorderLayout.CENTER);
        bottomPanel.add(btnExit, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHelpPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextArea descArea = new JTextArea(
            "[Description]\nLorem ipsum dolor sit amet, consectetur adipiscing elit..."
        );
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setMargin(new Insets(10, 10, 10, 10));
        panel.add(descArea, BorderLayout.NORTH);

        // Placeholder for the slider/screenshot
        JPanel imagePlaceholder = new JPanel(new BorderLayout());
        imagePlaceholder.setBackground(Color.LIGHT_GRAY);
        JLabel imageText = new JLabel("[Screenshot Image of Instruction]", SwingConstants.CENTER);
        imagePlaceholder.add(imageText, BorderLayout.CENTER);

        JButton btnLeft = new JButton("<");
        JButton btnRight = new JButton(">");
        btnLeft.setFont(new Font("Arial", Font.BOLD, 30));
        btnRight.setFont(new Font("Arial", Font.BOLD, 30));

        imagePlaceholder.add(btnLeft, BorderLayout.WEST);
        imagePlaceholder.add(btnRight, BorderLayout.EAST);

        panel.add(imagePlaceholder, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextArea aboutText = new JTextArea(
            "Seekulator - A Disk Algorithm Simulator\n\n" +
                "Authors:\nal1x3\nSchneidelstrom\nddrhckrzz\n\n" +
                "GitHub Repository Link: https://github.com/CMSC125OperatingSystems/Seekulator\n\n" +
                "2026 Copyright © All Rights Reserved. This project is duly for academic purposes only."
        );
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setEditable(false);
        aboutText.setMargin(new Insets(20, 20, 20, 20));
        panel.add(new JScrollPane(aboutText), BorderLayout.CENTER);

        return panel;
    }

    private JButton createImageButton(String imagePath, String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        try {
            java.net.URL imgURL = getClass().getResource(imagePath);
            if (imgURL != null) {
                btn.setIcon(new ImageIcon(imgURL));
                btn.setVerticalTextPosition(SwingConstants.BOTTOM);
                btn.setHorizontalTextPosition(SwingConstants.CENTER);
            } else {
                System.err.println("Resource not found: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Getters for controller to attach listeners
    public JButton getBtnSettings() { return btnSettings; }
    public JButton getBtnStart() { return btnStart; }
    public JButton getBtnExit() { return btnExit; }
}