package dixu.deckard.server;

public class GameController{

    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    public void start() {
        game.start();
    }

    public void playCard(Player player, int index) {
        game.playCard(player, index);
    }

    public void endTurn() {
        game.endTurn();
    }
}
