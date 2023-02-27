package dixu.deckard.server.event;

import dixu.deckard.server.Game;

public class TurnStartedEvent implements GameEvent {
    @Override
    public void accept(Game visitor) {
        visitor.handleTurnStart(this);
    }
}
