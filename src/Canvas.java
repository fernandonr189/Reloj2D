import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JFrame {

    private int height;
    private int width;

    public Canvas(int width, int height) {
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setTitle("LineaDDA");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new ClockFrame(
                height > width
                        ? width - 50
                        : height - 50,
                buffer,
                width,
                height));
        setVisible(true);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
