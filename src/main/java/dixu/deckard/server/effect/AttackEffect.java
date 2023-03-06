package dixu.deckard.server.effect;

public interface AttackEffect extends CardEffect{
    void setPiercing(boolean piercing);

    void setValue(int value);
}
