package dixu.deckard.client;

import dixu.deckard.server.Card;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.client.GuiParams.*;

public class CardView {

    private final List<CounterView> counters = new ArrayList<>();
    private final Card card;

    public CardView(Card card) {
        this.card = card;
    }

    public void render(Graphics g) {
        if (card == null) {
            return;
        }
        g.setColor(MAIN_COLOR_BRIGHT);
        g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        g.setColor(MAIN_COLOR_DARK);
        g.drawString(card.getName(), CARD_WIDTH / 10, CARD_HEIGHT / 6);

        for (CounterView counter : counters) {
            counter.render(g, new Rectangle(0, 0, CARD_WIDTH, CARD_HEIGHT));
        }
    }

    public void addCounter(CounterView counter) {
        counters.add(counter);
    }

    public Card getCard() {
        return card;
    }
}
