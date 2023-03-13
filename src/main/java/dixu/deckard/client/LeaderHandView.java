package dixu.deckard.client;

import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.event.*;

public class LeaderHandView extends HandView implements EventHandler {

    private final Leader leader;

    public LeaderHandView(Leader leader) {
        this.leader = leader;
        reloadCards();

        Bus.register(this, ActionEventType.LEADER_HAND_CHANGED);
    }

    private void reloadCards() {
        cardViews.clear();
        leader.getHand()
                .forEach(card -> cardViews.add(new CardView(card)));
    }



    @Override
    public void handle(ActionEvent event) {
        if (event.getType() == ActionEventType.LEADER_HAND_CHANGED && leader == event.getLeader()) {
            reloadCards();
        }
    }

    public Leader getLeader() {
        return leader;
    }

    @Override
    void postEventOnClick(CardView clickedCard) {
        Bus.post(GuiEvent.builder()
                .name(GuiEventType.LEADER_CARD_SELECTED)
                .cardView(clickedCard)
                .build()
        );
    }
}
