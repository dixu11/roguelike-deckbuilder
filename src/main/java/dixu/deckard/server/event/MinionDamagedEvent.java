package dixu.deckard.server.event;

import dixu.deckard.client.EventCounterView;
import dixu.deckard.server.Minion;

public class MinionDamagedEvent implements EventCounterEvent {
    private int newValue;
    private int oldValue;
    private Minion minion;

    public MinionDamagedEvent(int newValue, int oldValue, Minion minion) {
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.minion = minion;
    }

    @Override
    public void accept(EventCounterView visitor) {
        visitor.handleMinionDamaged(this);
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
