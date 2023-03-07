package dixu.deckard.server.effect;

import dixu.deckard.server.Card;

public abstract class BasicEffect implements CardEffect {
    final int initialValue;
    int value;
    Card card;

    public BasicEffect(int value, Card card) {
        this.value = value;
        initialValue = value;
        this.card = card;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getInitialValue() {
        return initialValue;
    }

    @Override
    public void modifyValueBy(int value) {
        this.value += value;
    }

    public Card getCard() {
        return card;
    }
}
