package dixu.deckard.server;

public class RandomDmgEvent implements Event {
    private TeamSide sentDmg;
    private int value;

    public RandomDmgEvent(TeamSide sentDmg, int value) {
        this.sentDmg = sentDmg;
        this.value = value;
    }

    public TeamSide getSentTeam() {
        return sentDmg;
    }

    public int getValue() {
        return value;
    }
}
