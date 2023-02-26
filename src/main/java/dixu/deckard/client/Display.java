package dixu.deckard.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

//manages game frame and canvas
public final class Display {

    public static int LOCATION_ON_SCREEN_X = 2200;
    public static int LOCATION_ON_SCREEN_Y = 50;
    private static final int DEFAULT_WIDTH = 2100;
    private static final int DEFAULT_HEIGHT = 800;

    private volatile static int width;
    private volatile static int height;

    private JFrame frame;
    private Canvas canvas;

    private String title;
    private Dimension size;

    public Display(String title) {
        this.title = title;
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        size = new Dimension(width, height);

        createAndSetupFrame();
        createAndSetupCanvas();
        frame.pack();
    }

    private void createAndSetupFrame() {
        frame = new JFrame(title);

        frame.setSize(size);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocation(LOCATION_ON_SCREEN_X, LOCATION_ON_SCREEN_Y);
//        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    private void createAndSetupCanvas() {
        canvas = new Canvas();

        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setMinimumSize(size);
        frame.add(canvas);

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public void addListener(MouseListener listener) {
        canvas.addMouseListener(listener);
    }

    public void showGame() {
        frame.setVisible(true);
    }

    public static int getWidth(double percent) {
        return (int) (getWidth() * percent);
    }

    public static int getHeight(double percent) {
        return (int) (getHeight() * percent);
    }
}
