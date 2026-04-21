package cmsc125.project4.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private JSlider sfxSlider;
    private JSlider bgmSlider;
    private JButton btnSaveClose;

    public SettingsDialog(Frame owner) {
        super(owner, "Settings", true);
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel slidersPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        slidersPanel.setBackground(Color.WHITE);
        slidersPanel.setBorder(new EmptyBorder(10, 30, 10, 30));

        // SFX Row
        JPanel sfxPanel = new JPanel(new BorderLayout());
        sfxPanel.setBackground(Color.WHITE);
        sfxPanel.add(new JLabel("SFX  "), BorderLayout.WEST);
        sfxSlider = new JSlider(0, 100, 100);
        sfxSlider.setBackground(Color.WHITE);
        sfxPanel.add(sfxSlider, BorderLayout.CENTER);
        slidersPanel.add(sfxPanel);

        // BGM Row
        JPanel bgmPanel = new JPanel(new BorderLayout());
        bgmPanel.setBackground(Color.WHITE);
        bgmPanel.add(new JLabel("BGM "), BorderLayout.WEST);
        bgmSlider = new JSlider(0, 100, 100);
        bgmSlider.setBackground(Color.WHITE);
        bgmPanel.add(bgmSlider, BorderLayout.CENTER);
        slidersPanel.add(bgmPanel);

        add(slidersPanel, BorderLayout.CENTER);

        btnSaveClose = new JButton("Save & Close");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnSaveClose);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JSlider getSfxSlider() { return sfxSlider; }
    public JSlider getBgmSlider() { return bgmSlider; }
    public JButton getBtnSaveClose() { return btnSaveClose; }
}