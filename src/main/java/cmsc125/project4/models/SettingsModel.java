package cmsc125.project4.models;

public class SettingsModel {
    private int sfxVolume;
    private int bgmVolume;

    public SettingsModel() {
        // Default settings
        this.sfxVolume = 100;
        this.bgmVolume = 100;
    }

    public int getSfxVolume() { return sfxVolume; }
    public void setSfxVolume(int sfxVolume) { this.sfxVolume = sfxVolume; }

    public int getBgmVolume() { return bgmVolume; }
    public void setBgmVolume(int bgmVolume) { this.bgmVolume = bgmVolume; }
}