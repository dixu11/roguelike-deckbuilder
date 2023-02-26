package dixu.deckard.server.event;

import dixu.deckard.server.Card;
import dixu.deckard.server.CardContext;
import dixu.deckard.server.Minion;
import dixu.deckard.server.Team;

public class DrawCardEvent implements Event {
    private CardContext cardContext;

    public DrawCardEvent(CardContext cardContext) {
        this.cardContext = cardContext;
    }

    public CardContext getPlayContext() {
        return cardContext;
    }
}
