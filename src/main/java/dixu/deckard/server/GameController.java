package dixu.deckard.server;

public class GameController{

    private final Game game;
    private final PlayerView player;

    public GameController(Game game, PlayerView player) {
        this.game = game;
        this.player = player;
        player.setController(this);
    }

    public void start() {
        game.start();
    }

    public void playCard(Player player, int index) {
        game.playCard(player, index);
    }
}
