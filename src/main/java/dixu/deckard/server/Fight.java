package dixu.deckard.server;

import dixu.deckard.server.event.*;

import static dixu.deckard.server.GameParams.SECOND_TEAM_INITIAL_BLOCK;
/**
 * {@link Fight} is central gameplay part of a whole game. In {@link Fight}s {@link Leader}s put their {@link Minion}s
 * to the test for life and death. Concept is a little similar to the Pokémon series because {@link Minion}s are not
 * directly controlled and {@link Leader} can influence their actions by modifying their decks of {@link Card}s.
 *<p>
 *  In this version first {@link Leader} is controlled by a player and second one is controlled by the game. Only
 *  player have {@link Special} abilities, and he has to use them to win a {@link Fight}. The greatest power of
 *  player {@link Leader} is ability to steal {@link Card}s from his enemy {@link Minion}s and pass them to their {@link Minion}s.
 *  <p>
 * Round consist of two phases. In first phase, called a turn, both leaders spend their {@link Leader#getEnergy}
 * to use their {@link Special}s until they decide to pass a turn.
 * <p>
 * In second phase first {@link Team} clears the block and its {@link Minion}s play their {@link Card}s, then
 * second {@link Team} block is cleared, and they play their {@link Card}s.
 * <p>
 * Now next round starts by {@link Minion}s drawing their new {@link Card}s and shuffling their decks if needed. {@link Fight} lasts
 * until all {@link Minion}s of one {@link Team} are slayed.
* */

public class Fight implements CoreEventHandler {

    private final static double PLAY_DELAY_SECONDS = 2;
    private final BusManager bus = BusManager.instance();
    private final Team firstTeam;
    private final Team secondTeam;
    private final Leader firstLeader;
    private static boolean delayCardPlay = true;

    public Fight(Leader firstLeader, Leader secondLeader) {
        this.firstTeam = firstLeader.getTeam();
        this.secondTeam = secondLeader.getTeam();
        this.firstLeader = firstLeader;

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
        firstTeam.executeStartTurnCardDraws();
        secondTeam.executeStartTurnCardDraws();
        firstTeam.clearBlock();
        firstLeader.regenerateEnergy();

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
