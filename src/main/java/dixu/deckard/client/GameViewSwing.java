package dixu.deckard.client;

import dixu.deckard.server.GameView;

import java.awt.*;

public class GameViewSwing implements GameView {
    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);

        g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
    }
}
