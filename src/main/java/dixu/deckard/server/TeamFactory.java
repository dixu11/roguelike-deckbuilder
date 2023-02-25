package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;

public class TeamFactory {

    private MinionFactory minionFactory = new MinionFactory();

    public Team createPlayer() {
        List<Minion> minions = new ArrayList<>();
        minions.add(new Minion());
        minions.add(new Minion());
        return new Team(minions, TeamSide.LEFT);
    }

    public Team createEnemy() {
        List<Minion> minions = new ArrayList<>();
        minions.add(new Minion());
        minions.add(new Minion());
        return new Team(minions, TeamSide.RIGHT);
    }
}
