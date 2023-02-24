package dixu.deckard.client;

import dixu.deckard.server.GameController;

import java.awt.*;

public class EndTurnButtonView implements Clickable {

    private Rectangle rect;

    public EndTurnButtonView() {
        rect = new Rectangle(Display.getWidth(0.63), Display.getHeight(0.9)+15, Display.getWidth(0.05), Display.getHeight(0.03));
    }

    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(rect.x, rect.y , rect.width, rect.height);
        g.setColor(Color.DARK_GRAY);
        g.drawString("Koniec tury",rect.x+10,rect.y +15);
    }



    @Override
    public void onClick(GameController controller) {
        controller.endTurn();
        System.out.println("Click: end turn");
    }

    @Override
    public boolean isClicked(int x, int y) {
        return rect.intersects(x, y, 1, 1);
    }
}
