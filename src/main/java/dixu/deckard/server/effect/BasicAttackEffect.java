package dixu.deckard.server.effect;

import dixu.deckard.server.Card;
import dixu.deckard.server.CardContext;

public class BasicAttackEffect extends BasicEffect implements AttackEffect{

    EnemySelection type;
    boolean isPiercing = false;
    public BasicAttackEffect(int value,EnemySelection type, Card card) {
        super(value, card);
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
        if (isPiercing) {
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

    @Override
    public void setPiercing(boolean piercing) {
        isPiercing = piercing;
    }

    public boolean isPiercing() {
        return isPiercing;
    }
}
