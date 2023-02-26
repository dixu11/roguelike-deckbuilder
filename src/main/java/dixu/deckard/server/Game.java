package dixu.deckard.server;

import dixu.deckard.server.event.*;

import javax.swing.*;

public class Game implements EventHandler {
    private final Team playerTeam;
    private final Team enemyTeam;

    public Game(Team playerTeam, Team enemyTeam) {
        this.playerTeam = playerTeam;
        this.enemyTeam = enemyTeam;
    }

    public void start() {
        EventBus eventBus = EventBus.getInstance();
        eventBus.register(this, EndTurnEvent.class);
        eventBus.register(this, StartTurnEvent.class);
        eventBus.register(this, MinionDiedEvent.class);
        eventBus.post(new StartTurnEvent());
        enemyTeam.addBlock(3);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GameStartedEvent) {

        } else if (event instanceof EndTurnEvent) {
            playerTeam.playCards(this);
            enemyTeam.clearBlock();
            enemyTeam.playCards(this);
            EventBus.getInstance().post(new StartTurnEvent());
        } else if (event instanceof StartTurnEvent) {
            playerTeam.drawCards();
            enemyTeam.drawCards();
            playerTeam.clearBlock();
        }
    }

    public void endTurn() {
        EventBus.getInstance().post(new EndTurnEvent());
    }

    //get current player

    public static void animate() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Team getEnemyTeamFor(Team team) {
        return team.getSide() == TeamSide.LEFT ? enemyTeam : playerTeam;
    }
}
