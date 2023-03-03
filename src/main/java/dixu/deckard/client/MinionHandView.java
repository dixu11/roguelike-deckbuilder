package dixu.deckard.client;

import dixu.deckard.server.*;
import dixu.deckard.server.event.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MinionHandView  implements ActionEventHandler{
    private final BusManager bus = BusManager.instance();
    private final List<CardView> cardViews = new ArrayList<>();
    private int translateX;
    private int translateY;
    private final Minion minion;

    public MinionHandView(Minion minion) {
        this.minion = minion;

        bus.register(this,ActionEventName.MINION_HAND_CHANGED);
    }

    public void render(Graphics2D g, int x, int y) {
        if (cardViews.isEmpty()) {
            return;
        }
        g.translate(x, y);
        translateX = (int) g.getTransform().getTranslateX();
        translateY = (int) g.getTransform().getTranslateY();
        List<CardView> views = new ArrayList<>(cardViews); //for concurrency safety
        for (int i = 0; i < views.size(); i++) {
            CardView cardView = views.get(i);
            cardView.render(g, i);
        }

        g.translate(-x, -y);

    }

    public void add(Card card) {
        cardViews.add(0, new CardView(card));
    }

    public void remove(Card card) {
        cardViews.removeIf(v -> v.getCard() == card);
    }

    public Optional<CardView> reactToClickOnScreen(int clickX, int clickY) { //may have bad performance
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            if (cardView.isClicked(clickX, clickY, translateX, translateY, i)) {
                return Optional.of(cardView);
            }
        }
        return Optional.empty();
    }


    @Override
    public void handle(ActionEvent event) {
        if (event.getName() == ActionEventName.MINION_HAND_CHANGED && event.getMinion() == minion) {
            cardViews.clear();
            for (Card card : minion.getHand()) {
                add(card);
            }
        }
    }
}
