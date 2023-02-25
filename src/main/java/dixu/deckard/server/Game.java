package dixu.deckard.server;

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
        eventBus.register(this, RandomDmgEvent.class);
        eventBus.post(new GameStartedEvent());
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GameStartedEvent) {
            System.out.println("Game: started");
        } else if (event instanceof NextTurnEvent) {
            playerTeam.playCards();
            computerTeam.playCards();
        }else if(event instanceof RandomDmgEvent){
            Team target = playerTeam;
            RandomDmgEvent randomDmgEvent = (RandomDmgEvent) event;
            if (randomDmgEvent.getSentTeam() == TeamSide.LEFT) {
                target = computerTeam;
            }
            target.applyRandomDmg(randomDmgEvent.getValue());
        }
        Game.animate();
    }

    public void endTurn() {
        EventBus.getInstance().post(new NextTurnEvent());
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
