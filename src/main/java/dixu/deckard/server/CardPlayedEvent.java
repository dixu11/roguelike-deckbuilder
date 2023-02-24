package dixu.deckard.server;

public class CardPlayedEvent implements Event {
    private final Team team;
    private final Card card;
    private final Character target;

    public CardPlayedEvent(Team team, Card card, Character target) {
        this.team = team;
        this.card = card;
        this.target = target;
    }

    public Team getTeam() {
        return team;
    }

    public Character getTarget() {
        return target;
    }

    public Card getCard() {
        return card;
    }
}
