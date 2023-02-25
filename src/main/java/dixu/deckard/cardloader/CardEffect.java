package dixu.deckard.cardloader;

import dixu.deckard.server.Minion;
import dixu.deckard.server.Team;

public class CardEffect {
    private EffectType type;
    private Integer power;

    public CardEffect() { }

    public void apply(Team team, Minion minion) {
        type.apply(team, minion, power);
    }

    @Override
    public String toString() {
        return "CardEffect{" +
                "type=" + type +
                ", power=" + power +
                '}';
    }
}
