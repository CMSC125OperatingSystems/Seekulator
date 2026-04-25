package cmsc125.project4.views;

import cmsc125.project4.services.ThemeManager.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private JComboBox<Theme> themeComboBox;
    private JButton btnSaveClose;

    public SettingsDialog(Frame owner) {
        super(owner, "Settings", true);
        setSize(400, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel themeLabel = new JLabel("Application Theme: ");
        themeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        themeComboBox = new JComboBox<>(Theme.values());
        themeComboBox.setFont(new Font("Arial", Font.PLAIN, 16));

        contentPanel.add(themeLabel);
        contentPanel.add(themeComboBox);
        add(contentPanel, BorderLayout.CENTER);

        btnSaveClose = new JButton("Save & Close");
        btnSaveClose.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSaveClose);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JComboBox<Theme> getThemeComboBox() { return themeComboBox; }
    public JButton getBtnSaveClose() { return btnSaveClose; }
}