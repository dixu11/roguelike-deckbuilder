package dixu.deckard.server;

public class LeaderFactory {

    private TeamFactory teamFactory = new TeamFactory();
    private CardFactory cardFactory = new CardFactory();

    public Leader create(LeaderType type) {
        Leader leader = new Leader(teamFactory.create());
        if (type == LeaderType.PLAYER) {
            leader.addCards(cardFactory.createCards(5,CardType.ATTACK));
        }
        return leader;
    }
}
