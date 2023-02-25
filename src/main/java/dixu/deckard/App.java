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
        FightViewImpl fightView = new FightViewImpl(new EndTurnButtonView(),playerTeam,enemyTeam);

        //create engine and connections
        GameEngine engine = new GameEngine(display, fightView);
        Game game = new Game(player,enemy);
        GameController gameController = new GameController(game);
        fightView.setController(gameController);
        display.addListener(fightView);
        gameController.start();
        engine.start();
    }
}
