package dixu.deckard.client;

import dixu.deckard.server.*;
import dixu.deckard.server.event.*;

import java.awt.*;

import static dixu.deckard.client.GuiParams.CARD_HEIGHT;
import static dixu.deckard.client.GuiParams.CARD_WIDTH;

public class MinionView implements ActionEventHandler {

    private final BusManager bus = BusManager.instance();
    private final Minion minion;
    private final CardView cardView;
    private final HandView handView;
    private CounterView drawCounter;
    private CounterView discardCounter;

    public MinionView(Minion minion) {
        this.minion = minion;

        handView = new HandView();
        cardView = new CardView(minion.getMinionCard());

        bus.register(this, ActionEventName.MINION_CARD_PLAYED);
        bus.register(this, ActionEventName.MINION_CARD_DRAW);

        setupCounters();
    }

    private void setupCounters() {
        EventCounterView healthCounter = EventCounterView.builder()
                .straightDirection(Direction.BOTTOM)
                .diagonalShift(Direction.BOTTOM)
                .description("♥: ")
                .source(minion)
                .value(minion.getHealth())
                .strategy(((oldValue, e) -> e.value()))
                .build();

        bus.register(healthCounter, ActionEventName.MINION_DAMAGED);
        cardView.addCounter(healthCounter);

        EventCounterView drawCounter = EventCounterView.builder()
                .straightDirection(Direction.BOTTOM)
                .diagonalShift(Direction.LEFT)
                .color(GuiParams.MAIN_COLOR_BRIGHT)
                .description("\uD83C\uDCA0: ")
                .value(minion.getDraw().size())
                .source(minion)
                .strategy(
                        ((oldValue, e) -> e.getName() == ActionEventName.MINION_SHUFFLE ?
                                e.getMinion().getDraw().size() : oldValue - 1)
                )
                .build();

        bus.register(drawCounter, ActionEventName.MINION_CARD_DRAW);
        bus.register(drawCounter, ActionEventName.MINION_SHUFFLE);
        this.drawCounter = drawCounter;

        EventCounterView discardCounter = EventCounterView.builder()
                .straightDirection(Direction.BOTTOM)
                .diagonalShift(Direction.RIGHT)
                .color(GuiParams.MAIN_COLOR_BRIGHT)
                .blinking(false)
                .description("\uD83C\uDCC1: ")
                .source(minion)
                .strategy(((oldValue, e) -> e.getName() == ActionEventName.MINION_SHUFFLE ? 0 : oldValue + 1))
                .build();

        bus.register(discardCounter, ActionEventName.MINION_CARD_PLAYED);
        bus.register(discardCounter, ActionEventName.MINION_SHUFFLE);
        this.discardCounter = discardCounter;
    }

    public void render(Graphics g) {
        cardView.render(g);
        g.translate(-CARD_WIDTH, -CARD_HEIGHT - 20); //todo definitively needs refactor ^^'
        handView.render(g);
        g.translate(CARD_WIDTH, CARD_HEIGHT + 20);
        Rectangle r = new Rectangle(0, CARD_HEIGHT / 2, CARD_WIDTH, CARD_HEIGHT);
        drawCounter.render(g, r);
        discardCounter.render(g, r);
    }


    @Override
    public void handle(ActionEvent event) {
        switch (event.getName()) {
            case MINION_CARD_PLAYED -> onCardPlayed(event);
            case MINION_CARD_DRAW -> onCardDraw(event);
        }
    }

    private void onCardPlayed(ActionEvent event) {
        if (event.getMinion() == minion) {
            handView.remove(event.getCard());
        }
    }

    private void onCardDraw(ActionEvent event) {
        if (event.getMinion() == minion) {
            handView.add(event.getCard());
        }
    }

    public Minion getCharacter() {
        return minion;
    }
}
