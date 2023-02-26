package dixu.deckard.server;

import lombok.Builder;

@Builder
public class CardContext {
    private Team actionTeam;
    private Team enemyTeam;
    private Minion minion;
    private Card card;

    public Team getActionTeam() {
        return actionTeam;
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

    public CardContext getCopy() {
        return builder()
                .actionTeam(actionTeam)
                .enemyTeam(enemyTeam)
                .minion(minion)
                .card(card)
                .build();
    }
}
