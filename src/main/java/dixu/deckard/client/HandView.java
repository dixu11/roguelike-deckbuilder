package dixu.deckard.client;

import dixu.deckard.server.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HandView {
    public static final int CARD_PADDING = 20;

    private List<CardView> cardViews = new ArrayList<>();

    public void render(Graphics g) {
        if (cardViews.isEmpty()) {
            return;
        }
        int xChange = -CardView.CARD_WIDTH + 10;
        g.translate(xChange, 0);
        int space = CardView.CARD_WIDTH + CARD_PADDING;
        for (int i = 0; i < cardViews.size(); i++) {
            g.translate(space, 0);
            cardViews.get(i).render(g);
            xChange += space;
        }
        g.translate(-xChange, 0);
    }

    public void remove(Card card) {
        cardViews.removeIf(v -> v.getCard() == card);
    }

    public void addCard(Card card) {
        cardViews.add(0, new CardView(card));
    }
}
