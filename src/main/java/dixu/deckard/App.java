package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.*;

public class App {
    public static void main(String[] args) {
        Display display = new Display("Deckard Thief");
        TrainerFactory trainerFactory = new TrainerFactory();
        Computer computer = trainerFactory.createComputer();
        Player player = trainerFactory.createPlayer();


        TeamView playerTeam = new TeamView(player.getTeam(),Direction.LEFT);
        TeamView enemyTeam = new TeamView(computer.getTeam(), Direction.RIGHT);
        GameViewImpl gameViewImpl = new GameViewImpl(new EndTurnButtonView(),playerTeam,enemyTeam);
        GameEngine engine = new GameEngine(display, gameViewImpl);
        engine.start();
        Game game = new Game(player,computer);
        GameController gameController = new GameController(game);
        gameViewImpl.setController(gameController);
        display.addListener(gameViewImpl);
        gameController.start();
    }
}
