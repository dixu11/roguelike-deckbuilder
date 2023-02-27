package dixu.deckard.server.event;

import dixu.deckard.server.Game;

public class TurnEndedEvent implements GameEvent {
    @Override
    public void accept(Game visitor) {
        visitor.handleTurnEnd(this);
    }
}
