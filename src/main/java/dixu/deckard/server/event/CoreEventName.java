package dixu.deckard.server.event;

public enum CoreEventName implements EventName {
    GAME_OVER, TURN_ENDED, TURN_STARTED;

    @Override
    public String getName() {
        return name();
    }
}
