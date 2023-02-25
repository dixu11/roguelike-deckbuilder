package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.*;

public class App {
    public static void main(String[] args) {
        Display display = new Display("Deckard Thief");
        TeamFactory teamFactory = new TeamFactory();
        Team enemy = teamFactory.createComputer();
        Team player = teamFactory.createPlayer();
        TeamView playerTeam = new TeamView(player,Direction.LEFT);
        TeamView enemyTeam = new TeamView(enemy, Direction.RIGHT);
        GameViewImpl gameViewImpl = new GameViewImpl(new EndTurnButtonView(),playerTeam,enemyTeam);
        GameEngine engine = new GameEngine(display, gameViewImpl);
        engine.start();
        Game game = new Game(player,enemy);
        GameController gameController = new GameController(game);
        gameViewImpl.setController(gameController);
        display.addListener(gameViewImpl);
        gameController.start();
    }
}
