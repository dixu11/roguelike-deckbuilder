package dixu.deckard.client;

import dixu.deckard.server.event.*;
import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.minion.Minion;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;


import static dixu.deckard.client.GuiParams.*;

public class MinionView implements EventHandler {

    private static final Logger logger = LogManager.getLogger(MinionView.class);
    private final Minion minion;
    private final CardView minionCardView;
    private final MinionHandView minionHandView;
    private CounterView drawCounter;
    private CounterView discardCounter;
    private final TeamView teamView;
    private int transX;
    private int transY;


    public MinionView(Minion minion, TeamView teamView) {
        this.minion = minion;
        this.teamView = teamView;

        minionHandView = new MinionHandView(this);
        minionCardView = new CardView(minion.getMinionCard());

        Bus.register(this, ActionEventType.MINION_CARD_PLAYED);
        Bus.register(this, ActionEventType.MINION_CARD_DRAW);

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

        Bus.register(healthCounter, ActionEventType.MINION_DAMAGED);
        Bus.register(healthCounter, ActionEventType.MINION_REGENERATED);
        minionCardView.addCounter(healthCounter);

        EventCounterView drawCounter = EventCounterView.builder()
                .straightDirection(Direction.BOTTOM)
                .diagonalShift(Direction.LEFT)
                .color(GuiParams.MAIN_COLOR_BRIGHT)
                .description("\uD83C\uDCA0: ")
                .value(minion.getDraw().size())
                .source(minion)
                .strategy(
                        ((oldValue, e) -> e.getType() == ActionEventType.MINION_SHUFFLE ?
                                e.getMinion().getDraw().size() : oldValue - 1)
                )
                .build();

        Bus.register(drawCounter, ActionEventType.MINION_CARD_DRAW);
        Bus.register(drawCounter, ActionEventType.MINION_SHUFFLE);
        this.drawCounter = drawCounter;

        EventCounterView discardCounter = EventCounterView.builder()
                .straightDirection(Direction.BOTTOM)
                .diagonalShift(Direction.RIGHT)
                .color(GuiParams.MAIN_COLOR_BRIGHT)
                .blinking(false)
                .description("\uD83C\uDCC1: ")
                .source(minion)
                .strategy(((oldValue, e) -> e.getType() == ActionEventType.MINION_SHUFFLE ? 0 : oldValue + 1))
                .build();

        Bus.register(discardCounter, ActionEventType.MINION_CARD_PLAYED); //change to source eventHandler -> but make 2 versions!
        Bus.register(discardCounter, ActionEventType.MINION_SHUFFLE);
        this.discardCounter = discardCounter;
    }

    public void render(Graphics2D g) {
        transX = (int) g.getTransform().getTranslateX();
        transY = (int) g.getTransform().getTranslateY();
        minionCardView.render(g);
        int xTran = CARD_PADDING-CARD_WIDTH;
        int yTran = -CARD_HEIGHT - CARD_PADDING;
        g.translate(xTran,yTran);
        minionHandView.render(g);//todo refactor to calculate center
        g.translate(-xTran,-yTran);
        Rectangle r = new Rectangle(0, CARD_HEIGHT / 2, CARD_WIDTH, CARD_HEIGHT);
        drawCounter.render(g, r);
        discardCounter.render(g, r);
    }


    @Override
    public void handle(ActionEvent event) {
        switch (event.getType()) {
            case MINION_CARD_PLAYED -> onCardPlayed(event); // todo move to hand object
            case MINION_CARD_DRAW -> onCardDraw(event); // todo move to hand object
        }
    }

    private void onCardPlayed(ActionEvent event) {
        if (event.getMinion() == minion) {
            minionHandView.remove(event.getCard());
            logger.trace("Animation: CardPlayed");
        }
    }

    private void onCardDraw(ActionEvent event) {
        if (event.getMinion() == minion) {
            minionHandView.add(event.getCard());
        }
    }

    public void reactToClick(int x, int y) {
        if (minionCardView.isClicked(x,y,transX,transY,0)) {
           Bus.post(GuiEvent.builder()
                   .name(GuiEventType.MINION_SELECTED)
                   .cardView(minionCardView)
                   .minionView(this)
                   .teamView(teamView)
                   .build()
           );
        }
        minionHandView.reactToClickOnWindow(x, y);
    }

    public Minion getMinion() {
        return minion;
    }

    public TeamView getTeamView() {
        return teamView;
    }
}
