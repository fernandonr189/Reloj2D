import javax.swing.*;
import java.awt.*;

public class ClockFrame extends JPanel {

    private final int diameter;
    private final int innerFrameDiameter;
    private final int innermostFrameDiameter;
    private int height;
    private int width;
    public ClockFrame(int _radius) {
        super(true);
        this.diameter = _radius;
        this.innerFrameDiameter = (int) (diameter * 0.95);
        this.innermostFrameDiameter = (int) (diameter * 0.90);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.height = this.getHeight();
        this.width = this.getWidth();
        setBackground(Color.black);
        drawClockFrame(graphics2D);
    }

    private void drawClockFrame(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((width / 2) - (diameter / 2), (height / 2) - (diameter / 2), diameter, diameter);
        g.setColor(Color.BLACK);
        g.drawOval(
                (width / 2) - (innerFrameDiameter / 2),
                (height / 2) - (innerFrameDiameter / 2),
                innerFrameDiameter,
                innerFrameDiameter);
        g.drawOval((
                (width / 2)) - (innermostFrameDiameter / 2),
                (height / 2) - (innermostFrameDiameter / 2),
                innermostFrameDiameter,
                innermostFrameDiameter);
        drawSecondsDivisions(g);
    }

    private void drawSecondsDivisions(Graphics2D g) {
        for(int i = 0; i < 60; i++) {
            int xc = (width / 2);
            int yc = (height / 2);
            int s_min = innermostFrameDiameter / 2;
            int s_max = innerFrameDiameter / 2;
            double angle = i * (6 * 2 * Math.PI / 360);
            int x1 = (int) (xc + (s_max * Math.cos(angle)));
            int y1 = (int) (yc + (s_max * Math.sin(angle)));
            int x2 = (int) (xc + (s_min * Math.cos(angle)));
            int y2 = (int) (yc + (s_min * Math.sin(angle)));
            g.drawLine(x1, y1, x2, y2);
        }
    }
}
