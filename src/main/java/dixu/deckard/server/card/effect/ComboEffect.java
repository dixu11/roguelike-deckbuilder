package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.CardCategory;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.event.*;

public class ComboEffect extends AttackEffectDecorator implements ActionEventHandler, CoreEventHandler {

    public ComboEffect(AttackEffect decorated) {
        super(decorated);
        bus.register(this, ActionEventType.MINION_CARD_PLAYED);
        bus.register(this, CoreEventType.TURN_STARTED);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " gets +1⚔️ after every attack played this turn";
    }

    @Override
    public void handle(ActionEvent event) {
        Minion owner = getCard().getOwner();
        if (owner == null || !owner.getTeam().equals(event.getOwnTeam())) {
            return;
        }
        if (event.getCard().getCategory() == CardCategory.ATTACK) {
            decorated.modifyValueBy(1);
        }
    }

    @Override
    public void handle(CoreEvent event) {
        decorated.setValue(decorated.getInitialValue());
    }
}
