package dixu.deckard.server.effect;

import dixu.deckard.server.CardContext;
import dixu.deckard.server.Minion;

import java.util.Optional;

public class AttackEffect implements CardEffect {
    private int value;

    public AttackEffect(int value) {
        this.value = value;
    }

    @Override
    public void execute(CardContext context) {
        Optional<Minion> optionalMinion = context.getEnemyTeam().getRandomMinion();
        if (optionalMinion.isEmpty()) return;

        context.getEnemyTeam()
                .applyDmgTo(value, optionalMinion.get());
    }
}
