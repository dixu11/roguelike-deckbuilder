package dixu.deckard.server;

import dixu.deckard.server.event.*;

public class Game implements EventHandler {
    private final Team playerTeam;
    private final Team computerTeam;

    public Game(Team playerTeam, Team computerTeam) {
        this.playerTeam = playerTeam;
        this.computerTeam = computerTeam;
    }

    public void start() {
        EventBus eventBus = EventBus.getInstance();
        eventBus.register(this, EndTurnEvent.class);
        eventBus.register(this, StartTurnEvent.class);
        eventBus.register(this, MinionDiedEvent.class);
        eventBus.post(new StartTurnEvent());
        computerTeam.addBlock(3);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof StartTurnEvent) {
            playerTeam.startTurnDrawCards(createContextForPlayer());
            computerTeam.startTurnDrawCards(createContextForComputer());
            playerTeam.clearBlock();
        } else if (event instanceof EndTurnEvent) {
            playerTeam.playCards(createContextForPlayer());
            computerTeam.clearBlock();
            computerTeam.playCards(createContextForComputer());
            EventBus.getInstance().post(new StartTurnEvent());
        }
    }

    private CardContext createContextForPlayer() {
        return CardContext.builder()
                .actionTeam(playerTeam)
                .enemyTeam(computerTeam)
                .build();
    }

    private CardContext createContextForComputer() {
        return CardContext.builder()
                .actionTeam(computerTeam)
                .enemyTeam(playerTeam)
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

    public Team getEnemyTeamFor(Team team) {
        return team.getSide() == TeamSide.LEFT ? computerTeam : playerTeam;
    }
}
