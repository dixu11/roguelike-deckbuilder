package dixu.deckard.server;

import dixu.deckard.server.event.*;

public class Game implements EventHandler {

    private static final int SECOND_TEAM_INITIAL_BLOCK_BONUS = 3;
    private final Team firstTeam;
    private final Team secondTeam;
    private final EventBus eventBus = EventBus.getInstance();

    public Game(Team firstTeam, Team secondTeam) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;

        eventBus.register(this, TurnEndedEvent.class);
        eventBus.register(this, TurnStartedEvent.class);
        eventBus.register(this, MinionDiedEvent.class);
    }

    public void start() {
        eventBus.post(new TurnStartedEvent());
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
        firstTeam.startTurnDrawCards(createContextForPlayer());
        secondTeam.startTurnDrawCards(createContextForComputer());
        firstTeam.clearBlock();
    }

    private void onTurnEnd() {
        firstTeam.playCards(createContextForPlayer());
        secondTeam.clearBlock();
        secondTeam.playCards(createContextForComputer());
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
