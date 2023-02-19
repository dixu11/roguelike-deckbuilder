package dixu.deckard;

import dixu.deckard.client.Display;
import dixu.deckard.client.GameEngine;
import dixu.deckard.client.GameViewSwing;
import dixu.deckard.client.PlayerViewSwing;
import dixu.deckard.server.Game;
import dixu.deckard.server.GameController;
import dixu.deckard.server.Player;

public class App {
    public static void main(String[] args) {
        Display display = new Display("Deckard Thief");
        GameViewSwing gameViewSwing = new GameViewSwing();
        GameEngine engine = new GameEngine(display, gameViewSwing);
        engine.start();
        Player player = new Player("Player");
        Player computer = new Player("Computer");
        Game game = new Game(player,computer);
        PlayerViewSwing playerView = new PlayerViewSwing(player.getName());
        PlayerViewSwing computerView = new PlayerViewSwing(player.getName());
        playerView.addAll(player.getHand());
        computerView.addAll(computer.getHand());


        GameController gameController = new GameController(game, playerView, computerView);
        gameController.start();

    }
}
