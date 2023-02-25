package dixu.deckard.server;

public class TeamBlockEvent implements Event {
    private int newValue;
    private Team team;

    public TeamBlockEvent(int valueChange, Team team) {
        this.newValue = valueChange;
        this.team = team;
    }

    public int getNewValue() {
        return newValue;
    }

    public Team getTeam() {
        return team;
    }
}
