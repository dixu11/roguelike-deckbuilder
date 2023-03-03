package dixu.deckard.client;

import dixu.deckard.server.Card;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.client.GuiParams.*;

public class CardView {
    private final List<CounterView> counters = new ArrayList<>();
    private final Card card;
    private boolean selected = false;

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

        if (!selected) {
            return;
        }
        g.setColor(HIGHLIGHT_COLOR);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(HIGHLIGHT_BORDER));
        g.drawRect(-HIGHLIGHT_BORDER,0,CARD_WIDTH+HIGHLIGHT_BORDER,CARD_HEIGHT+HIGHLIGHT_BORDER);
        g2d.setStroke(new BasicStroke(1));
    }

    public void render(Graphics g, int index) {
        int translateX = index * (CARD_WIDTH + CARD_PADDING);
        g.translate(translateX, 0);
        render(g);
        g.translate(-translateX, 0);
    }

    public void addCounter(CounterView counter) {
        counters.add(counter);
    }

    public Card getCard() {
        return card;
    }

    public boolean isClicked(int x, int y, int transX, int transY, int i) {
        Rectangle bounds = new Rectangle(transX + i*(CARD_WIDTH+CARD_PADDING), transY, CARD_WIDTH, CARD_HEIGHT);
        return bounds.intersects(new Rectangle(x, y, 1, 1));
    }

    public void toggleSelected() {
        selected = !selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
