package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand = new ArrayList<>();
    private final List<Character> team;

    public Player(List<Character> team) {
        this.team = team;
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

    public List<Character> getTeam() {
        return team;
    }
}
