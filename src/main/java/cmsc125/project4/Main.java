package cmsc125.project4;

import cmsc125.project4.controllers.AppController;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppController appController = new AppController();
            appController.start();
        });
    }
}