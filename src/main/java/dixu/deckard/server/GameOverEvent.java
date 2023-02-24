package dixu.deckard.server;

public class GameOverEvent implements Event {
    private final Team winner;

    public GameOverEvent(Team winner) {
        this.winner = winner;
    }

    public Team getWinner() {
        return winner;
    }
}
