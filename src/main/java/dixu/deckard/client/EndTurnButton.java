package dixu.deckard.client;

import java.awt.*;

public class EndTurnButton {

    private Rectangle rect;

    public EndTurnButton() {
        rect = new Rectangle(Display.getWidth(0.63), Display.getHeight(0.9), Display.getWidth(0.05), Display.getHeight(0.03));
    }

    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(rect.x, rect.y +15, rect.width, rect.height);
        g.setColor(Color.DARK_GRAY);
        g.drawString("Koniec tury",rect.x+10,rect.y +30);

    }
}
