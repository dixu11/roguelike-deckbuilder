package dixu.deckard.server;

import javax.swing.*;

public class Game implements EventHandler {
    private static final int ENEMY_TEAM_INITIAL_BLOCK = 3;
    private final Team playerTeam;
    private final Team enemyTeam;

    public Game(Team playerTeam, Team enemyTeam) {
        this.playerTeam = playerTeam;
        this.enemyTeam = enemyTeam;
    }

    public void start() {
        EventBus eventBus = EventBus.getInstance();
        eventBus.register(this, GameStartedEvent.class);
        eventBus.register(this, GameOverEvent.class);
        eventBus.register(this, EndTurnEvent.class);
        eventBus.register(this, RandomDmgEvent.class);
        eventBus.register(this, StartTurnEvent.class);
        eventBus.register(this, MinionDiedEvent.class);

        //eventBus.post(new GameStartedEvent()); // nic nie robi więc zakomentowany
        eventBus.post(new StartTurnEvent());
        enemyTeam.addBlock(3);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GameStartedEvent) {

        } else if (event instanceof EndTurnEvent) {
            playerTeam.playCards();
            enemyTeam.clearBlock();
            enemyTeam.playCards();
            EventBus.getInstance().post(new StartTurnEvent());
        }else if(event instanceof RandomDmgEvent){
            Team target = playerTeam;
            RandomDmgEvent randomDmgEvent = (RandomDmgEvent) event;
            if (randomDmgEvent.getSentTeam() == TeamSide.LEFT) {
                target = enemyTeam;
            }
            target.applyRandomDmg(randomDmgEvent.getValue());
        } else if (event instanceof StartTurnEvent) {
            playerTeam.drawCards();
            enemyTeam.drawCards();
            playerTeam.clearBlock();
        } else if (event instanceof MinionDiedEvent) {
            Team target = enemyTeam;
            MinionDiedEvent characterDied = (MinionDiedEvent) event;
            if (characterDied.getSide() == TeamSide.LEFT) {
                target = playerTeam;
            }
            target.characterDied(characterDied.getCharacter());
        } else if (event instanceof GameOverEvent) {
            //todo update UI
            JOptionPane.showMessageDialog(null,"Koniec gry!");
            System.exit(0);
        }
        Game.animate();
    }

    public void endTurn() {
        EventBus.getInstance().post(new EndTurnEvent());
    }

    //get current player

    public static void animate() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
