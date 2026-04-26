package cmsc125.project4.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardView extends JFrame {
    private JButton btnSettings;
    private JButton btnStart;
    private JButton btnExit;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton btnHelpTab;
    private JButton btnAboutTab;

    // Image Slider variables
    private int currentHelpImage = 1;
    private final int MAX_HELP_IMAGES = 4;
    private ScalableImagePanel imagePanel; // Custom dynamic scaling panel
    private JTextArea stepDescriptionArea;

    private final String[] stepDescriptions = {
            "Step 1: Inputting Data\nChoose how you want to generate the request queue using the dropdown. " +
                    "You can manually type your comma-separated values (between 0 and 199), generate a 'Random' queue, " +
                    "or load from a '.txt' file. The queue is limited to a maximum of 40 requests.",

            "Step 2: Algorithm & Head Configuration\nSelect the Disk Scheduling Algorithm you want to simulate " +
                    "(FCFS, SSTF, SCAN, C-SCAN, LOOK, C-LOOK). Next, set the starting position of the Read/Write Head " +
                    "(0-199) and choose the initial movement direction (Up/Right or Down/Left).",

            "Step 3: Simulation & Speed Control\nClick the 'Simulate' button to start the visualization! " +
                    "You can adjust the playback speed (0.5x to 2.0x) using the speed dropdown at the bottom. " +
                    "Watch as the head services the requests and tracks the total steps.",

            "Step 4: Exporting Results\nOnce the simulation is complete and the total head movement is calculated, " +
                    "click the 'Export' button at the bottom right. This will save a PNG image of your generated graph " +
                    "and its detailed metrics to your computer for record-keeping."
    };

    private final Dimension defaultSize = new Dimension(1024, 768);

    public DashboardView() {
        setTitle("Seekulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Keep minimum bounds, but open maximized
        setMinimumSize(defaultSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JLabel headerLabel = new JLabel("Seekulator", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 48));
        headerLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBorder(new EmptyBorder(0, 40, 0, 40));

        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnHelpTab = createTabButton("How to Play");
        btnAboutTab = createTabButton("About");
        tabBar.add(btnHelpTab);
        tabBar.add(btnAboutTab);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(createHelpPanel(), "HELP");
        cardPanel.add(createAboutPanel(), "ABOUT");

        btnHelpTab.addActionListener(e -> switchTab("HELP"));
        btnAboutTab.addActionListener(e -> switchTab("ABOUT"));

        centerWrapper.add(tabBar, BorderLayout.NORTH);
        centerWrapper.add(cardPanel, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        btnSettings = createIconButton("Settings");
        btnExit = createIconButton("Exit");

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
        switchTab("HELP");
    }

    public void updateIcons(boolean isDark) {
        String suffix = isDark ? "_light.png" : "_dark.png";
        updateButtonIcon(btnSettings, "/icons/settings" + suffix, 32, 32);
        updateButtonIcon(btnExit, "/icons/exit" + suffix, 32, 32);
    }

    private void updateButtonIcon(JButton btn, String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {}
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

        stepDescriptionArea = new JTextArea();
        stepDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 18));
        stepDescriptionArea.setWrapStyleWord(true);
        stepDescriptionArea.setLineWrap(true);
        stepDescriptionArea.setEditable(false);
        stepDescriptionArea.setMargin(new Insets(20, 20, 15, 20));
        panel.add(stepDescriptionArea, BorderLayout.NORTH);

        JPanel imagePlaceholder = new JPanel(new BorderLayout());

        // Initialize the custom dynamic scaling panel
        imagePanel = new ScalableImagePanel();

        JButton btnLeft = new JButton("<");
        JButton btnRight = new JButton(">");
        btnLeft.setFont(new Font("Arial", Font.BOLD, 48));
        btnRight.setFont(new Font("Arial", Font.BOLD, 48));
        btnLeft.setContentAreaFilled(false);
        btnLeft.setBorderPainted(false);
        btnLeft.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRight.setContentAreaFilled(false);
        btnRight.setBorderPainted(false);
        btnRight.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLeft.addActionListener(e -> {
            if (currentHelpImage > 1) {
                currentHelpImage--;
                updateHelpContent();
            }
        });

        btnRight.addActionListener(e -> {
            if (currentHelpImage < MAX_HELP_IMAGES) {
                currentHelpImage++;
                updateHelpContent();
            }
        });

        imagePlaceholder.add(btnLeft, BorderLayout.WEST);
        imagePlaceholder.add(imagePanel, BorderLayout.CENTER);
        imagePlaceholder.add(btnRight, BorderLayout.EAST);

        panel.add(imagePlaceholder, BorderLayout.CENTER);

        updateHelpContent();

        return panel;
    }

    private void updateHelpContent() {
        stepDescriptionArea.setText(stepDescriptions[currentHelpImage - 1]);

        String path = "/images/help" + currentHelpImage + ".png";
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                Image img = new ImageIcon(imgURL).getImage();
                imagePanel.setImage(img);
            } else {
                imagePanel.setImage(null);
            }
        } catch (Exception e) {
            imagePanel.setImage(null);
        }
    }

    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Custom.borderColor")));

        JTextArea topDesc = new JTextArea(
                "Seekulator is a Disk Scheduling Algorithm Simulator designed to visualize how various algorithms " +
                        "(FCFS, SSTF, SCAN, C-SCAN, LOOK, C-LOOK) service disk I/O requests."
        );
        topDesc.setFont(new Font("Arial", Font.PLAIN, 18));
        topDesc.setWrapStyleWord(true);
        topDesc.setLineWrap(true);
        topDesc.setEditable(false);
        topDesc.setMargin(new Insets(25, 25, 10, 25));
        panel.add(topDesc, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        Font titleFont = new Font("Arial", Font.BOLD, 20);
        Font regFont = new Font("Arial", Font.PLAIN, 18);

        JLabel authorsTitle = new JLabel("Authors:");
        authorsTitle.setFont(titleFont);
        authorsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel author1 = new JLabel("ali1x3");
        author1.setFont(regFont);
        author1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel author2 = new JLabel("ddrhckrzz");
        author2.setFont(regFont);
        author2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel author3 = new JLabel("Schneidelstrom");
        author3.setFont(regFont);
        author3.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel repoTitle = new JLabel("GitHub Repository:");
        repoTitle.setFont(titleFont);
        repoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel repoLink = new JLabel("<html><a href=''>https://github.com/CMSC125OperatingSystems/Seekulator.git</a></html>");
        repoLink.setFont(regFont);
        repoLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        repoLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        repoLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI("https://github.com/CMSC125OperatingSystems/Seekulator.git"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JLabel copyright = new JLabel("2026 Copyright. This project is strictly for academic purposes only.");
        copyright.setFont(new Font("Arial", Font.ITALIC, 14));
        copyright.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(authorsTitle);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(author1);
        centerPanel.add(author2);
        centerPanel.add(author3);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(repoTitle);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(repoLink);
        centerPanel.add(Box.createVerticalStrut(60));
        centerPanel.add(copyright);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createIconButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
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

    /**
     * Custom JPanel that dynamically scales an image to fit its bounds
     * while maintaining the original aspect ratio.
     */
    private class ScalableImagePanel extends JPanel {
        private Image img;

        public void setImage(Image img) {
            this.img = img;
            repaint(); // Trigger a redraw whenever the image changes
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (img != null) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imgWidth = img.getWidth(this);
                int imgHeight = img.getHeight(this);

                if (imgWidth > 0 && imgHeight > 0) {
                    // Calculate scaling factor to maintain aspect ratio
                    double scaleFactor = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                    int drawWidth = (int) (imgWidth * scaleFactor);
                    int drawHeight = (int) (imgHeight * scaleFactor);

                    // Center the image within the panel
                    int x = (panelWidth - drawWidth) / 2;
                    int y = (panelHeight - drawHeight) / 2;

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(img, x, y, drawWidth, drawHeight, this);
                }
            } else {
                // Fallback text if the image is missing
                g.setColor(UIManager.getColor("Label.foreground"));
                FontMetrics fm = g.getFontMetrics();
                String msg = "[Missing Image]";
                int x = (getWidth() - fm.stringWidth(msg)) / 2;
                int y = (getHeight() / 2) + (fm.getAscent() / 2);
                g.drawString(msg, x, y);
            }
        }
    }
}