package dixu.deckard.server.effect;

import dixu.deckard.server.Card;
import dixu.deckard.server.CardContext;

public interface CardEffect {
   void execute(CardContext context);

   String getDescription();

    int getAttack();

    int getBlock();

    void setValue(int value);

    int getInitialValue();

    void modifyValueBy(int value);

    Card getCard();
}
