package dixu.deckard.client;

import dixu.deckard.server.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HandView {
    public static final int CARD_PADDING = 20;

    private List<Card> cards;
    private List<CardView> cardViews = new ArrayList<>();

    public HandView(List<Card> cards) {
        this.cards = cards;
    }

    private void load() {
        cardViews = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            CardView view = new CardView(i, cards);
            cardViews.add(view);
        }
    }

    public void render(Graphics g) {
        if (cardViews.size() != cards.size()) { //todo remove after draw card event handled
            load();
        }
        int xChange =-CardView.CARD_WIDTH+10;
        g.translate(xChange,0);
        int space = CardView.CARD_WIDTH + CARD_PADDING;
        for (int i = cards.size()-1; i >=0; i--) {
            g.translate(space, 0);
            cardViews.get(i).render(g);
            xChange += space;
        }
        g.translate(-xChange, 0);
    }

    public void remove(Card card) {
        cardViews.removeIf(v -> v.getCard() == card);
    }
}
