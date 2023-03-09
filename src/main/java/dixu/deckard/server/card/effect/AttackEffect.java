package dixu.deckard.server.card.effect;


public interface AttackEffect extends CardEffect{
    void setPiercing(boolean piercing);

    EnemySelection getType();

    boolean isPiercing();
}
