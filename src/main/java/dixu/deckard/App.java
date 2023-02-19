package dixu.deckard;

import dixu.deckard.client.Display;
import dixu.deckard.client.GameEngine;
import dixu.deckard.client.GameViewSwing;
import dixu.deckard.client.PlayerViewSwing;
import dixu.deckard.server.Game;
import dixu.deckard.server.GameController;
import dixu.deckard.server.Player;
import dixu.deckard.server.PlayerType;

public class App {
    public static void main(String[] args) {
        Display display = new Display("Deckard Thief");
        Player computer = new Player(PlayerType.COMPUTER);
        Player player = new Player(PlayerType.PLAYER);
        PlayerViewSwing computerView = new PlayerViewSwing(computer.getType());
        PlayerViewSwing playerView = new PlayerViewSwing(player.getType());
        GameViewSwing gameViewSwing = new GameViewSwing(computerView,playerView);
        GameEngine engine = new GameEngine(display, gameViewSwing);
        engine.start();
        Game game = new Game(player,computer);
        playerView.addAll(player.getHand());
        computerView.addAll(computer.getHand());


        GameController gameController = new GameController(game, playerView, computerView);
        gameController.start();

    }
}
