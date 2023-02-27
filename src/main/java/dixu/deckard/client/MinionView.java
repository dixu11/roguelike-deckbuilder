package dixu.deckard.client;

import dixu.deckard.server.*;
import dixu.deckard.server.event.*;

import java.awt.*;

public class MinionView implements ActionEventHandler {

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
        bus.register(healthCounter, ActionEventName.MINION_DAMAGED);
        cardView.addCounter(healthCounter);
        EventCounterView drawCounter = new EventCounterView(Direction.BOTTOM, Direction.LEFT, Color.GRAY);
        drawCounter.setDescription("\uD83C\uDCA0: ");
        drawCounter.setValue(minion.getDraw().size());
        drawCounter.setSource(minion);
        bus.register(drawCounter,ActionEventName.MINION_CARD_DRAW);
        this.drawCounter = drawCounter;
        EventCounterView discardCounter = new EventCounterView(Direction.BOTTOM, Direction.RIGHT, Color.GRAY);
        discardCounter.setBlinking(false);
        discardCounter.setDescription("\uD83C\uDCC1: ");
        discardCounter.setSource(minion);
        bus.register(discardCounter,ActionEventName.MINION_CARD_PLAYED);
        this.discardCounter = discardCounter;
        bus.register(this, ActionEventName.MINION_CARD_PLAYED);
        bus.register(this, ActionEventName.MINION_CARD_DRAW);
        bus.register(this, ActionEventName.MINION_SHUFFLE);
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
    public void handle(ActionEvent event) {
        switch (event.getName()) {
            case MINION_CARD_PLAYED -> onCardPlayed(event);
            case MINION_CARD_DRAW -> onCardDraw(event);
            case MINION_SHUFFLE -> onShuffle(event);
        }
    }

    private void onCardPlayed(ActionEvent event) {
        if (event.getMinion()==minion) {
            handView.remove(event.getCard());
        }
    }

    private void onCardDraw(ActionEvent event) {
        if (event.getMinion() == minion) {
            handView.addCard(event.getCard());
        }
    }

    private void onShuffle(ActionEvent event) {
        if (event.getMinion() == minion) {
            discardCounter.setValue(minion.getDiscard().size()); //todo wsztrzyknąć tę logikę do countera jako obsługę on shuffle
        }
    }

    public Minion getCharacter() {
        return minion;
    }
}
