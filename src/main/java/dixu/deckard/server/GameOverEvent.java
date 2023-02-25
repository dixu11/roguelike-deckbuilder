package dixu.deckard.server;

public class GameOverEvent implements Event {
    private final Team lost;

    public GameOverEvent(Team lost) {
        this.lost = lost;
    }

    public Team getLost() {
        return lost;
    }
}
