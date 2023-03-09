package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.CardContext;

public class ChangeValueEffect extends AttackEffectDecorator {
    private int valueModification;

    public ChangeValueEffect(AttackEffect attackEffect, int valueModification) {
        super(attackEffect);
        this.valueModification = valueModification;
    }

    public void execute(CardContext context) {
        super.execute(context);
        decorated.setValue(valueModification+decorated.getAttack());
        if (decorated.getAttack() < 0) {
            decorated.setValue(0);
        }
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " -1⚔️ after each play";
    }
}
