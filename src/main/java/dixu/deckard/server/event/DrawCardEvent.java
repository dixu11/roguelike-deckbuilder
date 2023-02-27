package dixu.deckard.server.event;

import dixu.deckard.client.MinionView;
import dixu.deckard.server.CardContext;

public class DrawCardEvent implements MinionEvent {
    private CardContext cardContext;

    public DrawCardEvent(CardContext cardContext) {
        this.cardContext = cardContext;
    }

    @Override
    public void accept(MinionView visitor) {
        visitor.handleCardDraw(this);
    }

    public CardContext getPlayContext() {
        return cardContext;
    }
}
