package dixu.deckard.server.event;

public enum CoreEventType implements EventType {
    GAME_OVER, TURN_ENDED, TURN_STARTED;

    @Override
    public String getType() {
        return name();
    }
}
