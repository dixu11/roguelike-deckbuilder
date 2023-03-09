package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;

public class BasicBlockEffect extends BasicEffect implements BlockEffect {

    public BasicBlockEffect(int value, Card card) {
        super(value,card);
    }

    @Override
    public void execute(CardContext context) {
        context.getOwnTeam().addBlock(value);
    }

    @Override
    public String getDescription() {
        return  " +" + value + "\uD83D\uDEE1";
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getBlock() {
        return value;
    }
}
