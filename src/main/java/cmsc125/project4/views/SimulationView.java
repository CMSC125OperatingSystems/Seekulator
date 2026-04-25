package cmsc125.project4.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SimulationView extends JFrame {

    // Top Panel Components
    private JComboBox<String> inputTypeCombo;
    private JComboBox<String> algorithmCombo;
    private JSpinner headPositionSpinner;
    private JComboBox<String> directionCombo;
    private JTextField inputTextField;
    private JButton btnSimulate;

    // Center Panel Components
    private GraphPanel graphPanel;

    // Bottom Panel Components
    private JLabel lblTotalMovement;
    private JComboBox<String> speedCombo;
    private JLabel lblTime;
    private JButton btnExport;
    private JButton btnBack;

    public SimulationView() {
        setTitle("Seekulator - Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        initUI();
    }

    private void initUI() {
        // --- TOP PANEL ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(15, 20, 5, 20));

        // Row 1: Dropdowns and Spinners
        JPanel row1 = new JPanel(new BorderLayout());
        row1.setBackground(Color.WHITE);

        // Input Type Selection
        String[] inputTypes = {"User-Defined Input", "Random", "Text File Input"};
        inputTypeCombo = new JComboBox<>(inputTypes);
        inputTypeCombo.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(inputTypeCombo);
        row1.add(leftPanel, BorderLayout.WEST);

        // Algorithm Selection
        String[] algorithms = {
            "FCFS: First-Come, First-Served",
            "SSTF: Shortest Seek Time First",
            "SCAN", "C-SCAN", "LOOK", "C-LOOK"
        };
        algorithmCombo = new JComboBox<>(algorithms);
        algorithmCombo.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(algorithmCombo);
        row1.add(centerPanel, BorderLayout.CENTER);

        // Head Position & Direction Selection
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setBackground(Color.WHITE);

        JLabel lblHead = new JLabel("Head Position:");
        lblHead.setFont(new Font("Arial", Font.BOLD, 16));

        // Spinner bounded from 0 to 199
        headPositionSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 199, 1));
        headPositionSpinner.setFont(new Font("Arial", Font.BOLD, 16));

        // Added Direction Combo to satisfy requirements
        String[] directions = {"Up/Right", "Down/Left"};
        directionCombo = new JComboBox<>(directions);
        directionCombo.setFont(new Font("Arial", Font.BOLD, 14));

        rightPanel.add(lblHead);
        rightPanel.add(headPositionSpinner);
        rightPanel.add(directionCombo);
        row1.add(rightPanel, BorderLayout.EAST);

        // Row 2: Input Text Field and Simulate Button
        JPanel row2 = new JPanel(new BorderLayout(10, 0));
        row2.setBackground(Color.WHITE);
        row2.setBorder(new EmptyBorder(10, 0, 0, 0));

        inputTextField = new JTextField();
        inputTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        inputTextField.setToolTipText("Enter up to 40 requests separated by commas (e.g. 11,15,35,42)");

        btnSimulate = new JButton("Simulate");
        btnSimulate.setFont(new Font("Arial", Font.BOLD, 16));

        row2.add(inputTextField, BorderLayout.CENTER);
        row2.add(btnSimulate, BorderLayout.EAST);

        topPanel.add(row1);
        topPanel.add(row2);
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (Graph) ---
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setBorder(new EmptyBorder(10, 20, 10, 20));

        graphPanel = new GraphPanel();
        centerContainer.add(graphPanel, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        // --- BOTTOM PANEL ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(5, 20, 15, 20));

        // Stats & Speed
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsPanel.setBackground(Color.WHITE);

        lblTotalMovement = new JLabel("Total Head Movement: 0");
        lblTotalMovement.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        speedPanel.setBackground(Color.WHITE);
        JLabel lblSpeed = new JLabel("Speed:");
        lblSpeed.setFont(new Font("Arial", Font.BOLD, 14));
        String[] speeds = {"0.5x", "1.0x", "1.5x", "2.0x"};
        speedCombo = new JComboBox<>(speeds);
        speedCombo.setSelectedItem("1.0x");
        speedPanel.add(lblSpeed);
        speedPanel.add(speedCombo);

        lblTime = new JLabel("Time: 0s");
        lblTime.setFont(new Font("Arial", Font.BOLD, 14));

        statsPanel.add(lblTotalMovement);
        statsPanel.add(speedPanel);
        statsPanel.add(lblTime);

        // Actions (Export & Back)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(Color.WHITE);

        btnExport = createImageButton("/icons/export.png", "Export");
        btnBack = createImageButton("/icons/back.png", "Back");

        actionPanel.add(btnExport);
        actionPanel.add(btnBack);

        bottomPanel.add(statsPanel, BorderLayout.WEST);
        bottomPanel.add(actionPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createImageButton(String imagePath, String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
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
        }
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Getters for the Controller to hook up logic
    public JComboBox<String> getInputTypeCombo() { return inputTypeCombo; }
    public JComboBox<String> getAlgorithmCombo() { return algorithmCombo; }
    public JSpinner getHeadPositionSpinner() { return headPositionSpinner; }
    public JComboBox<String> getDirectionCombo() { return directionCombo; }
    public JTextField getInputTextField() { return inputTextField; }
    public JButton getBtnSimulate() { return btnSimulate; }
    public GraphPanel getGraphPanel() { return graphPanel; }
    public JLabel getLblTotalMovement() { return lblTotalMovement; }
    public JComboBox<String> getSpeedCombo() { return speedCombo; }
    public JLabel getLblTime() { return lblTime; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnBack() { return btnBack; }
}