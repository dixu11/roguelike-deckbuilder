package dixu.deckard.server;

import dixu.deckard.server.event.*;

public class Game implements CoreEventHandler {

    private final static double PLAY_DELAY_SECONDS = 1.0;
    private static final int SECOND_TEAM_INITIAL_BLOCK_BONUS = 3;
    private final BusManager bus = BusManager.instance();
    private final Team firstTeam;
    private final Team secondTeam;

    public Game(Team firstTeam, Team secondTeam) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;

        bus.register(this, CoreEventName.TURN_ENDED);
        bus.register(this, CoreEventName.TURN_STARTED);
    }

    public void start() {
        bus.post(CoreEvent.of(CoreEventName.TURN_STARTED));
        secondTeam.addBlock(SECOND_TEAM_INITIAL_BLOCK_BONUS);
    }

    @Override
    public void handle(CoreEvent event) {
        switch (event.getName()) {
            case TURN_STARTED -> onTurnStart();
            case TURN_ENDED -> onTurnEnd();
        }
    }

    private void onTurnStart() {
        firstTeam.executeStartTurnCardDraws(createContextForPlayer());
        secondTeam.executeStartTurnCardDraws(createContextForComputer());
        firstTeam.clearBlock();
    }

    private void onTurnEnd() {
        firstTeam.playAllCards(createContextForPlayer());
        secondTeam.clearBlock();
        secondTeam.playAllCards(createContextForComputer());
        bus.post(CoreEvent.of(CoreEventName.TURN_STARTED));
    }

    private CardContext createContextForPlayer() {
        return CardContext.builder()
                .ownTeam(firstTeam)
                .enemyTeam(secondTeam)
                .build();
    }

    private CardContext createContextForComputer() {
        return CardContext.builder()
                .ownTeam(secondTeam)
                .enemyTeam(firstTeam)
                .build();
    }

    public static void delayForAnimation() {
        try {
            Thread.sleep((long) (PLAY_DELAY_SECONDS * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
