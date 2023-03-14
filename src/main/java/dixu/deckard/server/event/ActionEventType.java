package dixu.deckard.server.event;

/**
 * (SOURCE)_(EVENT NAME)
 */
public enum ActionEventType implements EventType {
    MINION_CARD_DISCARDED,
    MINION_CARD_PLAYED,
    MINION_CARD_DRAW,
    MINION_SHUFFLE,
    MINION_DAMAGED,
    MINION_REGENERATED,
    MINION_DIED,
    LEADER_SPECIAL_UPGRADE,
    LEADER_SPECIAL_STEAL,
    LEADER_SPECIAL_MOVE_HAND,
    //state events //todo check if structure is different
    TEAM_BLOCK_CHANGED,
    MINION_HAND_CHANGED,
    LEADER_ENERGY_CHANGED,
    LEADER_HAND_CHANGED,
    CARD_OWNER_CHANGED;

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
