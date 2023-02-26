package dixu.deckard.server.event;

import dixu.deckard.server.CardContext;

public class CardPlayedEvent implements Event {
    private final CardContext cardContext;

    public CardPlayedEvent(CardContext cardContext) {
        this.cardContext = cardContext;
    }

    public CardContext getPlayContext() {
        return cardContext;
    }
}
