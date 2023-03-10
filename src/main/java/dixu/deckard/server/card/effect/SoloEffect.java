package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardCategory;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.event.*;

import java.util.List;

public class SoloEffect extends AttackEffectDecorator implements ActionEventHandler, CoreEventHandler {
    private static final int ATTACK_NEGATIVE_MODIFIER = -2;
    private boolean active = true;

    public SoloEffect(AttackEffect decorated) {
        super(decorated);
        bus.register(this, ActionEventType.MINION_HAND_CHANGED);
        bus.register(this, CoreEventType.SETUP_PHASE_STARTED);
        bus.register(this, CoreEventType.MINION_PHASE_STARTED);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " Has +2⚔️ if it is the only attack team plays"; //todo refactor symbols
    }

    @Override
    public void handle(ActionEvent event) {
        if (!active) {
            return;
        }

        resetValue();
        if (getCard().getOwner() == null) {
            return;
        }

        List<Card> hands = getCard().getOwner().getTeam().getMinionsHands();
        for (Card card : hands) {
            if (card.equals(getCard())) {
                continue;
            }
            if (card.getCategory() == CardCategory.ATTACK) {
                setValue(getInitialValue() + ATTACK_NEGATIVE_MODIFIER);
                break;
            }
        }
    }

    private void resetValue() {
        setValue(getInitialValue());
    }

    @Override
    public void execute(CardContext context) {
        super.execute(context);
    }

    @Override
    public void handle(CoreEvent event) {
        if (event.getType() == CoreEventType.MINION_PHASE_STARTED) {
            active = false;
        }
        if (event.getType() == CoreEventType.SETUP_PHASE_STARTED) {
            active = true;
            setValue(getInitialValue());
        }
    }
}
