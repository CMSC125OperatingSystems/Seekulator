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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class AppController {
    private SplashView splashView;
    private DashboardView dashboardView;
    private SimulationView simulationView;
    private SettingsModel settingsModel;
    private Container originalDashboardContent;

    private Timer simulationTimer;
    private int currentAnimStep = 0;
    private FIFO currentAlgorithm;

    public AppController() {
        this.settingsModel = new SettingsModel();

        this.splashView = new SplashView();
        this.dashboardView = new DashboardView();
        this.simulationView = new SimulationView();

        this.originalDashboardContent = dashboardView.getContentPane();

        ThemeManager.Theme currentTheme = settingsModel.getCurrentTheme();
        ThemeManager.applyTheme(currentTheme);
        ThemeManager.applyThemeToComponent(simulationView, currentTheme);

        boolean isDark = ThemeManager.isDarkTheme(currentTheme);
        dashboardView.updateIcons(isDark);
        simulationView.updateIcons(isDark);

        initializeListeners();
    }

    public void start() {
        splashView.setVisible(true);

        Timer timer = new Timer(2500, (ActionEvent e) -> {
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
            simulationView.getGraphPanel().clear();
            dashboardView.setContentPane(simulationView);
            dashboardView.revalidate();
            dashboardView.repaint();
        });

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
            simulationView.getDirectionCombo().setSelectedIndex(rand.nextInt(2));
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
            JOptionPane.showMessageDialog(dashboardView, "Input queue cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Cylinder> queue = new ArrayList<>();
        try {
            String[] tokens = inputText.split(",");
            if(tokens.length > 40) {
                JOptionPane.showMessageDialog(dashboardView, "Maximum 40 requests allowed.", "Boundary Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (String t : tokens) {
                int val = Integer.parseInt(t.trim());
                if (val < 0 || val > 199) {
                    JOptionPane.showMessageDialog(dashboardView, "Request queue values must be between 0 and 199.", "Boundary Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                queue.add(new Cylinder(val));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dashboardView, "Invalid numbers in input.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
            // 1. Gather Simulation Data
            String algo = (String) simulationView.getAlgorithmCombo().getSelectedItem();
            int head = (int) simulationView.getHeadPositionSpinner().getValue();
            String dir = (String) simulationView.getDirectionCombo().getSelectedItem();
            int totalMove = currentAlgorithm.getTotalMovement();
            String queueText = simulationView.getInputTextField().getText();

            // 2. Setup File Chooser with PNG and PDF options
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String defaultFilename = sdf.format(new Date()) + "_DS";

            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setDialogTitle("Save Exported Graph");

            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Image (*.png)", "png");
            FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Document (*.pdf)", "pdf");

            // Add both filters to the dropdown
            fileChooser.addChoosableFileFilter(pngFilter);
            fileChooser.addChoosableFileFilter(pdfFilter);
            fileChooser.setFileFilter(pngFilter); // Set PNG as default

            fileChooser.setSelectedFile(new File(defaultFilename));

            int userSelection = fileChooser.showSaveDialog(dashboardView);
            if (userSelection != JFileChooser.APPROVE_OPTION) {
                return; // User canceled
            }

            File fileToSave = fileChooser.getSelectedFile();
            javax.swing.filechooser.FileFilter selectedFilter = fileChooser.getFileFilter();

            // Determine if the user selected PDF or PNG
            boolean saveAsPdf = false;
            if (selectedFilter == pdfFilter || fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                saveAsPdf = true;
                if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                    fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".pdf");
                }
            } else {
                // Default to PNG
                if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                    fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".png");
                }
            }

            // 3. Create the Image (We do this for BOTH PNG and PDF)
            GraphPanel graph = simulationView.getGraphPanel();
            int width = graph.getWidth();
            int height = graph.getHeight() + 180;

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();

            Color bg = UIManager.getColor("Panel.background");
            g2d.setColor(bg != null ? bg : Color.WHITE);
            g2d.fillRect(0, 0, width, height);

            graph.paint(g2d);

            Color fg = UIManager.getColor("Label.foreground");
            g2d.setColor(fg != null ? fg : Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2d.getFontMetrics();

            int textYStart = graph.getHeight() + 25;
            g2d.drawString("Algorithm: " + algo, 20, textYStart);
            g2d.drawString("Initial Head Position: " + head, 20, textYStart + 25);
            g2d.drawString("Direction: " + dir, 20, textYStart + 50);

            int currentY = textYStart + 75;
            String[] queueItems = queueText.split(",");
            StringBuilder currentLine = new StringBuilder("Request Queue: ");

            for (int i = 0; i < queueItems.length; i++) {
                String addition = queueItems[i].trim() + (i == queueItems.length - 1 ? "" : ", ");

                if (fm.stringWidth(currentLine.toString() + addition) < width - 40) {
                    currentLine.append(addition);
                } else {
                    g2d.drawString(currentLine.toString(), 20, currentY);
                    currentY += 25;
                    currentLine = new StringBuilder("                             ").append(addition);
                }
            }
            g2d.drawString(currentLine.toString(), 20, currentY);

            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Total Head Movement: " + totalMove, width / 2, textYStart + 25);
            g2d.dispose();

            // 4. Save to the chosen format
            if (saveAsPdf) {
                // Save as PDF using Apache PDFBox
                try (PDDocument doc = new PDDocument()) {
                    // Create a PDF Page matching exactly the width and height of our image
                    PDPage page = new PDPage(new PDRectangle(width, height));
                    doc.addPage(page);

                    // Convert Java BufferedImage into a PDFBox image
                    PDImageXObject pdImage = LosslessFactory.createFromImage(doc, img);

                    // Draw the image exactly covering the PDF page
                    try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                        contentStream.drawImage(pdImage, 0, 0, width, height);
                    }

                    doc.save(fileToSave);
                }
            } else {
                // Save as standard PNG
                ImageIO.write(img, "png", fileToSave);
            }

            JOptionPane.showMessageDialog(dashboardView, "Exported successfully to: \n" + fileToSave.getAbsolutePath(), "Export Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dashboardView, "Failed to export file.", "Export Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showSettingsDialog() {
        SettingsDialog dialog = new SettingsDialog(dashboardView);
        dialog.getThemeComboBox().setSelectedItem(settingsModel.getCurrentTheme());

        dialog.getBtnSaveClose().addActionListener(e -> {
            ThemeManager.Theme selectedTheme = (ThemeManager.Theme) dialog.getThemeComboBox().getSelectedItem();
            settingsModel.setCurrentTheme(selectedTheme);

            ThemeManager.applyTheme(selectedTheme);
            ThemeManager.applyThemeToComponent(simulationView, selectedTheme);

            boolean isDark = ThemeManager.isDarkTheme(selectedTheme);
            dashboardView.updateIcons(isDark);
            simulationView.updateIcons(isDark);

            dialog.dispose();
        });
        dialog.setVisible(true);
    }
}