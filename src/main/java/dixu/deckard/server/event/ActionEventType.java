package dixu.deckard.server.event;

/**
 * (SOURCE)_(EVENT NAME)
 */
public enum ActionEventType implements EventType {
    MINION_CARD_DISCARDED, //action
    MINION_CARD_DRAW, //action
    MINION_DAMAGED, //action
    MINION_DIED, //action
    MINION_SHUFFLE, //action
    LEADER_SPECIAL_UPGRADE, //action
    LEADER_SPECIAL_STEAL, //action
    LEADER_SPECIAL_MOVE_HAND, //action
    MINION_CARD_PLAYED, //action
    MINION_REGENERATED, //action
    TEAM_BLOCK_CHANGED, //state
    MINION_HAND_CHANGED, // state
    LEADER_ENERGY_CHANGED, //state
    LEADER_HAND_CHANGED, //state
    CARD_OWNER_CHANGED; //state

    public static Object determineSourceFromEventName(ActionEvent actionEvent) {
        if (actionEvent.getType().name().startsWith("MINION")) {
            return actionEvent.getMinion();
        } else if (actionEvent.getType().name().startsWith("TEAM")) {
            return actionEvent.getOwnTeam();
        } else if (actionEvent.getType().name().startsWith("LEADER")) {
            return actionEvent.getLeader();
        } else {
            throw new IllegalStateException("UNKNOWN EVENT SOURCE");
        }
    }

    @Override
    public String getType() {
        return name();
    }

    public boolean isSpecial() {
        return name().contains("SPECIAL");
    }
}
