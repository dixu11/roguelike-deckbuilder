package dixu.deckard.server.effect;

import dixu.deckard.server.event.*;

public class ComboAttackEffect extends AttackEffectDecorator implements ActionEventHandler {

    public ComboAttackEffect(AttackEffect decorated) {
        super(decorated);
        bus.register(this,ActionEventName.MINION_CARD_PLAYED);
        //bus.register(this, CoreEventName.TURN_ENDED); //todo so this is illegal...
    }

    @Override
    public void handle(ActionEvent event) {

    }
}
