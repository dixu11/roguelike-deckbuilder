package dixu.deckard.server.effect;

import dixu.deckard.server.Card;

public interface AttackEffect extends CardEffect{
    void setPiercing(boolean piercing);

    EnemySelection getType();

    boolean isPiercing();
}
