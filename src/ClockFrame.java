import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.floor;

public class ClockFrame extends JPanel {

    private final int diameter;
    private final int innerFrameDiameter;
    private final int innermostFrameDiameter;
    private final int height;
    private final int width;

    private final String[] romanNumbers = {"I","II","III","IV","V","VI","VII","VIII","IX","X","XI","XII"};

    private final BufferedImage bufferedImage;
    public ClockFrame(BufferedImage bufferedImage, int width, int height) {
        this.diameter = height > width
                ? width - 50
                : height - 50;
        this.innerFrameDiameter = (int) floor((diameter * 0.95));
        this.innermostFrameDiameter = (int) floor((diameter * 0.90));
        this.bufferedImage = bufferedImage;
        this.height = height;
        this.width = width;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = this.bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        setBackground(Color.black);
        drawClockFrame(graphics2D);
        graphics2D.setClip(0, 0, width, height);
        g.drawImage(bufferedImage, 0, 0, this);
    }

    private void drawClockFrame(Graphics2D g) {
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((width / 2) - (diameter / 2), (height / 2) - (diameter / 2), diameter, diameter);
        g.setColor(Color.BLACK);
        g.drawOval(
                (width / 2) - (innerFrameDiameter / 2),
                (height / 2) - (innerFrameDiameter / 2),
                innerFrameDiameter,
                innerFrameDiameter);
        g.setStroke(new BasicStroke(2));
        g.drawOval((
                (width / 2)) - (innermostFrameDiameter / 2),
                (height / 2) - (innermostFrameDiameter / 2),
                innermostFrameDiameter,
                innermostFrameDiameter);
        drawSecondsDivisions(g);
        drawHoursDivision(g);
        hourNumbers(g);
    }

    private void drawSecondsDivisions(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        int xc = (width / 2);
        int yc = (height / 2);
        int s_min = innermostFrameDiameter / 2;
        int s_max = innerFrameDiameter / 2;
        for(int i = 0; i < 60; i++) {
            double angle = i * (12 * Math.PI / 360);
            g.drawLine(
                    (int) (xc + (s_max * Math.cos(angle))),
                    (int) (yc + (s_max * Math.sin(angle))),
                    (int) (xc + (s_min * Math.cos(angle))),
                    (int) (yc + (s_min * Math.sin(angle))));
        }
    }
    private void drawHoursDivision(Graphics2D g) {
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.BLACK);
        int xc = (width / 2);
        int yc = (height / 2);
        int s_min = innermostFrameDiameter / 2;
        int s_max = innerFrameDiameter / 2;
        for(int i = 0; i < 12; i++) {
            double angle = i * (60 * Math.PI / 360);
            g.drawLine(
                    (int) (xc + (s_max * Math.cos(angle))),
                    (int) (yc + (s_max * Math.sin(angle))),
                    (int) (xc + (s_min * Math.cos(angle))),
                    (int) (yc + (s_min * Math.sin(angle))));
        }
    }

    private void hourNumbers(Graphics2D g) {
        g.setColor(Color.BLACK);
        int xc = (width / 2);
        int yc = (height / 2);
        int s = (int) (innermostFrameDiameter / 2.1);
        for(int i = -2; i < 10; i++) {
            double angle = i * (60 * Math.PI / 360);
            g.drawString(romanNumbers[i + 2],
                    (float) (xc + (s * Math.cos(angle)) - 5),
                    (float) (yc + (s * Math.sin(angle))) + 5);
        }
    }
}