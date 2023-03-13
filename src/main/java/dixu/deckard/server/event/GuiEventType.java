package dixu.deckard.server.event;

public enum GuiEventType implements EventType {
    LEADER_CARD_SELECTED, MINION_CARD_SELECTED, MINION_SELECTED;

    @Override
    public String getType() {
        return name();
    }
}
