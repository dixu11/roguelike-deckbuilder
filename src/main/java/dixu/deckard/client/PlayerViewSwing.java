package dixu.deckard.client;

import dixu.deckard.server.Card;
import dixu.deckard.server.GameController;
import dixu.deckard.server.PlayerView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerViewSwing implements PlayerView {
   private final String playerName;
   private final List<Card> hand;
   private GameController gameController;

    public PlayerViewSwing(String playerName) {
        this.playerName = playerName;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void setController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void addAll(List<Card> cards) {
        hand.addAll(cards);
    }

    public void render(Graphics g) {

    }

    public int chooseCard() {
        return -1;
    }

}
