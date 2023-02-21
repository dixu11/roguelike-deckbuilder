package dixu.deckard.client;

import dixu.deckard.server.Card;
import dixu.deckard.server.GameController;
import dixu.deckard.server.PlayerType;
import dixu.deckard.server.PlayerView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerViewSwing implements PlayerView {

    private static final int Y_COMPUTER_OFFSET = 50;
    private static final int Y_PLAYER_OFFSET = 600;
    private static final int X_PLAYER_MARGIN = 800;

    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 160;
    private static final int CARD_PADDING = 20;
    private final PlayerType playerType;
   private final List<Card> hand;
   private GameController gameController;

    public PlayerViewSwing(PlayerType playerType) {
        this.playerType = playerType;
        hand = new ArrayList<>();
    }


    public void setController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void addAll(List<Card> cards) {
        hand.addAll(cards);
    }

    public void render(Graphics g) {
        int yOffset = playerType == PlayerType.COMPUTER ? Y_COMPUTER_OFFSET : Y_PLAYER_OFFSET;
        for (int i = 0; i < hand.size(); i++) {
            g.setColor(Color.GRAY);
            int x = X_PLAYER_MARGIN + i * CARD_WIDTH + CARD_PADDING * i;
            int y = yOffset;
            g.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);
            Card card = hand.get(i);
            g.setColor(Color.DARK_GRAY);
            g.drawString(card.getName(),x+CARD_WIDTH/4,y+CARD_HEIGHT/6);
        }
    }

    public int chooseCard() {
        return -1;
    }

}
