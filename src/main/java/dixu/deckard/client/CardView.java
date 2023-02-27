package dixu.deckard.client;

import dixu.deckard.server.Card;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardView {

    private List<CounterView> counters = new ArrayList<>();
    private final Card card;
    public static final int CARD_WIDTH = 80;
    public static final int CARD_HEIGHT = 120;

    public CardView(Card card) {
        this.card = card;
    }

    public void addCounter(CounterView counter) {
        counters.add(counter);
    }

    public void render(Graphics g) {
        if (card == null) {
            return;
        }
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        g.setColor(Color.DARK_GRAY);
        g.drawString(card.getName(), CARD_WIDTH / 10, CARD_HEIGHT / 6);
        for (CounterView counter : counters) {
            counter.render(g, new Rectangle(0, 0, CARD_WIDTH, CARD_HEIGHT));
        }
    }

    public Card getCard() {
        return card;
    }
}
