package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void add(Card card) {
        hand.add(card);
    }

    public void addAll(List<Card> cards) {
        hand.addAll(cards);
    }

    public Card playCard(int index) {
        return hand.remove(index);
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }
}
