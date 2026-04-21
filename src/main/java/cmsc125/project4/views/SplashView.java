package cmsc125.project4.views;

import javax.swing.*;
import java.awt.*;

public class SplashView extends JWindow {
    public SplashView() {
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Seekulator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));

        JLabel subtitleLabel = new JLabel("Disk Scheduling Algorithm Simulator");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        panel.add(titleLabel, gbc);
        panel.add(Box.createVerticalStrut(10), gbc);
        panel.add(subtitleLabel, gbc);

        add(panel);
    }
}