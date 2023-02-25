package dixu.deckard.server;

public class DrawCardEvent implements Event {
    private Card card;
    private Minion minion;
    private Team team;

    public DrawCardEvent(Card card, Minion minion, Team team) {
        this.card = card;
        this.minion = minion;
        this.team = team;
    }

    public Card getCard() {
        return card;
    }

    public Minion getMinion() {
        return minion;
    }

    public Team getTeam() {
        return team;
    }
}
