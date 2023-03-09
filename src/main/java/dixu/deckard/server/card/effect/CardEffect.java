package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;

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
