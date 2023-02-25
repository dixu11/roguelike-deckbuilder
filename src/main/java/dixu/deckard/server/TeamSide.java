package dixu.deckard.server;

public enum TeamSide {
    LEFT,RIGHT;

    public static TeamSide getOther(TeamSide side) {
        return side == LEFT ? RIGHT : LEFT;
    }
}
