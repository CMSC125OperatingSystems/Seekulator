package cmsc125.project4.models;

import cmsc125.project4.services.ThemeManager.Theme;

public class SettingsModel {
    private Theme currentTheme;

    public SettingsModel() {
        this.currentTheme = Theme.SYSTEM;
    }

    public Theme getCurrentTheme() { return currentTheme; }
    public void setCurrentTheme(Theme currentTheme) { this.currentTheme = currentTheme; }
}