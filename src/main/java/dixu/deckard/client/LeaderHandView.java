package dixu.deckard.client;


import dixu.deckard.server.Leader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderHandView{

    public static final int X = GuiParams.getWidth(0.37);
    public static final int Y = GuiParams.getHeight(0.8);
    private final List<CardView> cardViews = new ArrayList<>();

    public LeaderHandView(Leader leader) {
        leader.getHand()
                .forEach(card -> cardViews.add(new CardView(card)));
    }

    public void render(Graphics g) {
        g.translate(X, Y);

        List<CardView> views = new ArrayList<>(cardViews); //for concurrency safety
        for (int i = 0; i < views.size(); i++) {
            CardView cardView = views.get(i);
            cardView.render(g,i);
        }

        g.translate(-X, -Y);
    }

    public void reactToClickOnScreen(int x, int y) { //may have bad performance
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            if (cardView.isClicked(x, y, X, Y, i)) {
                cardView.onClick();
                return;
            }
        }
    }
}
