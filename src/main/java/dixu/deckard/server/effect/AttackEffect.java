package dixu.deckard.server.effect;

import dixu.deckard.server.CardContext;
import dixu.deckard.server.Minion;

import java.util.Optional;

public class AttackEffect implements CardEffect {
    int value;
    DmgType type;

    public AttackEffect(int value,DmgType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public void execute(CardContext context) {
        Optional<Minion> optionalMinion = context.getEnemyTeam().getRandomMinion();
        if (optionalMinion.isEmpty()) return;

        context.getEnemyTeam()
                .applyDmgTo(value,type, optionalMinion.get());
    }

    @Override
    public String getDescription() {
        String modifiers = "";
        if (type == DmgType.PIERCING) {
            modifiers += " Attack ignores block.";
        }

        return " " + value + "⚔️ to random enemy minion." + modifiers;
    }

    @Override
    public int getAttack() {
        return value;
    }

    @Override
    public int getBlock() {
        return 0;
    }
}
