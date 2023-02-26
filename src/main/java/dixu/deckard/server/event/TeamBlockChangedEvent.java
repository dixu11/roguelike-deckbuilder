package dixu.deckard.server.event;

import dixu.deckard.server.Team;

public class TeamBlockChangedEvent implements Event {
    private final int newValue;
    private final int oldValue;
    private final Team team;

    public TeamBlockChangedEvent(int newValue, int oldValue, Team team) {
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
