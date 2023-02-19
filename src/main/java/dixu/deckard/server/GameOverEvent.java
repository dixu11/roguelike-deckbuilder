package dixu.deckard.server;

public class GameOverEvent implements Event {
    private final Player winner;

    public GameOverEvent(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }
}
