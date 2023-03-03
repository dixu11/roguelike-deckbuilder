package dixu.deckard.server;

public class LeaderFactory {

    private TeamFactory teamFactory = new TeamFactory();
    private CardFactory cardFactory = new CardFactory();

    public Leader create(LeaderType type) {
        return new Leader(teamFactory.create(type));
    }
}
