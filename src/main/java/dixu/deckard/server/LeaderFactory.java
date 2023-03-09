package dixu.deckard.server;

public class LeaderFactory {
    private final TeamFactory teamFactory = new TeamFactory();

    public Leader create(LeaderType type) {
        return new Leader(teamFactory.create(type));
    }
}
