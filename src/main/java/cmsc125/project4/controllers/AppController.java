package cmsc125.project4.controllers;

import cmsc125.project4.models.SettingsModel;
import cmsc125.project4.views.DashboardView;
import cmsc125.project4.views.SettingsDialog;
import cmsc125.project4.views.SplashView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AppController {
    private SplashView splashView;
    private DashboardView dashboardView;
    private SettingsModel settingsModel;

    public AppController() {
        this.settingsModel = new SettingsModel();
        this.splashView = new SplashView();
        this.dashboardView = new DashboardView();

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
        dashboardView.getBtnExit().addActionListener(e -> System.exit(0));

        dashboardView.getBtnSettings().addActionListener(e -> showSettingsDialog());

        dashboardView.getBtnStart().addActionListener(e -> {
            // Trigger simulation view or panel transition here
            System.out.println("Transition to Simulation View");
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