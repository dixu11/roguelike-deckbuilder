package dixu.deckard.cardloader;

import dixu.deckard.server.EventBus;
import dixu.deckard.server.Minion;
import dixu.deckard.server.RandomDmgEvent;
import dixu.deckard.server.Team;

public enum EffectType {
    RANDOM_ATTACK(((team, minion, power) -> EventBus.getInstance().post(new RandomDmgEvent(team.getSide(), power)))),
    TEAM_BLOCK(((team, minion, power) -> team.addBlock(power)));

    private final EffectMethod method;

    EffectType(EffectMethod method) {
        this.method = method;
    }

    public void apply(Team team, Minion minion, Integer power) {
        method.apply(team, minion, power);
    }

    private interface EffectMethod {
        void apply(Team team, Minion minion, Integer power);
    }
}
