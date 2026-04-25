package cmsc125.project4.controllers;

import cmsc125.project4.models.SettingsModel;
import cmsc125.project4.services.*;
import cmsc125.project4.views.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AppController {
    private SplashView splashView;
    private DashboardView dashboardView;
    private SimulationView simulationView;
    private SettingsModel settingsModel;

    private Timer simulationTimer;
    private int currentAnimStep = 0;
    private FIFO currentAlgorithm;

    public AppController() {
        this.settingsModel = new SettingsModel();
        this.splashView = new SplashView();
        this.dashboardView = new DashboardView();
        this.simulationView = new SimulationView();

        initializeListeners();
    }

    public void start() {
        splashView.setVisible(true);
        Timer timer = new Timer(3000, (ActionEvent e) -> {
            splashView.dispose();
            dashboardView.setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void initializeListeners() {
        dashboardView.getBtnExit().addActionListener(e -> System.exit(0));
        dashboardView.getBtnSettings().addActionListener(e -> showSettingsDialog());

        dashboardView.getBtnStart().addActionListener(e -> {
            dashboardView.setVisible(false);
            simulationView.setVisible(true);
        });

        simulationView.getBtnBack().addActionListener(e -> {
            if (simulationTimer != null && simulationTimer.isRunning()) {
                simulationTimer.stop();
            }
            simulationView.setVisible(false);
            dashboardView.setVisible(true);
        });

        // Handle Input Type Selection Changes
        simulationView.getInputTypeCombo().addActionListener(e -> handleInputTypeChange());

        // Execute Simulation
        simulationView.getBtnSimulate().addActionListener(e -> executeSimulation());
    }

    private void handleInputTypeChange() {
        String selection = (String) simulationView.getInputTypeCombo().getSelectedItem();
        JTextField inputField = simulationView.getInputTextField();

        if ("Random".equals(selection)) {
            // Generate Random queue (Max 20 for visibility, bound 0-199)
            Random rand = new Random();
            int length = rand.nextInt(15) + 5; // 5 to 20 requests
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(rand.nextInt(200));
                if (i < length - 1) sb.append(",");
            }
            inputField.setText(sb.toString());
            simulationView.getHeadPositionSpinner().setValue(rand.nextInt(200));
            inputField.setEditable(false);

        } else if ("Text File Input".equals(selection)) {
            JFileChooser fileChooser = new JFileChooser("."); // Opens in current directory
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int result = fileChooser.showOpenDialog(simulationView);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    List<String> lines = Files.readAllLines(file.toPath());
                    if (lines.size() >= 1) inputField.setText(lines.get(0).trim());
                    if (lines.size() >= 2) simulationView.getHeadPositionSpinner().setValue(Integer.parseInt(lines.get(1).trim()));
                    if (lines.size() >= 3) {
                        String dir = lines.get(2).trim();
                        simulationView.getDirectionCombo().setSelectedItem(dir);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(simulationView, "Error reading file format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            inputField.setEditable(false);
        } else {
            inputField.setText("");
            inputField.setEditable(true);
        }
    }

    private void executeSimulation() {
        // 1. Parse Inputs
        String inputText = simulationView.getInputTextField().getText();
        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(simulationView, "Input queue cannot be empty.");
            return;
        }

        List<Cylinder> queue = new ArrayList<>();
        try {
            String[] tokens = inputText.split(",");
            if(tokens.length > 40) {
                JOptionPane.showMessageDialog(simulationView, "Maximum 40 requests allowed.");
                return;
            }
            for (String t : tokens) queue.add(new Cylinder(Integer.parseInt(t.trim())));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(simulationView, "Invalid numbers in input.");
            return;
        }

        int head = (int) simulationView.getHeadPositionSpinner().getValue();
        boolean direction = simulationView.getDirectionCombo().getSelectedIndex() == 0; // 0 = Up/Right (True)
        String algoSelection = (String) simulationView.getAlgorithmCombo().getSelectedItem();

        // 2. Instantiate Algorithm
        if (algoSelection.startsWith("FCFS")) currentAlgorithm = new FIFO(queue, head, direction);
        else if (algoSelection.startsWith("SSTF")) currentAlgorithm = new SSTF(queue, head, direction);
        else if (algoSelection.startsWith("SCAN")) currentAlgorithm = new SCAN(queue, head, direction);
        else if (algoSelection.startsWith("C-SCAN")) currentAlgorithm = new C_SCAN(queue, head, direction);
        else if (algoSelection.startsWith("LOOK")) currentAlgorithm = new LOOK(queue, head, direction);
        else if (algoSelection.startsWith("C-LOOK")) currentAlgorithm = new C_LOOK(queue, head, direction);

        // 3. Prepare the Graph path
        currentAlgorithm.preparePath();

        // FIX: Create a deep copy of the calculated path to pass to the GraphPanel.
        // This prevents the graph from breaking when executeStep() removes elements.
        List<Cylinder> fullPath = new ArrayList<>(currentAlgorithm.getPath());
        simulationView.getGraphPanel().setSimulationData(fullPath, head);
        simulationView.getLblTotalMovement().setText("Total Head Movement: Calculating...");

        // FIX: Cache the total steps so the shrinking internal path doesn't stop the timer early.
        int totalSteps = fullPath.size();

        // 4. Start Animation Timer
        if (simulationTimer != null && simulationTimer.isRunning()) simulationTimer.stop();

        currentAnimStep = 0;

        // Adjust speed based on combo box
        String speedStr = (String) simulationView.getSpeedCombo().getSelectedItem();
        double multiplier = Double.parseDouble(speedStr.replace("x", ""));
        int delay = (int) (1000 / multiplier); // 1000ms base delay

        simulationTimer = new Timer(delay, e -> {
            // Check against the cached totalSteps
            if (currentAnimStep < totalSteps) {
                simulationView.getGraphPanel().setStep(currentAnimStep);
                currentAlgorithm.executeStep(); // Step logically
                currentAnimStep++;
            } else {
                ((Timer)e.getSource()).stop();
                simulationView.getLblTotalMovement().setText("Total Head Movement: " + currentAlgorithm.getTotalMovement());
            }
        });
        simulationTimer.start();
    }

    private void showSettingsDialog() {
        SettingsDialog dialog = new SettingsDialog(dashboardView);

        dialog.getSfxSlider().setValue(settingsModel.getSfxVolume());
        dialog.getBgmSlider().setValue(settingsModel.getBgmVolume());

        dialog.getBtnSaveClose().addActionListener(e -> {
            settingsModel.setSfxVolume(dialog.getSfxSlider().getValue());
            settingsModel.setBgmVolume(dialog.getBgmSlider().getValue());
            dialog.dispose();
        });

        dialog.setVisible(true);
    }
}