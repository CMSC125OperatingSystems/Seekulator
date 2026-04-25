package cmsc125.project4.views;

import cmsc125.project4.services.Cylinder;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {

    private final int MIN_CYLINDER = 0;
    private final int MAX_CYLINDER = 199;
    private final int PADDING = 30;

    private List<Cylinder> path;
    private int initialHead;
    private int currentStep = 0;

    public GraphPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    public void setSimulationData(List<Cylinder> path, int initialHead) {
        this.path = path;
        this.initialHead = initialHead;
        this.currentStep = 0;
        repaint();
    }

    public void setStep(int step) {
        this.currentStep = step;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int axisY = 50;
        int startX = PADDING;
        int endX = width - PADDING;

        // Draw main axis
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(startX, axisY, endX, axisY);

        drawTickAndLabel(g2d, getXForCylinder(0, startX, endX), axisY, "0");
        drawTickAndLabel(g2d, getXForCylinder(199, startX, endX), axisY, "199");

        // If no data, stop drawing here
        if (path == null || path.isEmpty()) return;

        int yOffset = 40; // Pixels to drop per step
        int prevX = getXForCylinder(initialHead, startX, endX);
        int prevY = axisY + 20;

        // Draw the starting head position
        drawTickAndLabel(g2d, prevX, axisY, String.valueOf(initialHead));
        g2d.fillOval(prevX - 3, prevY - 3, 6, 6);

        // Draw paths up to the current simulation step
        for (int i = 0; i <= currentStep && i < path.size(); i++) {
            Cylinder current = path.get(i);
            int currX = getXForCylinder(current.data, startX, endX);
            int currY = prevY + yOffset;

            // Draw line from previous to current
            g2d.setColor(Color.BLACK);
            g2d.drawLine(prevX, prevY, currX, currY);

            // Draw dot
            if (current.virtual) {
                g2d.setColor(Color.RED); // Virtual boundaries in red
            } else {
                g2d.setColor(Color.BLUE);
                drawTickAndLabel(g2d, currX, axisY, String.valueOf(current.data));
            }
            g2d.fillOval(currX - 3, currY - 3, 6, 6);

            prevX = currX;
            prevY = currY;
        }
    }

    private int getXForCylinder(int cylinder, int startX, int endX) {
        int drawingWidth = endX - startX;
        return startX + (int) (((double) cylinder / MAX_CYLINDER) * drawingWidth);
    }

    private void drawTickAndLabel(Graphics2D g2d, int x, int y, String label) {
        g2d.drawLine(x, y - 5, x, y + 5);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(label, x - (textWidth / 2), y - 10);
    }
}