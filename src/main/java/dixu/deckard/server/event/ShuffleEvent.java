package dixu.deckard.server.event;

import dixu.deckard.client.MinionView;
import dixu.deckard.server.Minion;

public class ShuffleEvent implements MinionEvent {
    private final Minion minion;

    public ShuffleEvent(Minion minion) {
        this.minion = minion;
    }

    @Override
    public void accept(MinionView visitor) {
        visitor.handleShuffle(this);
    }

    public Minion getMinion() {
        return minion;
    }
}
