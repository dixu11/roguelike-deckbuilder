package dixu.deckard.client;

import dixu.deckard.server.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HandView {
    private static final int CARD_PADDING = 20;

    private List<Card> cards;
    private List<CardView> cardViews;

    public HandView(List<Card> cards) {
        this.cards = cards;
        reload();
    }

    private void reload() {
        cardViews = new ArrayList<>();
        for (Card card : cards) {
            CardView view = new CardView();
            view.setCard(card);
            cardViews.add(view);
        }
    }

    public void render(Graphics g) {
        int xChange =-CardView.CARD_WIDTH+10; //todo
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
