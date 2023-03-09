package dixu.deckard.server.event;

/**
 * (SOURCE)_(EVENT NAME)
* */
public enum ActionEventType implements EventType {
    MINION_CARD_DISCARDED,
    MINION_CARD_DRAW,
    MINION_DAMAGED,
    MINION_DIED,
    MINION_SHUFFLE,
    TEAM_BLOCK_CHANGED,
    LEADER_SPECIAL_UPGRADE,
    MINION_HAND_CHANGED,
    LEADER_SPECIAL_STEAL,
    LEADER_SPECIAL_MOVE_HAND,
    LEADER_ENERGY_CHANGED,
    LEADER_HAND_CHANGED,
    MINION_CARD_PLAYED,
    MINION_REGENERATED;

    public static Object determineSourceFromEventName(ActionEvent actionEvent) {
        if (actionEvent.getType().name().startsWith("MINION")) {
            return actionEvent.getMinion();
        } else if (actionEvent.getType().name().startsWith("TEAM")) {
            return actionEvent.getOwnTeam();
        } else if(actionEvent.getType().name().startsWith("LEADER")){
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