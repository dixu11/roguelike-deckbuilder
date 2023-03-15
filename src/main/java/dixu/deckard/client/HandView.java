package dixu.deckard.client;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class HandView {
    final List<CardView> cardViews = new ArrayList<>();

    public void render(Graphics2D g,int layoutX,int layoutY) {
        List<CardView> views = new ArrayList<>(cardViews); //for concurrency safety
        for (int i = 0; i < views.size(); i++) {
            CardView cardView = views.get(i);
            cardView.render(g,layoutX,layoutY, i);
        }
    }

    public boolean reactToClickOnWindow(int windowX, int windowY) {
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            if (cardView.isClicked(windowX, windowY)) {
                postEventOnClick(cardView);
                return true;
            }
        }
        return false;
    }

    abstract void postEventOnClick(CardView clickedCard);
}
