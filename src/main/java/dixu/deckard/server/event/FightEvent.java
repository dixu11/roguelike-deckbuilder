package dixu.deckard.server.event;

import dixu.deckard.server.Card;
import dixu.deckard.server.CardContext;
import dixu.deckard.server.Minion;
import dixu.deckard.server.Team;
import lombok.Builder;

@Builder
public class FightEvent implements Event<FightEventName>{
    private FightEventName name;
    private Object source;
    private Team ownTeam;
    private Team enemyTeam;
    private Minion minion;
    private Card card;
    private int value;

    public static FightEvent of(FightEventName name) {
        return ofName(name)
                .build();
    }

    public static FightEvent of(FightEventName name, CardContext context) {
       return ofName(name)
                .ownTeam(context.getOwnTeam())
                .enemyTeam(context.getEnemyTeam())
                .minion(context.getMinion())
                .card(context.getCard())
                .build();
    }

    private static FightEventBuilder ofName(FightEventName name) {
        return builder()
                .name(name);
    }
    @Override
    public FightEventName getName() {
        return name;
    }

    public Object getSource() {
        return source;
    }

    public int value() {
        return value;
    }

    public Team getOwnTeam() {
        return ownTeam;
    }

    public Minion getMinion() {
        return minion;
    }

    public Card getCard() {
        return card;
    }
}
