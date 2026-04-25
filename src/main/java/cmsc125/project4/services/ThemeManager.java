package cmsc125.project4.services;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ThemeManager {
    public enum Theme { LIGHT, DARK, SYSTEM }

    public static void applyTheme(Theme theme) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isDark = (theme == Theme.SYSTEM) ? isSystemDarkMode() : (theme == Theme.DARK);

        Color bg = isDark ? new Color(30, 30, 30) : Color.WHITE;
        Color panelBg = isDark ? new Color(50, 50, 50) : new Color(245, 245, 245);
        Color fg = isDark ? new Color(200, 200, 200) : Color.BLACK;
        Color border = isDark ? new Color(100, 100, 100) : new Color(150, 150, 150);

        // Highlight colors for dropdown selections
        Color selectionBg = isDark ? new Color(70, 100, 140) : new Color(180, 210, 255);
        Color selectionFg = isDark ? Color.WHITE : Color.BLACK;

        // Standard properties
        UIManager.put("Panel.background", bg);
        UIManager.put("Label.foreground", fg);
        UIManager.put("OptionPane.background", bg);
        UIManager.put("OptionPane.messageForeground", fg);
        UIManager.put("Custom.borderColor", border);

        // --- METAL LAF SPECIFIC OVERRIDES ---
        // These fix the invisible arrows on JComboBox and JSpinner
        UIManager.put("control", panelBg);           // Base button/control background
        UIManager.put("controlText", fg);            // Base text and ARROW color
        UIManager.put("text", fg);
        UIManager.put("textText", fg);
        UIManager.put("controlHighlight", bg);       // Highlight edge
        UIManager.put("controlShadow", border);      // Shadow edge
        UIManager.put("controlDkShadow", border);    // Dark shadow edge

        // ComboBox Dropdown Colors
        UIManager.put("ComboBox.background", panelBg);
        UIManager.put("ComboBox.foreground", fg);
        UIManager.put("ComboBox.selectionBackground", selectionBg);
        UIManager.put("ComboBox.selectionForeground", selectionFg);
        UIManager.put("ComboBox.buttonBackground", panelBg);
        UIManager.put("ComboBox.buttonShadow", border);
        UIManager.put("ComboBox.buttonDarkShadow", border);
        UIManager.put("ComboBox.buttonHighlight", bg);

        // Spinner Colors
        UIManager.put("Spinner.background", panelBg);
        UIManager.put("Spinner.foreground", fg);

        for (Window window : Window.getWindows()) {
            applyColorsToComponentTree(window, bg, panelBg, fg);
            SwingUtilities.updateComponentTreeUI(window);
            window.revalidate();
            window.repaint();
        }
    }

    public static void applyThemeToComponent(Component root, Theme theme) {
        boolean isDark = (theme == Theme.SYSTEM) ? isSystemDarkMode() : (theme == Theme.DARK);
        Color bg = isDark ? new Color(30, 30, 30) : Color.WHITE;
        Color panelBg = isDark ? new Color(50, 50, 50) : new Color(245, 245, 245);
        Color fg = isDark ? new Color(200, 200, 200) : Color.BLACK;

        applyColorsToComponentTree(root, bg, panelBg, fg);
        SwingUtilities.updateComponentTreeUI(root);
        root.revalidate();
        root.repaint();
    }

    private static void applyColorsToComponentTree(Component c, Color bg, Color panelBg, Color fg) {
        if (c instanceof JPanel || c instanceof JScrollPane || c instanceof JViewport) {
            c.setBackground(bg);
            c.setForeground(fg);
        } else if (c instanceof JTextArea || c instanceof JTextField || c instanceof JSpinner || c instanceof JComboBox) {
            c.setBackground(panelBg);
            c.setForeground(fg);
            if (c instanceof JTextField) {
                ((JTextField) c).setCaretColor(fg);
            }
        } else if (c instanceof JLabel) {
            c.setForeground(fg);
        } else if (c instanceof JButton) {
            JButton b = (JButton) c;
            b.setForeground(fg);

            // Only force background if it's a standard button, leave icon buttons alone
            if (b.isContentAreaFilled()) {
                b.setBackground(panelBg);
            }
        }

        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                applyColorsToComponentTree(child, bg, panelBg, fg);
            }
        }
    }

    private static boolean isSystemDarkMode() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Process process = Runtime.getRuntime().exec("reg query \"HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize\" /v AppsUseLightTheme");
            } else if (os.contains("mac")) {
                Process process = Runtime.getRuntime().exec("defaults read -g AppleInterfaceStyle");
                return process.waitFor() == 0;
            } else {
                try {
                    Process process = Runtime.getRuntime().exec(new String[]{
                        "dbus-send", "--session", "--print-reply=literal",
                        "--dest=org.freedesktop.portal.Desktop",
                        "/org/freedesktop/portal/desktop",
                        "org.freedesktop.portal.Settings.Read",
                        "string:org.freedesktop.appearance", "string:color-scheme"
                    });
                    process.waitFor();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = reader.readLine();
                    if (line != null && line.contains("uint32 1")) {
                        return true;
                    }
                } catch (Exception e) {}

                try {
                    String desktop = System.getenv("XDG_CURRENT_DESKTOP");
                    if (desktop != null && desktop.toLowerCase().contains("kde")) {
                        Process process = Runtime.getRuntime().exec(new String[]{
                            "kreadconfig6", "--group", "General", "--key", "ColorScheme"
                        });
                        process.waitFor();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line = reader.readLine();
                        if (line != null && line.toLowerCase().contains("dark")) {
                            return true;
                        }
                    }
                } catch (Exception e) {}

                String gtkTheme = System.getenv("GTK_THEME");
                if (gtkTheme != null && gtkTheme.toLowerCase().contains("dark")) {
                    return true;
                }
            }
        } catch (Exception e) {}

        return false;
    }
}