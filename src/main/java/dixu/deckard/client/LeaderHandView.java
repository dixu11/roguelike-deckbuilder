package dixu.deckard.client;

import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.event.*;

public class LeaderHandView extends HandView implements ActionEventHandler{

    private final Leader leader;

    public LeaderHandView(Leader leader) {
        this.leader = leader;
        reloadCards();

        bus.register(this,ActionEventName.LEADER_HAND_CHANGED);
    }

    private void reloadCards() {
        cardViews.clear();
        leader.getHand()
                .forEach(card -> cardViews.add(new CardView(card)));
    }



    @Override
    public void handle(ActionEvent event) {
        if (event.getName() == ActionEventName.LEADER_HAND_CHANGED && leader == event.getLeader()) {
            reloadCards();
        }
    }

    public Leader getLeader() {
        return leader;
    }

    @Override
    void postEventOnClick(CardView clickedCard) {
        bus.post(GuiEvent.builder()
                .name(GuiEventName.LEADER_CARD_SELECTED)
                .cardView(clickedCard)
                .build()
        );
    }
}
