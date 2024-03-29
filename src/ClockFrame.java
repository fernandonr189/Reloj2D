import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.TimeZone;

import static java.lang.Math.floor;

public class ClockFrame extends JPanel implements Runnable{

    private final int diameter;
    private final int innerFrameDiameter;
    private final int innermostFrameDiameter;
    private final int height;
    private final int width;

    private final int xc;
    private final int yc;

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
        xc = (width / 2);
        yc = (height / 2);
        Thread thread = new Thread(this);
        thread.start();
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
        update(graphics2D, g);
    }

    public void update(Graphics2D graphics2D, Graphics g) {
        drawClockHands(graphics2D);
        pixelMethods.antiAliasedCircle(width / 2, height / 2, 150, Color.black, bufferedImage);
        g.drawImage(bufferedImage, 0, 0, this);
    }

    private void drawClockHands(Graphics2D g){
        Calendar cal = Calendar.getInstance();
        int seconds = cal.get(Calendar.SECOND);
        int milliseconds = cal.get(Calendar.MILLISECOND);
        int minutes = cal.get(Calendar.MINUTE);
        int hours = cal.get(Calendar.HOUR);
        int seconds_length = (int) floor(innermostFrameDiameter / 2.5);
        int minutes_length = (int) floor(innermostFrameDiameter / 3.0);
        int hours_length = (int) floor(innermostFrameDiameter / 3.5);
        double secondsAngle = (seconds + (milliseconds / 1000.0)) * (12 * Math.PI / 360) - Math.PI / 2;
        double minutesAngle = (minutes + (seconds / 60.0)) * (12 * Math.PI / 360) - Math.PI / 2;
        double hoursAngle = (hours + (minutes / 60.0)) * (60 * Math.PI / 360) - Math.PI / 2;
        g.setColor(new Color(154, 43, 43));
        g.drawLine(
                xc,
                yc,
                (int) (xc + (seconds_length * Math.cos(secondsAngle))),
                (int) (yc + (seconds_length * Math.sin(secondsAngle))));
        g.setColor(Color.BLACK);
        g.drawLine(
                xc,
                yc,
                (int) (xc + (minutes_length * Math.cos(minutesAngle))),
                (int) (yc + (minutes_length * Math.sin(minutesAngle))));
        g.drawLine(
                xc,
                yc,
                (int) (xc + (hours_length * Math.cos(hoursAngle))),
                (int) (yc + (hours_length * Math.sin(hoursAngle))));
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
        g.drawOval(
                (width / 2) - (innermostFrameDiameter / 2),
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
            Font font = new Font("Times New Roman", Font.ITALIC, 15);
            g.setFont(font);
            g.drawString(romanNumbers[i + 2],
                    (float) (xc + (s * Math.cos(angle)) - 5),
                    (float) (yc + (s * Math.sin(angle))) + 5);
        }
    }

    @Override
    public void run() {
        boolean noException = true;
        while(noException) {
            try {
                repaint();
                Thread.sleep(16);
            }
            catch (InterruptedException e) {
                noException = false;
            }
        }
    }
}