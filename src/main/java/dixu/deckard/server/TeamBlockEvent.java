package dixu.deckard.server;

public class TeamBlockEvent implements Event {
    private int valueChange;
    private Team team;

    public TeamBlockEvent(int valueChange, Team team) {
        this.valueChange = valueChange;
        this.team = team;
    }

    public int getValueChange() {
        return valueChange;
    }

    public Team getTeam() {
        return team;
    }
}
