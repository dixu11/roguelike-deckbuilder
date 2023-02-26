package dixu.deckard.server.event;

import dixu.deckard.server.Minion;
import dixu.deckard.server.Team;

public class MinionDiedEvent implements Event {
    private Team team;
    private Minion minion;

    public MinionDiedEvent(Team team, Minion minion) {
        this.team = team;
        this.minion = minion;
    }

    public Team getTeam() {
        return team;
    }

    public Minion getMinion() {
        return minion;
    }
}
