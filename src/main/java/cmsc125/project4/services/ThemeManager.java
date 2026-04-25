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
        Color border = isDark ? new Color(100, 100, 100) : Color.BLACK;

        UIManager.put("Panel.background", bg);
        UIManager.put("Label.foreground", fg);
        UIManager.put("OptionPane.background", bg);
        UIManager.put("OptionPane.messageForeground", fg);
        UIManager.put("Custom.borderColor", border);

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
                // Fallback to light if unreadable
            } else if (os.contains("mac")) {
                Process process = Runtime.getRuntime().exec("defaults read -g AppleInterfaceStyle");
                return process.waitFor() == 0;
            } else {
                // Linux / Unix environment

                // 1. Try FreeDesktop DBus Portal (Standard for Wayland/Modern DEs)
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
                        return true; // 1 = Dark Mode
                    }
                } catch (Exception e) {}

                // 2. Fallback for KDE Plasma 6 specifically
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

                // 3. Fallback to older GTK theme env var
                String gtkTheme = System.getenv("GTK_THEME");
                if (gtkTheme != null && gtkTheme.toLowerCase().contains("dark")) {
                    return true;
                }
            }
        } catch (Exception e) {}

        return false;
    }
}