package dixu.deckard.server.leader;

import dixu.deckard.server.team.TeamFactory;

public class LeaderFactory {
    private final TeamFactory teamFactory = new TeamFactory();

    public Leader create(LeaderType type) {
        return new Leader(teamFactory.create(type));
    }
}
