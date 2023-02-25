package dixu.deckard.server;

public class MinionDiedEvent implements Event{
    private TeamSide side;
    private Minion minion;

    public MinionDiedEvent(TeamSide side, Minion minion) {
        this.side = side;
        this.minion = minion;
    }

    public TeamSide getSide() {
        return side;
    }

    public Minion getCharacter() {
        return minion;
    }
}
