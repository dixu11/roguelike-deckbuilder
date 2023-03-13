package dixu.deckard.server.combat;

import dixu.deckard.server.event.EventHandler;
import dixu.deckard.server.event.CoreEvent;
import dixu.deckard.server.event.CoreEventType;
import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.leader.Special;
import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.team.Team;

import static dixu.deckard.server.game.GameParams.SECOND_TEAM_INITIAL_BLOCK;
/**
 * {@link Combat} is central gameplay part of a whole game. In {@link Combat}s {@link Leader}s put their {@link Minion}s
 * to the test for life and death. Concept is a little similar to the Pokémon series because {@link Minion}s are not
 * directly controlled and {@link Leader} can influence their actions by modifying their decks of {@link Card}s.
 *<p>
 *  In this version first {@link Leader} is controlled by a player and second one is controlled by the game. Only
 *  player have {@link Special} abilities, and he has to use them to win a {@link Combat}. The greatest power of
 *  player {@link Leader} is ability to steal {@link Card}s from his enemy {@link Minion}s and pass them to their {@link Minion}s.
 *  <p>
 * Round consist of two phases. In first phase, called a turn, both leaders spend their {@link Leader#getEnergy}
 * to use their {@link Special}s until they decide to pass a turn.
 * <p>
 * In second phase first {@link Team} clears the block and its {@link Minion}s play their {@link Card}s, then
 * second {@link Team} block is cleared, and they play their {@link Card}s.
 * <p>
 * Now next round starts by {@link Minion}s drawing their new {@link Card}s and shuffling their decks if needed. {@link Combat} lasts
 * until all {@link Minion}s of one {@link Team} are slayed.
* */

public class Combat implements EventHandler {
    private final Leader firstLeader;
    private final Team firstTeam;
    private final Team secondTeam;
    private final CombatState combatState = CombatState.getInstance();
    private static boolean delayCardPlay = true;

    public Combat(Leader firstLeader, Leader secondLeader) {
        this.firstTeam = firstLeader.getTeam();
        this.secondTeam = secondLeader.getTeam();
        this.firstLeader = firstLeader;

        Bus.register(this, CoreEventType.SETUP_PHASE_STARTED);
        Bus.register(this, CoreEventType.LEADER_PHASE_STARTED);
        Bus.register(this, CoreEventType.MINION_PHASE_STARTED);
    }

    public void start() {
        secondTeam.addBlock(SECOND_TEAM_INITIAL_BLOCK);
        Bus.post(CoreEvent.of(CoreEventType.SETUP_PHASE_STARTED));
    }

    @Override
    public void handle(CoreEvent event) {
        switch (event.getType()) {
            case SETUP_PHASE_STARTED -> onSetupPhase();
            case LEADER_PHASE_STARTED -> onLeader();
            case MINION_PHASE_STARTED -> onMinionPhase();
        }
    }

    private void onSetupPhase() {
        combatState.setPhase(Phase.SETUP);
        firstTeam.minionsDrawCards();
        secondTeam.minionsDrawCards();
        firstTeam.clearBlock();
        firstLeader.regenerateEnergy();
        Bus.post(CoreEvent.of(CoreEventType.LEADER_PHASE_STARTED));
    }

    private void onLeader() {
        combatState.setPhase(Phase.LEADER);
    }

    private void onMinionPhase() {
        combatState.setPhase(Phase.MINIONS);
        firstTeam.playAllCards(createContextForFirstTeam());
        secondTeam.clearBlock();
        secondTeam.playAllCards(createContextForSecondTeam());
        Bus.post(CoreEvent.of(CoreEventType.SETUP_PHASE_STARTED));
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

    public static void delayForAnimation(double delayTimeSeconds) {
        if (!delayCardPlay) {
            return;
        }
        try {
            Thread.sleep((long) (delayTimeSeconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //for tests
    public static void disableDelay() {
        delayCardPlay = false;
    }
}
