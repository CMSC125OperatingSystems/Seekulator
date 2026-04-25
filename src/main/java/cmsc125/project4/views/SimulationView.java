package cmsc125.project4.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SimulationView extends JPanel {

    private JComboBox<String> inputTypeCombo;
    private JComboBox<String> algorithmCombo;
    private JSpinner headPositionSpinner;
    private JComboBox<String> directionCombo;
    private JTextField inputTextField;
    private JButton btnSimulate;
    private GraphPanel graphPanel;
    private JLabel lblTotalMovement;
    private JComboBox<String> speedCombo;
    private JLabel lblSteps;
    private JButton btnExport;
    private JButton btnBack;

    public SimulationView() {
        setLayout(new BorderLayout(10, 10));
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(15, 20, 5, 20));

        JPanel row1 = new JPanel(new BorderLayout());

        String[] inputTypes = {"User-Defined Input", "Random", "Text File Input"};
        inputTypeCombo = new JComboBox<>(inputTypes);
        inputTypeCombo.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.add(inputTypeCombo);
        row1.add(leftPanel, BorderLayout.WEST);

        String[] algorithms = {
            "FCFS: First-Come, First-Served", "SSTF: Shortest Seek Time First",
            "SCAN", "C-SCAN", "LOOK", "C-LOOK"
        };
        algorithmCombo = new JComboBox<>(algorithms);
        algorithmCombo.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.add(algorithmCombo);
        row1.add(centerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        JLabel lblHead = new JLabel("Head Position: ");
        lblHead.setFont(new Font("Arial", Font.BOLD, 18));

        headPositionSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 199, 1));
        headPositionSpinner.setFont(new Font("Arial", Font.BOLD, 18));

        String[] directions = {"Up/Right", "Down/Left"};
        directionCombo = new JComboBox<>(directions);
        directionCombo.setFont(new Font("Arial", Font.BOLD, 16));

        rightPanel.add(lblHead);
        rightPanel.add(headPositionSpinner);
        rightPanel.add(directionCombo);
        row1.add(rightPanel, BorderLayout.EAST);

        JPanel row2 = new JPanel(new BorderLayout(10, 0));
        row2.setBorder(new EmptyBorder(10, 0, 0, 0));

        inputTextField = new JTextField();
        inputTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputTextField.setToolTipText("Enter up to 40 requests separated by commas (e.g. 11,15,35,42)");

        btnSimulate = new JButton("Simulate");
        btnSimulate.setFont(new Font("Arial", Font.BOLD, 18));

        row2.add(inputTextField, BorderLayout.CENTER);
        row2.add(btnSimulate, BorderLayout.EAST);

        topPanel.add(row1);
        topPanel.add(row2);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBorder(new EmptyBorder(10, 20, 10, 20));

        graphPanel = new GraphPanel();
        centerContainer.add(graphPanel, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(5, 20, 15, 20));

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));

        lblTotalMovement = new JLabel("Total Head Movement: 0");
        lblTotalMovement.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel lblSpeed = new JLabel("Speed: ");
        lblSpeed.setFont(new Font("Arial", Font.BOLD, 20));
        String[] speeds = {"0.5x", "1.0x", "1.5x", "2.0x"};
        speedCombo = new JComboBox<>(speeds);
        speedCombo.setFont(new Font("Arial", Font.PLAIN, 18));
        speedCombo.setSelectedItem("1.0x");
        speedPanel.add(lblSpeed);
        speedPanel.add(speedCombo);

        lblSteps = new JLabel("Steps: 0");
        lblSteps.setFont(new Font("Arial", Font.BOLD, 20));

        statsPanel.add(lblTotalMovement);
        statsPanel.add(speedPanel);
        statsPanel.add(lblSteps);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));

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
        btn.setFont(new Font("Arial", Font.BOLD, 14));
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

    public JComboBox<String> getInputTypeCombo() { return inputTypeCombo; }
    public JComboBox<String> getAlgorithmCombo() { return algorithmCombo; }
    public JSpinner getHeadPositionSpinner() { return headPositionSpinner; }
    public JComboBox<String> getDirectionCombo() { return directionCombo; }
    public JTextField getInputTextField() { return inputTextField; }
    public JButton getBtnSimulate() { return btnSimulate; }
    public GraphPanel getGraphPanel() { return graphPanel; }
    public JLabel getLblTotalMovement() { return lblTotalMovement; }
    public JComboBox<String> getSpeedCombo() { return speedCombo; }
    public JLabel getLblSteps() { return lblSteps; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnBack() { return btnBack; }
}