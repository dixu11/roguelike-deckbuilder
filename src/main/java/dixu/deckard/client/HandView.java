package dixu.deckard.client;

import dixu.deckard.server.event.BusManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class HandView {
    final BusManager bus = BusManager.instance();
    final List<CardView> cardViews = new ArrayList<>();
    int x;
    int y;

    public HandView(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.translate(x, y);

        List<CardView> views = new ArrayList<>(cardViews); //for concurrency safety
        for (int i = 0; i < views.size(); i++) {
            CardView cardView = views.get(i);
            cardView.render(g, i);
        }

        g.translate(-x, -y);
    }

    public void reactToClickOnScreen(int windowX, int windowY) {
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            if (cardView.isClicked(windowX, windowY, x, y, i)) {
                postEventOnClick(cardView);
                return;
            }
        }
    }

    abstract void postEventOnClick(CardView clickedCard);
}
