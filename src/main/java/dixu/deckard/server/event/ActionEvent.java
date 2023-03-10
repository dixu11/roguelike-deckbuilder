package dixu.deckard.server.event;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.team.Team;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ActionEvent implements Event<ActionEventType> {

    //when adding field update lombok builder on bottom!
    private ActionEventType type;
    private ActionEventSubtype subtype;
    private Object source;
    private Team ownTeam;
    private Team enemyTeam;
    private Minion minion;
    private Card card;
    private Card oldCard;
    private int value;
    private int oldValue;
    private Leader leader;

    public static ActionEvent of(ActionEventType type, CardContext context) {
        return ofType(type)
                .ownTeam(context.getOwnTeam())
                .enemyTeam(context.getEnemyTeam())
                .minion(context.getMinion())
                .card(context.getCard())
                .build();
    }

    private static ActionEventBuilder ofType(ActionEventType type) {
        return builder()
                .type(type);
    }

    public boolean isSpecial() {
        return type.isSpecial();
    }

    public ActionEventType getType() {
        return type;
    }

    public static class ActionEventBuilder {

        public ActionEvent build() {
            ActionEvent actionEvent = new ActionEvent(this.type,this.subtype, this.source, this.ownTeam,
                    this.enemyTeam, this.minion, this.card,this.oldCard, this.value,this.oldValue,this.leader);
            actionEvent.source = ActionEventType.determineSourceFromEventName(actionEvent);

            return actionEvent;
        }
    }
}
