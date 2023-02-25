package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.*;

public class App {
    public static void main(String[] args) {
        //create frame class and initialize window size
        Display display = new Display("Deckard Thief");

        //create team
        TeamFactory teamFactory = new TeamFactory();
        Team enemy = teamFactory.createEnemy();
        Team player = teamFactory.createPlayer();

        //create views
        TeamView playerTeam = new TeamView(player,Direction.LEFT);
        TeamView enemyTeam = new TeamView(enemy, Direction.RIGHT);
        GameViewImpl gameViewImpl = new GameViewImpl(new EndTurnButtonView(),playerTeam,enemyTeam);

        //create engine and connections
        GameEngine engine = new GameEngine(display, gameViewImpl);
        Game game = new Game(player,enemy);
        GameController gameController = new GameController(game);
        gameViewImpl.setController(gameController);
        display.addListener(gameViewImpl);
        gameController.start();
        engine.start();
    }
}
