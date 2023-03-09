package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.game.MyRandom;

import java.util.List;

public class HealEffect extends BasicEffect {
    public HealEffect(int value, Card card) {
        super(value, card);
    }

    @Override
    public void execute(CardContext context) {
        List<Minion> woundedAllies = context.getOwnTeam().getMinions().stream().
                filter(Minion::isWounded)
                .toList();

        MyRandom.getRandomElement(woundedAllies)
                .ifPresent(minion -> minion.applyRegen(value));
    }

    @Override
    public String getDescription() {
        return "+1♥ to random wounded friendly minion";
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getBlock() {
        return 0;
    }
}
