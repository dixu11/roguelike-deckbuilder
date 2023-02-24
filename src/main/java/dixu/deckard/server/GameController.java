package dixu.deckard.server;

public class GameController{

    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    public void start() {
        game.start();
    }

    public void endTurn() {
        game.endTurn();
    }
}
