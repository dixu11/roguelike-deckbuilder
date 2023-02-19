package dixu.deckard.server;

public class GameController{

    private final Game game;
    private final PlayerView player1View;
    private final PlayerView player2View;

    public GameController(Game game, PlayerView player1View, PlayerView player2View) {
        this.game = game;
        this.player1View = player1View;
        this.player2View = player2View;
        player1View.setController(this);
        player2View.setController(this);
    }

    public void start() {
        game.start();
    }

    public void playCard(Player player, int index) {
        game.playCard(player, index);
    }
}
