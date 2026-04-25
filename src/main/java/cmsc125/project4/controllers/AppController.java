package cmsc125.project4.controllers;

import cmsc125.project4.models.SettingsModel;
import cmsc125.project4.views.DashboardView;
import cmsc125.project4.views.SettingsDialog;
import cmsc125.project4.views.SimulationView;
import cmsc125.project4.views.SplashView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AppController {
    private SplashView splashView;
    private DashboardView dashboardView;
    private SimulationView simulationView;
    private SettingsModel settingsModel;

    public AppController() {
        this.settingsModel = new SettingsModel();
        this.splashView = new SplashView();
        this.dashboardView = new DashboardView();
        this.simulationView = new SimulationView();

        initializeListeners();
    }

    public void start() {
        splashView.setVisible(true);

        // Simulate 3 seconds loading time then transition to Dashboard
        Timer timer = new Timer(3000, (ActionEvent e) -> {
            splashView.dispose();
            dashboardView.setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void initializeListeners() {
        // --- Dashboard View Listeners ---
        dashboardView.getBtnExit().addActionListener(e -> System.exit(0));

        dashboardView.getBtnSettings().addActionListener(e -> showSettingsDialog());

        // Transition: Dashboard -> Simulation
        dashboardView.getBtnStart().addActionListener(e -> {
            dashboardView.setVisible(false);
            simulationView.setVisible(true);
        });

        // --- Simulation View Listeners ---
        // Transition: Simulation -> Dashboard
        simulationView.getBtnBack().addActionListener(e -> {
            simulationView.setVisible(false);
            dashboardView.setVisible(true);
        });

        // Placeholder for the actual simulation logic trigger
        simulationView.getBtnSimulate().addActionListener(e -> {
            System.out.println("Executing Simulation...");
            // You will hook up your SimulationService algorithms here later
        });
    }

    private void showSettingsDialog() {
        SettingsDialog dialog = new SettingsDialog(dashboardView);

        // Populate view with current model data
        dialog.getSfxSlider().setValue(settingsModel.getSfxVolume());
        dialog.getBgmSlider().setValue(settingsModel.getBgmVolume());

        // Handle save action
        dialog.getBtnSaveClose().addActionListener(e -> {
            settingsModel.setSfxVolume(dialog.getSfxSlider().getValue());
            settingsModel.setBgmVolume(dialog.getBgmSlider().getValue());
            dialog.dispose();
        });

        dialog.setVisible(true);
    }
}