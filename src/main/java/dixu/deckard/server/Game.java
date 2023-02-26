package dixu.deckard.server;

import dixu.deckard.server.event.*;

public class Game implements EventHandler {

    private static final int SECOND_TEAM_INITIAL_BLOCK_BONUS = 3;
    private final EventBus bus = EventBus.getInstance();
    private final Team firstTeam;
    private final Team secondTeam;

    public Game(Team firstTeam, Team secondTeam) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;

        bus.register(this, TurnEndedEvent.class);
        bus.register(this, TurnStartedEvent.class);
        bus.register(this, MinionDiedEvent.class);
    }

    public void start() {
        bus.post(new TurnStartedEvent());
        secondTeam.addBlock(SECOND_TEAM_INITIAL_BLOCK_BONUS);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof TurnStartedEvent) {
            onTurnStart();
        } else if (event instanceof TurnEndedEvent) {
            onTurnEnd();
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
        EventBus.getInstance().post(new TurnStartedEvent());
    }

    private CardContext createContextForPlayer() {
        return CardContext.builder()
                .actionTeam(firstTeam)
                .enemyTeam(secondTeam)
                .build();
    }

    private CardContext createContextForComputer() {
        return CardContext.builder()
                .actionTeam(secondTeam)
                .enemyTeam(firstTeam)
                .build();
    }

    //get current player
    public static void animate() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
