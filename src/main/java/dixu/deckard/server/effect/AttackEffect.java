package dixu.deckard.server.effect;

import dixu.deckard.server.CardContext;

public class AttackEffect implements CardEffect {
    int value;
    EnemySelection type;
    boolean isPiercing = false;

    public AttackEffect(int value, EnemySelection type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public void execute(CardContext context) {
        context.getEnemyTeam()
                .applyDmgTo(value,this);
    }

    @Override
    public String getDescription() {
        String modifiers = "";
        if (type == EnemySelection.PIERCING) {
            modifiers += " Attack ignores block.";
        }

        return " " + value + "⚔️" + modifiers;
    }

    @Override
    public int getAttack() {
        return value;
    }

    @Override
    public int getBlock() {
        return 0;
    }

    public EnemySelection getType() {
        return type;
    }

    public void setPiercing(boolean piercing) {
        isPiercing = piercing;
    }

    public boolean isPiercing() {
        return isPiercing;
    }
}
