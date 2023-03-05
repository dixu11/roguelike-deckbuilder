package dixu.deckard.server.effect;

import dixu.deckard.server.CardContext;

public class BlockEffect implements CardEffect {
    private int value;

    public BlockEffect(int value) {
        this.value = value;
    }

    @Override
    public void execute(CardContext context) {
        context.getOwnTeam().addBlock(value);
    }

    @Override
    public String getDescription() {
        return  " +" + value + "\uD83D\uDEE1";
    }
}
