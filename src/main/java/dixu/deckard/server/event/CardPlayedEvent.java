package dixu.deckard.server.event;

import dixu.deckard.server.Card;
import dixu.deckard.server.Minion;
import dixu.deckard.server.PlayContext;
import dixu.deckard.server.Team;

public class CardPlayedEvent implements Event {
    private PlayContext playContext;

    public CardPlayedEvent(PlayContext playContext) {
        this.playContext = playContext;
    }

    public PlayContext getPlayContext() {
        return playContext;
    }
}
