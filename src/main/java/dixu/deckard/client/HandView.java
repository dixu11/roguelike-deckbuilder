package dixu.deckard.client;

import dixu.deckard.server.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HandView {
    public static final int CARD_PADDING = 20;

    private List<Card> cards;
    private List<CardView> cardViews;

    public HandView(List<Card> cards) {
        this.cards = cards;
        load();
    }

    private void load() {
        cardViews = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            CardView view = new CardView(i, cards);
            cardViews.add(view);
        }
    }

    public void render(Graphics g) {
        int xChange =-CardView.CARD_WIDTH+10;
        g.translate(xChange,0);
        int space = CardView.CARD_WIDTH + CARD_PADDING;
        for (int i = 0; i < cards.size(); i++) {
            g.translate(space, 0);
            cardViews.get(i).render(g);
            xChange += space;
        }
        g.translate(-xChange, 0);
    }

}
