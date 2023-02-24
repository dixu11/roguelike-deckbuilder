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
        PlayerViewSwing playerView = new PlayerViewSwing(player);
        GameViewSwing gameViewSwing = new GameViewSwing(playerView, new EndTurnButton());
        GameEngine engine = new GameEngine(display, gameViewSwing);
        engine.start();
        Game game = new Game(player,computer);



        GameController gameController = new GameController(game, playerView);
        gameController.start();

    }
}
