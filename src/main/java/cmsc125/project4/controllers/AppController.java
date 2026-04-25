package cmsc125.project4.controllers;

import cmsc125.project4.models.SettingsModel;
import cmsc125.project4.services.*;
import cmsc125.project4.views.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AppController {
    private SplashView splashView;
    private DashboardView dashboardView; // Acts as the main window
    private SimulationView simulationView;
    private SettingsModel settingsModel;
    private Container originalDashboardContent; // Used for swapping views

    private Timer simulationTimer;
    private int currentAnimStep = 0;
    private FIFO currentAlgorithm;

    public AppController() {
        this.settingsModel = new SettingsModel();
        this.splashView = new SplashView();
        this.dashboardView = new DashboardView();
        this.simulationView = new SimulationView();

        // Save the dashboard's initial layout so we can swap back to it
        this.originalDashboardContent = dashboardView.getContentPane();

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

        // Switch to Simulation Panel
        dashboardView.getBtnStart().addActionListener(e -> {
            simulationView.getGraphPanel().clear();
            dashboardView.setContentPane(simulationView);
            dashboardView.revalidate();
            dashboardView.repaint();
        });

        // Switch back to Dashboard Panel
        simulationView.getBtnBack().addActionListener(e -> {
            if (simulationTimer != null && simulationTimer.isRunning()) {
                simulationTimer.stop();
            }
            dashboardView.setContentPane(originalDashboardContent);
            dashboardView.revalidate();
            dashboardView.repaint();
        });

        simulationView.getInputTypeCombo().addActionListener(e -> handleInputTypeChange());
        simulationView.getBtnSimulate().addActionListener(e -> executeSimulation());
        simulationView.getBtnExport().addActionListener(e -> exportSimulation());
    }

    private void handleInputTypeChange() {
        String selection = (String) simulationView.getInputTypeCombo().getSelectedItem();
        JTextField inputField = simulationView.getInputTextField();

        if ("Random".equals(selection)) {
            Random rand = new Random();
            int length = rand.nextInt(15) + 5;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(rand.nextInt(200));
                if (i < length - 1) sb.append(",");
            }
            inputField.setText(sb.toString());
            simulationView.getHeadPositionSpinner().setValue(rand.nextInt(200));
            inputField.setEditable(false);

        } else if ("Text File Input".equals(selection)) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            int result = fileChooser.showOpenDialog(dashboardView);

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
                    JOptionPane.showMessageDialog(dashboardView, "Error reading file format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            inputField.setEditable(false);
        } else {
            inputField.setText("");
            inputField.setEditable(true);
        }
    }

    private void executeSimulation() {
        String inputText = simulationView.getInputTextField().getText();
        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(dashboardView, "Input queue cannot be empty.");
            return;
        }

        List<Cylinder> queue = new ArrayList<>();
        try {
            String[] tokens = inputText.split(",");
            if(tokens.length > 40) {
                JOptionPane.showMessageDialog(dashboardView, "Maximum 40 requests allowed.");
                return;
            }
            for (String t : tokens) queue.add(new Cylinder(Integer.parseInt(t.trim())));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dashboardView, "Invalid numbers in input.");
            return;
        }

        int head = (int) simulationView.getHeadPositionSpinner().getValue();
        boolean direction = simulationView.getDirectionCombo().getSelectedIndex() == 0;
        String algoSelection = (String) simulationView.getAlgorithmCombo().getSelectedItem();

        if (algoSelection.startsWith("FCFS")) currentAlgorithm = new FIFO(queue, head, direction);
        else if (algoSelection.startsWith("SSTF")) currentAlgorithm = new SSTF(queue, head, direction);
        else if (algoSelection.startsWith("SCAN")) currentAlgorithm = new SCAN(queue, head, direction);
        else if (algoSelection.startsWith("C-SCAN")) currentAlgorithm = new C_SCAN(queue, head, direction);
        else if (algoSelection.startsWith("LOOK")) currentAlgorithm = new LOOK(queue, head, direction);
        else if (algoSelection.startsWith("C-LOOK")) currentAlgorithm = new C_LOOK(queue, head, direction);

        currentAlgorithm.preparePath();

        List<Cylinder> fullPath = new ArrayList<>(currentAlgorithm.getPath());
        simulationView.getGraphPanel().setSimulationData(fullPath, head);
        simulationView.getLblTotalMovement().setText("Total Head Movement: Calculating...");
        simulationView.getLblSteps().setText("Steps: 0");

        int totalSteps = fullPath.size();

        if (simulationTimer != null && simulationTimer.isRunning()) simulationTimer.stop();

        currentAnimStep = 0;

        String speedStr = (String) simulationView.getSpeedCombo().getSelectedItem();
        double multiplier = Double.parseDouble(speedStr.replace("x", ""));
        int delay = (int) (1000 / multiplier);

        simulationTimer = new Timer(delay, e -> {
            if (currentAnimStep < totalSteps) {
                simulationView.getGraphPanel().setStep(currentAnimStep);
                currentAlgorithm.executeStep();
                currentAnimStep++;
                simulationView.getLblSteps().setText("Steps: " + currentAnimStep);
            } else {
                ((Timer)e.getSource()).stop();
                simulationView.getLblTotalMovement().setText("Total Head Movement: " + currentAlgorithm.getTotalMovement());
            }
        });
        simulationTimer.start();
    }

    private void exportSimulation() {
        if (currentAlgorithm == null) {
            JOptionPane.showMessageDialog(dashboardView, "Please run a simulation before exporting.", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Get simulation metadata
            String algo = (String) simulationView.getAlgorithmCombo().getSelectedItem();
            int head = (int) simulationView.getHeadPositionSpinner().getValue();
            String dir = (String) simulationView.getDirectionCombo().getSelectedItem();
            int totalMove = currentAlgorithm.getTotalMovement();

            // Format filename
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String filename = sdf.format(new Date()) + "_DS.png";

            GraphPanel graph = simulationView.getGraphPanel();
            int width = graph.getWidth();
            int height = graph.getHeight() + 100; // Extend canvas height for text

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();

            // Draw background
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);

            // Stamp Graph onto image
            graph.paint(g2d);

            // Draw Metadata Text at the bottom
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));

            int textYStart = graph.getHeight() + 25;
            g2d.drawString("Algorithm: " + algo, 20, textYStart);
            g2d.drawString("Initial Head Position: " + head, 20, textYStart + 25);
            g2d.drawString("Direction: " + dir, 20, textYStart + 50);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Total Head Movement: " + totalMove, width / 2, textYStart + 25);

            g2d.dispose();

            ImageIO.write(img, "png", new File(filename));
            JOptionPane.showMessageDialog(dashboardView, "Exported successfully to: \n" + new File(filename).getAbsolutePath(), "Export Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dashboardView, "Failed to export image.", "Export Error", JOptionPane.ERROR_MESSAGE);
        }
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