package dixu.deckard.server;

public class CardPlayedEvent implements Event {
    private final Team team;
    private final Card card;
    private final Minion target;

    public CardPlayedEvent(Team team, Card card, Minion target) {
        this.team = team;
        this.card = card;
        this.target = target;
    }

    public Team getTeam() {
        return team;
    }

    public Minion getTarget() {
        return target;
    }

    public Card getCard() {
        return card;
    }
}
