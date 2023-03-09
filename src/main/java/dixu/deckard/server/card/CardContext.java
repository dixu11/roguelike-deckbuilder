package dixu.deckard.server.card;

import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.team.Team;
import lombok.Builder;

@Builder
public class CardContext {
    private Team ownTeam;
    private Team enemyTeam;
    private Minion minion;
    private Card card;

    public Team getOwnTeam() {
        return ownTeam;
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
                .ownTeam(ownTeam)
                .enemyTeam(enemyTeam)
                .minion(minion)
                .card(card)
                .build();
    }
}
