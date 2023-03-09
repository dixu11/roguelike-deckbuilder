package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.event.BusManager;

public class EffectDecorator<T extends CardEffect> implements CardEffect {
     BusManager bus = BusManager.instance();
    T decorated;

    public EffectDecorator(T decorated) {
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
