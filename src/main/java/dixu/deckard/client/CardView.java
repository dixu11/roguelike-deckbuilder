package dixu.deckard.client;

import java.awt.*;

public class CardView {

    public static final int CARD_WIDTH = 80;
    public static final int CARD_HEIGHT = 160;
    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0,0,CARD_WIDTH,CARD_HEIGHT);
    }
}
