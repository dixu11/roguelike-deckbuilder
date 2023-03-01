package dixu.deckard;

import dixu.deckard.server.Leader;
import dixu.deckard.server.TeamFactory;

public class LeaderFactory {

    private TeamFactory teamFactory = new TeamFactory();

    public Leader create() {
        return new Leader(teamFactory.create());
    }
}
