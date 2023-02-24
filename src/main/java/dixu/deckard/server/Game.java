package dixu.deckard.server;

import java.util.List;

public class Game implements EventHandler {
    private final Team playerTeam;
    private final Team computerTeam;


    public Game(Team playerTeam, Team computerTeam) {
        this.playerTeam = playerTeam;
        this.computerTeam = computerTeam;
    }

    public void start() {
        EventBus eventBus = EventBus.getInstance();
        eventBus.register(this, GameStartedEvent.class);
        eventBus.register(this, GameOverEvent.class);
        eventBus.register(this, NextTurnEvent.class);
        eventBus.post(new GameStartedEvent());
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GameStartedEvent) {
            System.out.println("Game: started");
        } else if (event instanceof NextTurnEvent) {
            playerTeam.playBlocks();
//            computer.playBlock();
//            player.playAttack();
//            computer.playAttack();
        }

    }

    public void endTurn() {
        EventBus.getInstance().post(new NextTurnEvent());
    }

    //get current player


}
