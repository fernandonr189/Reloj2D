import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class Canvas extends JFrame {

    private final JPanel panel;
    private final BufferedImage buffer;


    public Canvas() {
        setTitle("LineaDDA");
        setSize(600, 600);
        panel = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setSize(600, 600);
        setLocationRelativeTo(null);
        buffer = new BufferedImage(panel.getWidth(),panel.getHeight(),BufferedImage.TYPE_INT_ARGB);
        add(panel);
        setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        lineaBresenham(100, 100, 500, 500, Color.red);
        antiAliasedLine(100, 500, 500, 300, Color.blue);
        getGraphics().drawImage(buffer,0,0,panel);
    }
    public void antiAliasedLine(double x1, double y1, double x2, double y2, Color a) {
        long start = System.nanoTime();
        double dx = x2 - x1;
        double dy = y2 - y1;
        double m = dy / dx;
        double b = y1 - (m * x1);
        if(dy >= dx) {
            for(int i = (int) floor(y1); i <= (int) floor(y2); i++) {
                int tempX = (int) floor((i - b) / m);
                pixel(tempX, i, Color.blue, 255);
            }
            for(int i = (int) floor(y1); i >= (int) floor(y2); i--) {
                int tempX = (int) floor((i - b) / m);
                pixel(tempX, i, Color.blue, 255);
            }
        }
        else {
            for(int i = (int) floor(x1); i <= (int) floor(x2); i++) {
                //pixel(i, tempY, Color.blue, 255);
                double midX = i + 0.5;
                double midY = (m * midX) + b;
                double q = midY - floor(midY);
                if(q > 0.5) {
                    double percent = abs(0.5 - q) * 2;
                }
                else {

                }
            }
            for(int i = (int) floor(x1); i >= (int) floor(x2); i--) {
                int tempY = (int) floor((m * i) + b);
                pixel(i, tempY, Color.blue, 255);
            }
        }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("AntiAliased: " + timeElapsed + " nanosegundos");
    }

    public void lineaBresenham(int x1, int y1, int x2, int y2, Color a){
        long start = System.nanoTime();
        int dy = y2 - y1;
        int dx = x2 - x1;
        int x = x1;
        int y = y1;
        int p;
        int incX = 1;
        int incY = 1;
        double m = (double) dy / dx;
        System.out.println("Pendiente: " + m);

        if (dy < 0){
            dy = -dy;
            incY = -1;
        }
        if (dx < 0){
            dx = -dx;
            incX = -1;
        }

        if (dx > dy){
            p = 2 * dy - dx;
            for (int i = 0; i <= dx; i++){
                if (p >= 0){
                    y += incY;
                    p += 2 * (dy - dx);
                }
                else {
                    p += 2 * dy;
                }
                x += incX;
                pixel(x, y, a, 255);
            }
        }
        else {
            p = 2 * dx - dy;
            for (int i = 0; i <= dy; i++){
                if (p >= 0){
                    x += incX;
                    p += 2 * (dx - dy);
                }
                else {
                    p += 2 * dx;
                }
                y += incY;
                pixel(x, y, a, 255);
            }
        }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("Bresenham: " + timeElapsed + " nanosegundos");
    }

    public void lineaDDA(int x1, int y1, int x2, int y2, Color a){
        int dy = y2 - y1;
        int dx = x2 - x1;
        double m = (double) dy / dx;
        System.out.println("Pendiente: " + m);

        if (x1 > x2 || y1 > y2) {
            int tempX = x1;
            int tempY = y1;
            x1 = x2;
            y1 = y2;
            x2 = tempX;
            y2 = tempY;
        }

        if (abs(m) <= 1){
            double y = y1;
            for(int x = x1; x <= x2; x++){
                pixel(x, (int) y, a, 255);
                y += m;
            }
        }
        else {
            double x = x1;
            for(int y = y1; y <= y2; y++){
                pixel((int) x, y, a, 255);
                x += (1/m);
            }
        }
    }

    int ipart(double x) {
        return (int) x;
    }

    double fpart(double x) {
        return x - floor(x);
    }

    double rfpart(double x) {
        return 1.0 - fpart(x);
    }

    void plot(Graphics2D g, double x, double y, double c) {
        g.setColor(new Color(0f, 0f, 0f, (float)c));
        g.fillOval((int) x, (int) y, 2, 2);
    }

    void drawLine(Graphics2D g, double x0, double y0, double x1, double y1) {

        boolean steep = abs(y1 - y0) > abs(x1 - x0);
        if (steep)
            drawLine(g, y0, x0, y1, x1);

        if (x0 > x1)
            drawLine(g, x1, y1, x0, y0);

        double dx = x1 - x0;
        double dy = y1 - y0;
        double gradient = dy / dx;

        // handle first endpoint
        double xend = round(x0);
        double yend = y0 + gradient * (xend - x0);
        double xgap = rfpart(x0 + 0.5);
        double xpxl1 = xend; // this will be used in the main loop
        double ypxl1 = ipart(yend);

        if (steep) {
            plot(g, ypxl1, xpxl1, rfpart(yend) * xgap);
            plot(g, ypxl1 + 1, xpxl1, fpart(yend) * xgap);
        } else {
            plot(g, xpxl1, ypxl1, rfpart(yend) * xgap);
            plot(g, xpxl1, ypxl1 + 1, fpart(yend) * xgap);
        }

        // first y-intersection for the main loop
        double intery = yend + gradient;

        // handle second endpoint
        xend = round(x1);
        yend = y1 + gradient * (xend - x1);
        xgap = fpart(x1 + 0.5);
        double xpxl2 = xend; // this will be used in the main loop
        double ypxl2 = ipart(yend);

        if (steep) {
            plot(g, ypxl2, xpxl2, rfpart(yend) * xgap);
            plot(g, ypxl2 + 1, xpxl2, fpart(yend) * xgap);
        } else {
            plot(g, xpxl2, ypxl2, rfpart(yend) * xgap);
            plot(g, xpxl2, ypxl2 + 1, fpart(yend) * xgap);
        }

        // main loop
        for (double x = xpxl1 + 1; x <= xpxl2 - 1; x++) {
            if (steep) {
                plot(g, ipart(intery), x, rfpart(intery));
                plot(g, ipart(intery) + 1, x, fpart(intery));
            } else {
                plot(g, x, ipart(intery), rfpart(intery));
                plot(g, x, ipart(intery) + 1, fpart(intery));
            }
            intery = intery + gradient;
        }
    }

    private void pixel(int x, int y, Color a, int alpha) {
        buffer.setRGB(x, y, new Color(a.getRed(), a.getGreen(), a.getBlue(), alpha).getRGB());
    }
}