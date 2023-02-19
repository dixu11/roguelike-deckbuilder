package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final PlayerType type;
    private final List<Card> hand = new ArrayList<>();

    public Player(PlayerType type) {
        this.type = type;
        for (int i = 0; i < 5; i++) {
            hand.add(new Card());
        }
    }


    public Card playCard(int index) {
        return hand.remove(index);
    }

    public PlayerType getType() {
        return type;
    }

    public List<Card> getHand() {
        return hand;
    }
}
