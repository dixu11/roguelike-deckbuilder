package dixu.deckard.client;

import dixu.deckard.server.*;
import dixu.deckard.server.event.*;

import java.awt.*;

public class MinionView implements EventHandler<MinionEvent> {

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
        healthCounter.setParent(minion);
        healthCounter.setValue(minion.getHealth());
        EventBus.getInstance().register(healthCounter, MinionDamagedEvent.class);
        cardView.addCounter(healthCounter);
        CounterView drawCounter = new SourceCounterView(Direction.BOTTOM, Direction.LEFT, () -> minion.getDraw().size(), Color.GRAY);
        drawCounter.setDescription("\uD83C\uDCA0: ");
        drawCounter.setValue(minion.getDraw().size());
        this.drawCounter = drawCounter;
        CounterView discardCounter = new EventCounterView(Direction.BOTTOM, Direction.RIGHT, Color.GRAY);
        discardCounter.setBlinking(false);
        discardCounter.setDescription("\uD83C\uDCC1: ");
        this.discardCounter = discardCounter;
        EventBus.getInstance().register(this, CardPlayedEvent.class);
        EventBus.getInstance().register(this, DrawCardEvent.class);
        EventBus.getInstance().register(this,ShuffleEvent.class);
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


    public Minion getCharacter() {
        return minion;
    }

    //todo CAN SOMEBODY TELL ME HOW TO IMPLEMENT THIS WITHOUT NEED OF INSTANCEOF?

    @Override
    public void handle(MinionEvent event) {
        event.accept(this);
    }

    public void handleCardPlayed(CardPlayedEvent event) {
        CardContext context = event.getPlayContext();
        if (context.getMinion()==minion) {
            discardCounter.addValue(1);
            handView.remove(context.getCard());
        }
    }

    public void handleCardDraw(DrawCardEvent event) {
        CardContext context = event.getPlayContext();
        if (context.getMinion() == minion) {
            handView.addCard(context.getCard());
            drawCounter.setValue(minion.getDraw().size());
        }
    }

    public void handleShuffle(ShuffleEvent shuffleEvent) {
        if (shuffleEvent.getMinion() == minion) {
            discardCounter.setValue(minion.getDiscard().size());
            drawCounter.setValue(minion.getDraw().size());
        }
    }
}
