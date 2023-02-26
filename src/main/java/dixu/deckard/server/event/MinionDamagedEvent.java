package dixu.deckard.server.event;

import dixu.deckard.server.Minion;

public class MinionDamagedEvent implements Event {
    private int newValue;
    private int oldValue;
    private Minion minion;

    public MinionDamagedEvent(int newValue, int oldValue, Minion minion) {
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.minion = minion;
    }

    public int getNewValue() {
        return newValue;
    }

    public int getOldValue() {
        return oldValue;
    }

    public Minion getMinion() {
        return minion;
    }
}
