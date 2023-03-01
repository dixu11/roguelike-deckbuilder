package dixu.deckard.server.event;

/**
 * (SOURCE)_(EVENT NAME)
* */
public enum ActionEventName implements EventName {
    MINION_CARD_PLAYED,
    MINION_CARD_DRAW,
    MINION_DAMAGED,
    MINION_DIED,
    MINION_SHUFFLE,
    TEAM_BLOCK_CHANGED;

    public static Object determineSourceFromEventName(ActionEvent actionEvent) {
        if (actionEvent.getName().name().startsWith("MINION")) {
            return actionEvent.getMinion();
        } else if (actionEvent.getName().name().startsWith("TEAM")) {
            return actionEvent.getOwnTeam();
        } else {
            throw new IllegalStateException("UNKNOWN EVENT SOURCE");
        }
    }

    @Override
    public String getName() {
        return name();
    }
}
