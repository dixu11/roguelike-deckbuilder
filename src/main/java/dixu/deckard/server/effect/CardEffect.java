package dixu.deckard.server.effect;

import dixu.deckard.server.CardContext;

public interface CardEffect {
   void execute(CardContext context);
}
