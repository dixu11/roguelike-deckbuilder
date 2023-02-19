package dixu.deckard.server;

public class Game implements EventHandler{
    private final EventBus eventBus = new EventBus();
    private final Player player;
    private final Player computer;


    public Game(Player player, Player computer) {
        this.player = player;
        this.computer = computer;
    }

    public void start() {
        eventBus.register(this,GameStartedEvent.class);
        eventBus.register(this,GameOverEvent.class);
        eventBus.register(this,TurnChangedEvent.class);

        eventBus.post(new GameStartedEvent());
    }

    @Override
    public void handle(Event event) {
        if (event instanceof GameStartedEvent) {
            System.out.println("Game: started");
        }
    }

    public void onPlayerTurn(Event event){

    }

    public void onCardPlayed(Event event) {

    }

    public void onGameOver(Event event) {

    }
    //metody dla clienta:

    public void playCard(Player player, int index) {
        Card card = player.playCard(index);
        eventBus.post(new CardPlayedEvent(player, card,null ));
    }

    //get current player


}
