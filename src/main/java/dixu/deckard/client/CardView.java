package dixu.deckard.client;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardView {

    private String title;
    private List<CounterView> counters = new ArrayList<>();
    public static final int CARD_WIDTH = 80;
    public static final int CARD_HEIGHT = 120;

    public CardView(String name) {
        this.title = name;
    }

    public void addCounter(CounterView counter){
        counters.add(counter);
    }
    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0,0,CARD_WIDTH,CARD_HEIGHT);
        g.setColor(Color.DARK_GRAY);
        g.drawString(title,CARD_WIDTH/6,CARD_HEIGHT/6);
        for (CounterView counter : counters) {
            counter.render(g,new Rectangle(0,0,CARD_WIDTH,CARD_HEIGHT));
        }
    }
}
