package dixu.deckard.client;


import dixu.deckard.server.Card;
import dixu.deckard.server.Leader;
import dixu.deckard.server.event.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderHandView implements ActionEventHandler{

    private final BusManager bus = BusManager.instance();
    public static final int X = GuiParams.getWidth(0.37); //extract outside?
    public static final int Y = GuiParams.getHeight(0.8);
    private final List<CardView> cardViews = new ArrayList<>();
    private Leader leader;

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

    public void render(Graphics g) {
        g.translate(X, Y);

        List<CardView> views = new ArrayList<>(cardViews); //for concurrency safety
        for (int i = 0; i < views.size(); i++) {
            CardView cardView = views.get(i);
            cardView.render(g, i);
        }

        g.translate(-X, -Y);
    }

    public void reactToClickOnScreen(int x, int y) { //may have bad performance
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            if (cardView.isClicked(x, y, X, Y, i)) {
                bus.post(GuiEvent.builder()
                        .name(GuiEventName.LEADER_CARD_SELECTED)
                        .cardView(cardView)
                        .build()
                );
                return;
            }
        }
    }

    public Leader getLeader() {
        return leader;
    }

    public void remove(Card card) {
        cardViews.removeIf(v -> v.getCard() == card);
    }

    public void add(Card card) {
        cardViews.add(new CardView(card));
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getName() == ActionEventName.LEADER_HAND_CHANGED && leader == event.getLeader()) {
            reloadCards();
        }
    }
}
