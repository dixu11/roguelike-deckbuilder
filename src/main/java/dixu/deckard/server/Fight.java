package dixu.deckard.server;

import dixu.deckard.server.event.*;

import static dixu.deckard.server.GameParams.SECOND_TEAM_INITIAL_BLOCK;

public class Fight implements CoreEventHandler {

    private final static double PLAY_DELAY_SECONDS = 2;
    private final BusManager bus = BusManager.instance();
    private final Team firstTeam;
    private final Team secondTeam;
    private static boolean delayCardPlay = true;

    public Fight(Leader firstLeader, Leader secondLeader) {
        this.firstTeam = firstLeader.getTeam();
        this.secondTeam = secondLeader.getTeam();

        bus.register(this, CoreEventName.TURN_ENDED);
        bus.register(this, CoreEventName.TURN_STARTED);
    }

    public void start() {
        bus.post(CoreEvent.of(CoreEventName.TURN_STARTED));
        secondTeam.addBlock(SECOND_TEAM_INITIAL_BLOCK);
    }

    @Override
    public void handle(CoreEvent event) {
        switch (event.getName()) {
            case TURN_STARTED -> onTurnStart();
            case TURN_ENDED -> onTurnEnd();
        }
    }

    private void onTurnStart() {
        firstTeam.executeStartTurnCardDraws(createContextForFirstTeam());
        secondTeam.executeStartTurnCardDraws(createContextForSecondTeam());
        firstTeam.clearBlock();
    }

    private void onTurnEnd() {
        firstTeam.playAllCards(createContextForFirstTeam());
        secondTeam.clearBlock();
        secondTeam.playAllCards(createContextForSecondTeam());
        bus.post(CoreEvent.of(CoreEventName.TURN_STARTED));
    }

    private CardContext createContextForFirstTeam() {
        return CardContext.builder()
                .ownTeam(firstTeam)
                .enemyTeam(secondTeam)
                .build();
    }

    private CardContext createContextForSecondTeam() {
        return CardContext.builder()
                .ownTeam(secondTeam)
                .enemyTeam(firstTeam)
                .build();
    }

    public static void delayForAnimation() {
        if (!delayCardPlay) {
            return;
        }
        try {
            Thread.sleep((long) (PLAY_DELAY_SECONDS * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disableDaley() {
        delayCardPlay = false;
    }
}
