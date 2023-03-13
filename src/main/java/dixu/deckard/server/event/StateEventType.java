package dixu.deckard.server.event;

public enum StateEventType implements EventType {
    ;

    @Override
    public String getType() {
        return name();
    }
}
