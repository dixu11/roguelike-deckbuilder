package dixu.deckard.client;

import dixu.deckard.server.*;
import dixu.deckard.server.event.*;
import dixu.deckard.server.event.Event;

import java.awt.*;

public class MinionView implements FightEventHandler {

    private final BusManager bus = BusManager.instance();
    private final Minion minion;
    private final CardView cardView;
    private final HandView handView;
    private final CounterView drawCounter;
    private final CounterView discardCounter;

    public MinionView(Minion minion) {
        this.minion = minion;
        this.handView = new HandView(minion.getHand());
        cardView = new CardView( minion.getMinionCard());
        EventCounterView healthCounter = new EventCounterView(Direction.BOTTOM, Direction.BOTTOM);
        healthCounter.setDescription("♥: ");
        healthCounter.setSource(minion);
        healthCounter.setValue(minion.getHealth());
        bus.register(healthCounter, FightEventName.MINION_DAMAGED);
        cardView.addCounter(healthCounter);
        CounterView drawCounter = new SourceCounterView(Direction.BOTTOM, Direction.LEFT, () -> minion.getDraw().size(), Color.GRAY);
        drawCounter.setDescription("\uD83C\uDCA0: ");
        drawCounter.setValue(minion.getDraw().size());
        this.drawCounter = drawCounter;
        CounterView discardCounter = new EventCounterView(Direction.BOTTOM, Direction.RIGHT, Color.GRAY);
        discardCounter.setBlinking(false);
        discardCounter.setDescription("\uD83C\uDCC1: ");
        this.discardCounter = discardCounter;
        bus.register(this, FightEventName.MINION_CARD_PLAYED);
        bus.register(this, FightEventName.MINION_CARD_DRAW);
        bus.register(this,FightEventName.MINION_SHUFFLE);
    }

    public void render(Graphics g) {
        cardView.render(g);
        g.translate(-CardView.CARD_WIDTH,-CardView.CARD_HEIGHT -20);
        handView.render(g);
        g.translate(CardView.CARD_WIDTH,CardView.CARD_HEIGHT +20);
        Rectangle r = new Rectangle(0,CardView.CARD_HEIGHT/2,CardView.CARD_WIDTH,CardView.CARD_HEIGHT);
       drawCounter.render(g,r);
       discardCounter.render(g,r);
    }


    @Override
    public void handle(FightEvent event) {
        switch (event.getName()) {
            case MINION_CARD_PLAYED -> onCardPlayed(event);
            case MINION_CARD_DRAW -> onCardDraw(event);
            case MINION_SHUFFLE -> onShuffle(event);
        }
    }

    private void onCardPlayed(FightEvent event) {
        if (event.getMinion()==minion) {
            discardCounter.addValue(1);
            handView.remove(event.getCard());
        }
    }

    private void onCardDraw(FightEvent event) {
        if (event.getMinion() == minion) {
            handView.addCard(event.getCard());
            drawCounter.setValue(minion.getDraw().size());
        }
    }

    private void onShuffle(FightEvent event) {
        if (event.getMinion() == minion) {
            discardCounter.setValue(minion.getDiscard().size());
            drawCounter.setValue(minion.getDraw().size());
        }
    }

    public Minion getCharacter() {
        return minion;
    }
}
