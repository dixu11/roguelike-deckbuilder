package dixu.deckard.server.event;

import dixu.deckard.server.Minion;

public class ShuffleEvent implements Event{
    private Minion minion;

    public ShuffleEvent(Minion minion) {
        this.minion = minion;
    }

    public Minion getMinion() {
        return minion;
    }
}
