package dixu.deckard.server;

public class TurnChangedEvent implements Event {
    private final Player player;

    public TurnChangedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
