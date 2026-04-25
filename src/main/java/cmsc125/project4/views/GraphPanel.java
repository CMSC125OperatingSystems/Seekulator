package cmsc125.project4.views;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    private final int MIN_CYLINDER = 0;
    private final int MAX_CYLINDER = 199;
    private final int PADDING = 30; // Padding from the edges

    public GraphPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // horizontal axis
        int axisY = 50;
        int startX = PADDING;
        int endX = width - PADDING;

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(startX, axisY, endX, axisY);

        // boundaries
        drawTickAndLabel(g2d, startX, axisY, String.valueOf(MIN_CYLINDER));
        drawTickAndLabel(g2d, endX, axisY, String.valueOf(MAX_CYLINDER));

        // Note: Future simulation drawing logic (dots, text labels for requests,
        // and lines mapping the path) will be implemented here.
    }

    private void drawTickAndLabel(Graphics2D g2d, int x, int y, String label) {
        // vertical tick mark
        g2d.drawLine(x, y - 10, x, y + 10);

        // label text above tick
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString(label, x - (textWidth / 2), y - 15);
    }
}