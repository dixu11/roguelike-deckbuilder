package dixu.deckard.server.effect;

import dixu.deckard.server.Card;
import dixu.deckard.server.CardContext;
import dixu.deckard.server.event.BusManager;

public class AttackEffectDecorator implements AttackEffect {
     BusManager bus = BusManager.instance();
    AttackEffect decorated;

    public AttackEffectDecorator(AttackEffect decorated) {
        this.decorated = decorated;
    }

    @Override
    public void execute(CardContext context) {
        decorated.execute(context);
    }

    @Override
    public String getDescription() {
        return decorated.getDescription();
    }

    @Override
    public int getAttack() {
        return decorated.getAttack();
    }

    @Override
    public int getBlock() {
        return decorated.getBlock();
    }

    @Override
    public void setPiercing(boolean piercing) {
        decorated.setPiercing(piercing);
    }

    @Override
    public void setValue(int value) {
        decorated.setValue(value);
    }

    @Override
    public int getInitialValue() {
        return decorated.getInitialValue();
    }

    @Override
    public void modifyValueBy(int value) {
        decorated.modifyValueBy(value);
    }

    @Override
    public Card getCard() {
        return decorated.getCard();
    }
}
