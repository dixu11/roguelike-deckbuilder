package dixu.deckard.server;

public class ShuffleEvent implements Event{
    private Minion minion;

    public ShuffleEvent(Minion minion) {
        this.minion = minion;
    }

    public Minion getMinion() {
        return minion;
    }
}
