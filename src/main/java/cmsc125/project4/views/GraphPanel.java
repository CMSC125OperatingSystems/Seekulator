package cmsc125.project4.views;

import cmsc125.project4.services.Cylinder;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphPanel extends JPanel {

    private final int MIN_CYLINDER = 0;
    private final int MAX_CYLINDER = 199;
    private final int PADDING = 40;

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

    public void clear() {
        this.path = null;
        this.currentStep = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int axisY = 60;
        int startX = PADDING;
        int endX = width - PADDING;

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine(startX, axisY, endX, axisY);

        Set<Integer> uniquePoints = new HashSet<>();
        uniquePoints.add(MIN_CYLINDER);
        uniquePoints.add(MAX_CYLINDER);

        if (path != null && !path.isEmpty()) {
            uniquePoints.add(initialHead);
            for (Cylinder c : path) {
                uniquePoints.add(c.data);
            }
        } else {
            drawTickAndLabel(g2d, getXForCylinder(MIN_CYLINDER, startX, endX), axisY, String.valueOf(MIN_CYLINDER), axisY - 12);
            drawTickAndLabel(g2d, getXForCylinder(MAX_CYLINDER, startX, endX), axisY, String.valueOf(MAX_CYLINDER), axisY - 12);
            return;
        }

        // Sort points to calculate distances for overlap prevention
        List<Integer> sortedPoints = new ArrayList<>(uniquePoints);
        Collections.sort(sortedPoints);

        int prevLabelEndX = -100;
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();

        // Stagger labels to prevent overlapping
        for (int point : sortedPoints) {
            int xPos = getXForCylinder(point, startX, endX);
            int textWidth = fm.stringWidth(String.valueOf(point));
            int currentLabelStartX = xPos - (textWidth / 2);

            int yPos = axisY - 12; // Default height
            if (currentLabelStartX < prevLabelEndX + 8) {
                yPos = axisY - 28; // Stagger higher if overlapping
            }

            drawTickAndLabel(g2d, xPos, axisY, String.valueOf(point), yPos);
            prevLabelEndX = currentLabelStartX + textWidth;
        }

        int availableHeight = height - axisY - PADDING;
        int yOffset = Math.max(15, availableHeight / (path.size() + 1));

        int prevX = getXForCylinder(initialHead, startX, endX);
        int prevY = axisY + (yOffset / 2);

        g2d.setColor(Color.BLACK);
        g2d.fillOval(prevX - 4, prevY - 4, 8, 8);

        for (int i = 0; i <= currentStep && i < path.size(); i++) {
            Cylinder current = path.get(i);
            int currX = getXForCylinder(current.data, startX, endX);
            int currY = prevY + yOffset;

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawLine(prevX, prevY, currX, currY);

            boolean isCurrentPosition = (i == currentStep);

            if (isCurrentPosition) {
                g2d.setColor(new Color(255, 140, 0));
                g2d.fillOval(currX - 7, currY - 7, 14, 14);
                g2d.setColor(new Color(255, 140, 0, 80));
                g2d.fillOval(currX - 12, currY - 12, 24, 24);
            } else {
                if (current.virtual) g2d.setColor(Color.RED);
                else g2d.setColor(Color.BLUE);
                g2d.fillOval(currX - 4, currY - 4, 8, 8);
            }

            prevX = currX;
            prevY = currY;
        }
    }

    private int getXForCylinder(int cylinder, int startX, int endX) {
        int drawingWidth = endX - startX;
        return startX + (int) (((double) cylinder / MAX_CYLINDER) * drawingWidth);
    }

    private void drawTickAndLabel(Graphics2D g2d, int x, int y, String label, int labelYPos) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawLine(x, y - 6, x, y + 6);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        g2d.drawString(label, x - (textWidth / 2), labelYPos);
    }
}