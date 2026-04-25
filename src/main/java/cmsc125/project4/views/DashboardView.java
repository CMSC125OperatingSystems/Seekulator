package cmsc125.project4.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardView extends JFrame {
    private JButton btnSettings;
    private JButton btnStart;
    private JButton btnExit;
    private final Dimension defaultSize = new Dimension(1024, 768);

    public DashboardView() {
        setTitle("Seekulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(defaultSize);
        setSize(defaultSize);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headerLabel = new JLabel("Seekulator", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 48));
        headerLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 20));
        tabbedPane.addTab("How to Play", createHelpPanel());
        tabbedPane.addTab("About", createAboutPanel());

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBorder(new EmptyBorder(0, 40, 0, 40));
        centerWrapper.add(tabbedPane, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        btnSettings = createImageButton("/icons/settings.png", "Settings");
        btnExit = createImageButton("/icons/exit.png", "Exit");

        btnStart = new JButton("Start");
        btnStart.setFont(new Font("Arial", Font.BOLD, 36));
        btnStart.setFocusPainted(false);
        btnStart.setBorderPainted(false);
        btnStart.setContentAreaFilled(false);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(btnSettings, BorderLayout.WEST);
        bottomPanel.add(btnStart, BorderLayout.CENTER);
        bottomPanel.add(btnExit, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createHelpPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        // Dynamically get the border color from UIManager
        panel.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Custom.borderColor")));

        JTextArea descArea = new JTextArea(
            "[Description]\nLorem ipsum dolor sit amet, consectetur adipiscing elit..."
        );
        descArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setMargin(new Insets(15, 20, 15, 20));
        panel.add(descArea, BorderLayout.NORTH);

        JPanel imagePlaceholder = new JPanel(new BorderLayout());
        JLabel imageText = new JLabel("[Screenshot Image of Instruction]", SwingConstants.CENTER);
        imagePlaceholder.add(imageText, BorderLayout.CENTER);

        JButton btnLeft = new JButton("<");
        JButton btnRight = new JButton(">");
        btnLeft.setFont(new Font("Arial", Font.BOLD, 48));
        btnRight.setFont(new Font("Arial", Font.BOLD, 48));
        btnLeft.setContentAreaFilled(false);
        btnLeft.setBorderPainted(false);
        btnRight.setContentAreaFilled(false);
        btnRight.setBorderPainted(false);

        imagePlaceholder.add(btnLeft, BorderLayout.WEST);
        imagePlaceholder.add(btnRight, BorderLayout.EAST);

        panel.add(imagePlaceholder, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Custom.borderColor")));

        JTextArea aboutText = new JTextArea(
            "Lorem ipsum dolor sit amet...\n\n" +
                "Authors:\n[Author 1]\n[Author 2]\n[Author 3]\n\n" +
                "GitHub Repository Link\n\n" +
                "2026 Copyright. This project is duly for academic purposes only."
        );
        aboutText.setFont(new Font("Arial", Font.PLAIN, 16));
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
            }
        } catch (Exception e) {}
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public JButton getBtnSettings() { return btnSettings; }
    public JButton getBtnStart() { return btnStart; }
    public JButton getBtnExit() { return btnExit; }
}