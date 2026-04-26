package cmsc125.project4.views;

import javax.swing.*;
import java.awt.*;

public class SplashView extends JFrame {
    public SplashView() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Seekulator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 64));

        JLabel subtitleLabel = new JLabel("Disk Scheduling Algorithm Simulator");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 32));

        panel.add(titleLabel, gbc);
        panel.add(Box.createVerticalStrut(20), gbc);
        panel.add(subtitleLabel, gbc);

        add(panel);
    }
}