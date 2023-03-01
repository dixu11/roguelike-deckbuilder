package dixu.deckard.server;


import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.server.GameParams.INITIAL_ENERGY;

/**
 * {@link Leader} is player entity or computer that is controlling one side of a fight.
 * {@link Leader} has  {@link Team} of {@link Minion}s fighting for him but not directly controlled.
 * {@link Leader} can decide to spend his {@link Leader#energy} to cast {@link Special} actions to mess
 * with {@link Minion}s {@link Card}s.
* */

public class Leader {
    private Team team;
    private List<Card> hand = new ArrayList<>();
    private int energy = INITIAL_ENERGY;

    public Leader(Team team) {
        this.team = team;
    }

    public void addCards(List<Card> cards) {
        hand.addAll(cards);
    }

    public Team getTeam() {
        return team;
    }

    public int getEnergy() {
        return energy;
    }

    public List<Card> getHand() {
        return hand;
    }
}
