package cmsc125.project4.services;

import javax.swing.*;
import java.awt.*;
import java.awt.Window;

public class ThemeManager {
    public enum Theme { LIGHT, DARK, SYSTEM }

    public static void applyTheme(Theme theme) {
        boolean isDark = false;

        if (theme == Theme.SYSTEM) {
            isDark = isSystemDarkMode();
        } else {
            isDark = (theme == Theme.DARK);
        }

        Color bg = isDark ? new Color(30, 30, 30) : Color.WHITE;
        Color panelBg = isDark ? new Color(45, 45, 45) : new Color(245, 245, 245);
        Color fg = isDark ? new Color(240, 240, 240) : Color.BLACK;
        Color border = isDark ? new Color(100, 100, 100) : Color.BLACK;

        UIManager.put("Panel.background", bg);
        UIManager.put("Label.foreground", fg);
        UIManager.put("Button.background", panelBg);
        UIManager.put("Button.foreground", fg);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        UIManager.put("ComboBox.background", panelBg);
        UIManager.put("ComboBox.foreground", fg);
        UIManager.put("TextField.background", panelBg);
        UIManager.put("TextField.foreground", fg);
        UIManager.put("TextField.caretForeground", fg);
        UIManager.put("TextArea.background", bg);
        UIManager.put("TextArea.foreground", fg);
        UIManager.put("Spinner.background", panelBg);
        UIManager.put("Spinner.foreground", fg);
        UIManager.put("TabbedPane.background", bg);
        UIManager.put("TabbedPane.foreground", fg);
        UIManager.put("TabbedPane.selected", panelBg);
        UIManager.put("TabbedPane.contentAreaColor", border);
        UIManager.put("OptionPane.background", bg);
        UIManager.put("OptionPane.messageForeground", fg);

        // Store custom border color for custom drawing (like GraphPanel)
        UIManager.put("Custom.borderColor", border);

        // Refresh all active windows dynamically
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
            window.repaint();
        }
    }

    private static boolean isSystemDarkMode() {
        // Basic heuristic for OS dark mode detection
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Process process = Runtime.getRuntime().exec("reg query \"HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize\" /v AppsUseLightTheme");
                // Simplified: defaults to light if we can't easily parse it
            } else if (os.contains("mac")) {
                Process process = Runtime.getRuntime().exec("defaults read -g AppleInterfaceStyle");
                return process.waitFor() == 0; // 0 means Dark is set
            } else {
                // Linux / Unix fallback (Checks standard GTK theme env variables if available)
                String gtkTheme = System.getenv("GTK_THEME");
                if (gtkTheme != null && gtkTheme.toLowerCase().contains("dark")) {
                    return true;
                }
            }
        } catch (Exception e) {}
        return false; // Fallback to Light mode
    }
}