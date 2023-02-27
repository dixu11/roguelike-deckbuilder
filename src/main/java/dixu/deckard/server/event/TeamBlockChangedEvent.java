package dixu.deckard.server.event;

import dixu.deckard.client.EventCounterView;
import dixu.deckard.server.Team;

public class TeamBlockChangedEvent implements EventCounterEvent {
    private final int newValue;
    private final int oldValue;
    private final Team team;

    public TeamBlockChangedEvent(int newValue, int oldValue, Team team) {
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.team = team;
    }

    @Override
    public void accept(EventCounterView visitor) {
        visitor.handleTeamBlockChanged(this);
    }

    public int getNewValue() {
        return newValue;
    }

    public Team getTeam() {
        return team;
    }


}
