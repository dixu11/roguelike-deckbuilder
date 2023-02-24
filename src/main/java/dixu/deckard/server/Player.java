package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand = new ArrayList<>();

    public Player() {
        for (int i = 0; i < 5; i++) {
            hand.add(new Card());
        }
    }


    public Card playCard(int index) {
        return hand.remove(index);
    }

    public List<Card> getHand() {
        return hand;
    }
}
