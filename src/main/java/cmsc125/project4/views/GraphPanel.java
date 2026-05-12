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

    private final int PIXELS_PER_CYLINDER = 32;
    private final int PIXELS_PER_STEP = 40;
    private final int PADDING = 60;
    private final int AXIS_Y = 60;

    private List<Cylinder> path;
    private int initialHead;
    private int currentStep = 0;

    public GraphPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        updatePreferredSize();
    }

    public void setSimulationData(List<Cylinder> path, int initialHead) {
        this.path = path;
        this.initialHead = initialHead;
        this.currentStep = 0;
        updatePreferredSize();
        revalidate();
        repaint();
    }

    public void setStep(int step) {
        this.currentStep = step;
        repaint();
    }

    public void clear() {
        this.path = null;
        this.currentStep = 0;
        updatePreferredSize();
        revalidate();
        repaint();
    }

    private void updatePreferredSize() {
        int width = (MAX_CYLINDER * PIXELS_PER_CYLINDER) + (PADDING * 2);
        int height = AXIS_Y + PADDING;

        if (path != null) height += (path.size() * PIXELS_PER_STEP);

        setPreferredSize(new Dimension(width, height));
    }

    public Point getCurrentNodeLocation() {
        if (path == null || path.isEmpty()) return null;

        int x = getXForCylinder(path.get(currentStep).data, PADDING);
        int y = AXIS_Y + ((currentStep + 1) * PIXELS_PER_STEP);
        return new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color fgColor = UIManager.getColor("Label.foreground");
        Color borderColor = UIManager.getColor("Custom.borderColor");
        setBorder(BorderFactory.createLineBorder(borderColor, 2));

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int startX = PADDING;
        int endX = startX + (MAX_CYLINDER * PIXELS_PER_CYLINDER);

        g2d.setColor(fgColor);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine(startX, AXIS_Y, endX, AXIS_Y);

        Set<Integer> uniquePoints = new HashSet<>();
        uniquePoints.add(MIN_CYLINDER);
        uniquePoints.add(MAX_CYLINDER);

        if (path != null && !path.isEmpty()) {
            uniquePoints.add(initialHead);
            for (Cylinder c : path) {
                uniquePoints.add(c.data);
            }
        } else {
            drawHorizontalTickAndLabel(g2d, getXForCylinder(MIN_CYLINDER, startX), AXIS_Y, String.valueOf(MIN_CYLINDER), fgColor);
            drawHorizontalTickAndLabel(g2d, getXForCylinder(MAX_CYLINDER, startX), AXIS_Y, String.valueOf(MAX_CYLINDER), fgColor);
            return;
        }

        List<Integer> sortedPoints = new ArrayList<>(uniquePoints);
        Collections.sort(sortedPoints);

        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        for (int point : sortedPoints) {
            int xPos = getXForCylinder(point, startX);
            drawHorizontalTickAndLabel(g2d, xPos, AXIS_Y, String.valueOf(point), fgColor);
        }

        int prevX = getXForCylinder(initialHead, startX);
        int prevY = AXIS_Y;

        g2d.setColor(fgColor);
        g2d.fillOval(prevX - 4, prevY - 4, 8, 8);

        for (int i = 0; i <= currentStep && i < path.size(); i++) {
            Cylinder current = path.get(i);
            int currX = getXForCylinder(current.data, startX);
            int currY = prevY + PIXELS_PER_STEP;

            g2d.setColor(fgColor);
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
                else g2d.setColor(new Color(0, 102, 204));
                g2d.fillOval(currX - 4, currY - 4, 8, 8);
            }

            prevX = currX;
            prevY = currY;
        }
    }

    private int getXForCylinder(int cylinder, int startX) {
        return startX + (cylinder * PIXELS_PER_CYLINDER);
    }

    private void drawHorizontalTickAndLabel(Graphics2D g2d, int x, int y, String label, Color fgColor) {
        g2d.setColor(fgColor);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawLine(x, y - 6, x, y + 6);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(label);

        g2d.drawString(label, x - (textWidth / 2), y + 25);
    }
}