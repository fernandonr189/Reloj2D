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
        antiAliasedLine(500, 100, 100, 300, Color.blue);
        antiAliasedLine(500, 100, 100, 500, Color.blue);
        getGraphics().drawImage(buffer,0,0,panel);
    }
    public void antiAliasedLine(double x1, double y1, double x2, double y2, Color a) {
        long start = System.nanoTime();
        double dx = x2 - x1;
        double dy = y2 - y1;
        double m = dy / dx;
        double b = y1 - (m * x1);
        if(abs(dy) == abs(dx)) {
            if(x1 > x2) {
                double tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            for(int i = (int) floor(x1); i <= (int) floor(x2); i++) {
                double midX = i + 0.5;
                double midY = (m * midX) + b;
                double tempY = floor(midY);
                double q = midY - tempY;
                pixel(i, (int) tempY, a, 255);
                pixel(i, (int) tempY + 1, a, (int) ((0.25) * 255));
                pixel(i, (int) tempY - 1, a, (int) ((0.25) * 255));
            }
        }
        else {
            if(abs(dy) >= abs(dx)) {
                if(y1 > y2) {
                    double tmp = y1;
                    y1 = y2;
                    y2 = tmp;
                }
                for(int i = (int) floor(y1); i <= (int) floor(y2); i++) {
                    double midY = i + 0.5;
                    double midX = (midY - b) / m;
                    double tempX = floor(midX);
                    double q = midX - tempX;
                    if(q > 0.5) {
                        double percent = 1 - (q - 0.5);
                        pixel((int) tempX, i, a, (int) (percent * 255));
                        pixel((int) tempX + 1, i, a, (int) ((1 - percent) * 255));
                    }
                    else {
                        double percent = 1 - (0.5 - q);
                        pixel((int) tempX, i, a, (int) (percent * 255));
                        pixel((int) tempX - 1, i, a, (int) ((1 - percent) * 255));
                    }
                }
            }
            else {
                if(x1 > x2) {
                    double tmp = x1;
                    x1 = x2;
                    x2 = tmp;
                }
                for(int i = (int) floor(x1); i <= (int) floor(x2); i++) {
                    double midX = i + 0.5;
                    double midY = (m * midX) + b;
                    double tempY = floor(midY);
                    double q = midY - tempY;
                    if(q > 0.5) {
                        double percent = 1 - (q - 0.5);
                        pixel(i, (int) tempY, a, (int) (percent * 255));
                        pixel(i, (int) tempY + 1, a, (int) ((1 - percent) * 255));
                    }
                    else {
                        double percent = 1 - (0.5 - q);
                        pixel(i, (int) tempY, a, (int) (percent * 255));
                        pixel(i, (int) tempY - 1, a, (int) ((1 - percent) * 255));
                    }
                }
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

    private void pixel(int x, int y, Color a, int alpha) {
        buffer.setRGB(x, y, new Color(a.getRed(), a.getGreen(), a.getBlue(), alpha).getRGB());
    }
}