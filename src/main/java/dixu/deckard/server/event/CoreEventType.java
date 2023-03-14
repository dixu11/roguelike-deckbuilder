package dixu.deckard.server.event;

public enum CoreEventType implements EventType {
    SETUP_PHASE_STARTED,
    LEADER_PHASE_STARTED,
    MINION_PHASE_STARTED,
    COMBAT_OVER;

    @Override
    public String getType() {
        return name();
    }
}
