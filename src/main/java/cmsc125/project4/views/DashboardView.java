package cmsc125.project4.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardView extends JFrame {
    private JButton btnSettings;
    private JButton btnStart;
    private JButton btnExit;

    // Custom Tab Components
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton btnHelpTab;
    private JButton btnAboutTab;

    private final Dimension defaultSize = new Dimension(1024, 768);

    public DashboardView() {
        setTitle("Seekulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(defaultSize);
        setSize(defaultSize);
        setPreferredSize(defaultSize);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headerLabel = new JLabel("Seekulator", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 48));
        headerLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // --- Custom Tab System ---
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBorder(new EmptyBorder(0, 40, 0, 40));

        // Tab Buttons
        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnHelpTab = createTabButton("How to Play");
        btnAboutTab = createTabButton("About");
        tabBar.add(btnHelpTab);
        tabBar.add(btnAboutTab);

        // Tab Content (CardLayout)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(createHelpPanel(), "HELP");
        cardPanel.add(createAboutPanel(), "ABOUT");

        // Tab Logic
        btnHelpTab.addActionListener(e -> switchTab("HELP"));
        btnAboutTab.addActionListener(e -> switchTab("ABOUT"));

        centerWrapper.add(tabBar, BorderLayout.NORTH);
        centerWrapper.add(cardPanel, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        // --- Footer ---
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

        // Initialize default tab state
        switchTab("HELP");
    }

    private JButton createTabButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void switchTab(String tabName) {
        cardLayout.show(cardPanel, tabName);

        // Emulate selection styling by underlining the active tab
        if ("HELP".equals(tabName)) {
            btnHelpTab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, UIManager.getColor("Label.foreground")),
                new EmptyBorder(7, 15, 10, 15)
            ));
            btnAboutTab.setBorder(new EmptyBorder(10, 15, 10, 15));
        } else {
            btnAboutTab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, UIManager.getColor("Label.foreground")),
                new EmptyBorder(7, 15, 10, 15)
            ));
            btnHelpTab.setBorder(new EmptyBorder(10, 15, 10, 15));
        }
    }

    private JPanel createHelpPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Custom.borderColor")));

        JTextArea descArea = new JTextArea("[Description]\nLorem ipsum dolor sit amet...");
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
            "Lorem ipsum dolor sit amet...\n\nAuthors:\n[Author 1]\n[Author 2]\n[Author 3]\n\nGitHub Repository Link\n\n2026 Copyright. This project is duly for academic purposes only."
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
            if (imgURL != null) btn.setIcon(new ImageIcon(imgURL));
        } catch (Exception e) {}
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
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