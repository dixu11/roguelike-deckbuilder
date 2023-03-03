package dixu.deckard.client;

import dixu.deckard.server.*;
import dixu.deckard.server.event.*;

import java.awt.*;
import java.util.Optional;

import static dixu.deckard.client.GuiParams.*;

public class MinionView implements ActionEventHandler {

    private final BusManager bus = BusManager.instance();
    private final Minion minion;
    private final CardView cardView;
    private final MinionHandView minionHandView;
    private CounterView drawCounter;
    private CounterView discardCounter;
    private TeamView teamView;


    public MinionView(Minion minion, TeamView teamView) {
        this.minion = minion;
        this.teamView = teamView;

        minionHandView = new MinionHandView(minion);
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
                .strategy(((oldValue, e) -> e.getValue()))
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

    public void render(Graphics2D g) {
        cardView.render(g);
        minionHandView.render(g,CARD_PADDING-CARD_WIDTH, -CARD_HEIGHT - CARD_PADDING);//todo refactor to calculate center
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
            minionHandView.remove(event.getCard());
        }
    }

    private void onCardDraw(ActionEvent event) {
        if (event.getMinion() == minion) {
            minionHandView.add(event.getCard());
        }
    }

    public Optional<CardView> reactToClick(int x, int y) {
        return minionHandView.reactToClickOnScreen(x, y);
    }

    public Minion getMinion() {
        return minion;
    }
}
