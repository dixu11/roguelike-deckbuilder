package dixu.deckard.client;

import dixu.deckard.server.*;

import java.awt.*;
import java.util.List;

public class PlayerViewSwing implements PlayerView {

    private static final int Y_PLAYER_OFFSET = 600;
    private static final int X_PLAYER_MARGIN = 800;

    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 160;
    private static final int CARD_PADDING = 20;
   private Player player;
   private GameController gameController;

    public PlayerViewSwing(Player player) {
        this.player = player;
    }


    public void setController(GameController gameController) {
        this.gameController = gameController;
    }

    public void render(Graphics g) {
        for (int i = 0; i < player.getHand().size(); i++) {
            g.setColor(Color.GRAY);
            int x = X_PLAYER_MARGIN + i * CARD_WIDTH + CARD_PADDING * i;
            g.fillRect(x, Y_PLAYER_OFFSET, CARD_WIDTH, CARD_HEIGHT);
            Card card = player.getHand().get(i);
            g.setColor(Color.DARK_GRAY);
            g.drawString(card.getName(),x+CARD_WIDTH/4,Y_PLAYER_OFFSET+CARD_HEIGHT/6);
        }
    }
    public int chooseCard() {
        return -1;
    }

}
