package dixu.deckard.server;

public class Game implements EventHandler {
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
        eventBus.post(new GameStartedEvent());
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GameStartedEvent) {
            System.out.println("Game: started");
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
        }
        Game.animate();
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

}
