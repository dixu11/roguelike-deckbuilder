package dixu.deckard.server;

public class PlayContext {
    private Team playTeam;
    private Team enemyTeam;
    private Minion minion;
    private Card card;

    public PlayContext(Team playTeam, Team enemyTeam) {
        this.playTeam = playTeam;
        this.enemyTeam = enemyTeam;
    }

    public Team getPlayTeam() {
        return playTeam;
    }

    public Team getEnemyTeam() {
        return enemyTeam;
    }

    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Minion getMinion() {
        return minion;
    }

    public Card getCard() {
        return card;
    }
}
