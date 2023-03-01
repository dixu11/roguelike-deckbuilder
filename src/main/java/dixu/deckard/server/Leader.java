package dixu.deckard.server;

import static dixu.deckard.server.GameParams.INITIAL_ENERGY;

public class Leader {
    private Team team;
    private int energy = INITIAL_ENERGY;

    public Leader(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}
