package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.client.HandViewImpl;
import dixu.deckard.server.*;

public class App {
    public static void main(String[] args) {
        Display display = new Display("Deckard Thief");
        TrainerFactory trainerFactory = new TrainerFactory();
        Computer computer = trainerFactory.createComputer();
        Player player = trainerFactory.createPlayer();
        HandViewImpl handViewImpl = new HandViewImpl(player);

        TeamView playerTeam = new TeamView(player.getTeam(),Direction.LEFT);
        TeamView enemyTeam = new TeamView(computer.getTeam(), Direction.RIGHT);
        GameViewImpl gameViewImpl = new GameViewImpl(handViewImpl, new EndTurnButtonView(),playerTeam,enemyTeam);
        GameEngine engine = new GameEngine(display, gameViewImpl);
        engine.start();
        Game game = new Game(player,computer);
        GameController gameController = new GameController(game, handViewImpl);
        gameViewImpl.setController(gameController);
        display.addListener(gameViewImpl);
        gameController.start();
    }
}
