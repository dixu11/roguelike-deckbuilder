package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardCategory;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventHandler;
import dixu.deckard.server.event.ActionEventType;

import java.util.List;

public class DeckShieldEffect extends BlockEffectDecorator implements ActionEventHandler {
    public DeckShieldEffect(BlockEffect decorated) {
        super(decorated);
        bus.register(this, ActionEventType.MINION_HAND_CHANGED);
        //todo refactor to be better targeted by actions. like on minion owner change or on deck change
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getMinion() == getCard().getOwner()) {
            calculateValue();
        }
    }

    @Override
    public void execute(CardContext context) {
        calculateValue();
        super.execute(context);
    }

    private void calculateValue() {
        Minion owner = getCard().getOwner();
        if (owner == null) {
            setValue(0);
            return;
        }
       List<Card> cards = owner.getAllCards();
        int value = (int) cards.stream()
                .filter(card -> card.getCategory() == CardCategory.BLOCK)
                .filter(card -> !card.equals(getCard()))
                .count();
        setValue(value);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " = other block cards in deck";
    }
}
