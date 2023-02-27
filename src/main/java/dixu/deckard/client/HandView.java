package dixu.deckard.client;

import dixu.deckard.server.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.client.GuiParams.CARD_WIDTH;

public class HandView {
    public static final int CARD_PADDING = 20;

    private final List<CardView> cardViews = new ArrayList<>();

    public void render(Graphics g) {
        if (cardViews.isEmpty()) {
            return;
        }

        int xChange = -CARD_WIDTH + 10;
        g.translate(xChange, 0);
        int space = CARD_WIDTH + CARD_PADDING;
        for (CardView cardView : cardViews) {
            g.translate(space, 0);
            cardView.render(g);
            xChange += space;
        }
        g.translate(-xChange, 0);
    }

    public void add(Card card) {
        cardViews.add(0, new CardView(card));
    }

    public void remove(Card card) {
        cardViews.removeIf(v -> v.getCard() == card);
    }
}
