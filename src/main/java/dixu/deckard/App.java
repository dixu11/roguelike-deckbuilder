package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.Game;
import dixu.deckard.server.GameController;
import dixu.deckard.server.Player;

public class App {
    public static void main(String[] args) {
        Display display = new Display("Deckard Thief");
        Player computer = new Player();
        Player player = new Player();
        PlayerView playerView = new PlayerView(player);
        GameViewImpl gameViewImpl = new GameViewImpl(playerView, new EndTurnButtonView());
        GameEngine engine = new GameEngine(display, gameViewImpl);
        engine.start();
        Game game = new Game(player,computer);
        GameController gameController = new GameController(game, playerView);
        gameViewImpl.setController(gameController);
        display.addListener(gameViewImpl);
        gameController.start();
    }
}
