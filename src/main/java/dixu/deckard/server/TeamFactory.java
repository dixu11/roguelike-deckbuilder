package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;

public class TeamFactory {

    public Team createPlayer() {
        List<Minion> minions = new ArrayList<>();
        minions.add(new Minion());
        minions.add(new Minion());
        return new Team(minions);
    }

    public Team createEnemy() {
        List<Minion> minions = new ArrayList<>();
        minions.add(new Minion());
        minions.add(new Minion());
        return new Team(minions);
    }
}
