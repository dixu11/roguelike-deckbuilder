package dixu.deckard.client;

import java.awt.*;

public class MainView {
    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);

        g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
    }
}
