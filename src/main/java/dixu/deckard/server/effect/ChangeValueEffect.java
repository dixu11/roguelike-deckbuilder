package dixu.deckard.server.effect;

import dixu.deckard.server.CardContext;

public class ChangeValueEffect extends AttackEffect {

    private AttackEffect attackEffect;
    private int valueModification;

    public ChangeValueEffect(AttackEffect attackEffect, int valueModification) {
        super(0);
        this.attackEffect = attackEffect;
        this.valueModification = valueModification;
    }

    @Override
    public void execute(CardContext context) {
        attackEffect.execute(context);
        attackEffect.value += valueModification;
    }

    @Override
    public String getDescription() {
        return attackEffect.getDescription() +  " -1⚔️ after each play";
    }
}
