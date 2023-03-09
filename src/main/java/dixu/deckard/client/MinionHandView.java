package dixu.deckard.client;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.event.*;

public class MinionHandView extends HandView  implements ActionEventHandler{
    private final MinionView minionView;

    public MinionHandView(MinionView minionView) {
        super();
        this.minionView = minionView;
        bus.register(this,ActionEventName.MINION_HAND_CHANGED);
    }

    @Override
    void postEventOnClick(CardView clickedCard) {
        bus.post(GuiEvent.builder()
                .name(GuiEventName.MINION_CARD_SELECTED)
                .cardView(clickedCard)
                .minionView(minionView)
                .teamView(minionView.getTeamView())
                .build()
        );
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getName() == ActionEventName.MINION_HAND_CHANGED && event.getMinion() == minionView.getMinion()) {
            cardViews.clear();
            for (Card card : minionView.getMinion().getHand()) {
                add(card);
            }
        }
    }

    public void add(Card card) {
        cardViews.add( new CardView(card));
    }

    public void remove(Card card) {
        cardViews.removeIf(v -> v.getCard() == card);
    }
}
