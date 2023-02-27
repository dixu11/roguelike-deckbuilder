package dixu.deckard.server.event;

import dixu.deckard.server.Game;

public interface GameEvent extends Event {
    void accept(Game visitor);
}
