package dixu.deckard.server.event;

import dixu.deckard.client.MinionView;
import dixu.deckard.server.CardContext;

public class CardPlayedEvent implements MinionEvent {
    private final CardContext cardContext;

    public CardPlayedEvent(CardContext cardContext) {
        this.cardContext = cardContext;
    }

    @Override
    public void accept(MinionView visitor) {
        visitor.handleCardPlayed(this);
    }

    public CardContext getPlayContext() {
        return cardContext;
    }
}
