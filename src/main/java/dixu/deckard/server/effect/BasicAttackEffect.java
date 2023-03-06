package dixu.deckard.server.effect;

import dixu.deckard.server.Card;
import dixu.deckard.server.CardContext;

public class BasicAttackEffect implements AttackEffect {
    final int initialValue;
    int value;
    EnemySelection type;
    boolean isPiercing = false;
    Card card;

    public BasicAttackEffect(int value, EnemySelection type, Card card) {
        this.value = value;
        this.type = type;
        initialValue = value;
        this.card = card;
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

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getInitialValue() {
        return initialValue;
    }

    @Override
    public void modifyValueBy(int value) {
        this.value += value;
    }

    public boolean isPiercing() {
        return isPiercing;
    }

    public Card getCard() {
        return card;
    }
}
