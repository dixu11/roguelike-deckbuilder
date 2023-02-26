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
            playerTeam.drawCards();
            computerTeam.drawCards();
            playerTeam.clearBlock();
        } else if (event instanceof EndTurnEvent) {
            playerTeam.playCards(new PlayContext(playerTeam, computerTeam));
            computerTeam.clearBlock();
            computerTeam.playCards(new PlayContext(computerTeam, playerTeam));
            EventBus.getInstance().post(new StartTurnEvent());
        }
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
