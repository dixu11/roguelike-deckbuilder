package dixu.deckard.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

import static dixu.deckard.client.GuiParams.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

//manages game frame and canvas
public final class Display {

    private JFrame frame;
    private Canvas canvas;

    private final String title;
    private Dimension size;

    public Display(String title) {
        this.title = title;
        size = new Dimension(WIDTH, HEIGHT);

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

    public void addListener(MouseListener listener) {
        canvas.addMouseListener(listener);
    }

    public void showGame() {
        frame.setVisible(true);
    }
}
