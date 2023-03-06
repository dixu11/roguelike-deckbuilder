package dixu.deckard.server.effect;

import dixu.deckard.server.Card;

public interface AttackEffect extends CardEffect{
    void setPiercing(boolean piercing);

    void setValue(int value);

    int getInitialValue();

    void modifyValueBy(int value);

    Card getCard();
}
