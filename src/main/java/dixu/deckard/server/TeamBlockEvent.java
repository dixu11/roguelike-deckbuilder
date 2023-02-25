package dixu.deckard.server;

public class TeamBlockEvent implements Event {
    private int newValue;
    private int oldValue;
    private Team team;

    public TeamBlockEvent(int newValue, int oldValue, Team team) {
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.team = team;
    }

    public int getNewValue() {
        return newValue;
    }



    public Team getTeam() {
        return team;
    }


}
