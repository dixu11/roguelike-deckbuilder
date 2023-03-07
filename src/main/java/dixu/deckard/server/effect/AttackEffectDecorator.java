package dixu.deckard.server.effect;

public class AttackEffectDecorator extends EffectDecorator<AttackEffect> implements AttackEffect {
    public AttackEffectDecorator(AttackEffect decorated) {
        super(decorated);
    }

    @Override
    public void setPiercing(boolean piercing) {
        decorated.setPiercing(piercing);
    }

    @Override
    public EnemySelection getType() {
        return decorated.getType();
    }

    @Override
    public boolean isPiercing() {
        return decorated.isPiercing();
    }
}
